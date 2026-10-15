package com.mahmoud.bank.service;

import com.mahmoud.bank.dao.AccountDao;
import com.mahmoud.bank.dao.TransactionDao;
import com.mahmoud.bank.model.Account;
import com.mahmoud.bank.model.Transaction;
import com.mahmoud.bank.model.TransactionType;
import com.mahmoud.bank.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionService {

    private static final String ERROR = "Something went wrong, please try again";

    private final AccountDao accountDao = new AccountDao();
    private final TransactionDao transactionDao = new TransactionDao();

    public void deposit(long userId, long accountId, BigDecimal amount) throws BankException {
        validateAmount(amount);

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Account account = lockOwnedActiveAccount(con, userId, accountId);
                BigDecimal newBalance = account.getBalance().add(amount);

                accountDao.updateBalance(con, accountId, newBalance);
                transactionDao.save(con, new Transaction(accountId, TransactionType.DEPOSIT, amount, newBalance, null));

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public void withdraw(long userId, long accountId, BigDecimal amount) throws BankException {
        validateAmount(amount);

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Account account = lockOwnedActiveAccount(con, userId, accountId);
                if (account.getBalance().compareTo(amount) < 0) {
                    throw new BankException("Insufficient balance");
                }
                BigDecimal newBalance = account.getBalance().subtract(amount);

                accountDao.updateBalance(con, accountId, newBalance);
                transactionDao.save(con, new Transaction(accountId, TransactionType.WITHDRAWAL, amount, newBalance, null));

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public void transfer(long userId, long fromAccountId, String toAccountNumber, BigDecimal amount) throws BankException {
        validateAmount(amount);
        if (toAccountNumber == null || toAccountNumber.isBlank()) {
            throw new BankException("Target account number is required");
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                Account target = accountDao.findByNumber(con, toAccountNumber.trim());
                if (target == null) {
                    throw new BankException("Target account not found");
                }
                if (target.getId() == fromAccountId) {
                    throw new BankException("You can't transfer to the same account");
                }

                long firstId = Math.min(fromAccountId, target.getId());
                long secondId = Math.max(fromAccountId, target.getId());
                Account first = accountDao.findByIdForUpdate(con, firstId);
                Account second = accountDao.findByIdForUpdate(con, secondId);
                if (first == null || second == null) {
                    throw new BankException("Account not found");
                }

                Account from = first.getId() == fromAccountId ? first : second;
                Account to = from == first ? second : first;

                if (from.getUserId() != userId) {
                    throw new BankException("Account not found");
                }
                if (!from.isActive() || !to.isActive()) {
                    throw new BankException("Both accounts must be active");
                }
                if (from.getBalance().compareTo(amount) < 0) {
                    throw new BankException("Insufficient balance");
                }

                BigDecimal fromBalance = from.getBalance().subtract(amount);
                BigDecimal toBalance = to.getBalance().add(amount);

                accountDao.updateBalance(con, from.getId(), fromBalance);
                accountDao.updateBalance(con, to.getId(), toBalance);

                transactionDao.save(con, new Transaction(from.getId(), TransactionType.TRANSFER_OUT, amount, fromBalance, to.getAccountNumber()));
                transactionDao.save(con, new Transaction(to.getId(), TransactionType.TRANSFER_IN, amount, toBalance, from.getAccountNumber()));

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    private Account lockOwnedActiveAccount(Connection con, long userId, long accountId) throws SQLException, BankException {
        Account account = accountDao.findByIdForUpdate(con, accountId);
        if (account == null || account.getUserId() != userId) {
            throw new BankException("Account not found");
        }
        if (!account.isActive()) {
            throw new BankException("Account is closed");
        }
        return account;
    }

    private void validateAmount(BigDecimal amount) throws BankException {
        if (amount == null || amount.signum() <= 0) {
            throw new BankException("Amount must be greater than zero");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new BankException("Amount can't have more than 2 decimal places");
        }
    }
}
