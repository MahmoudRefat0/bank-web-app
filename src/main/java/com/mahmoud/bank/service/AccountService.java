package com.mahmoud.bank.service;

import com.mahmoud.bank.dao.AccountDao;
import com.mahmoud.bank.dao.TransactionDao;
import com.mahmoud.bank.model.Account;
import com.mahmoud.bank.model.AccountStatus;
import com.mahmoud.bank.model.AccountType;
import com.mahmoud.bank.model.Transaction;
import com.mahmoud.bank.util.DBConnection;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AccountService {

    private static final String ERROR = "Something went wrong, please try again";

    private final AccountDao accountDao = new AccountDao();
    private final TransactionDao transactionDao = new TransactionDao();
    private final SecureRandom random = new SecureRandom();

    public List<Account> getUserAccounts(long userId) throws BankException {
        try (Connection con = DBConnection.getConnection()) {
            return accountDao.findByUserId(con, userId);
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public Account openAccount(long userId, AccountType type) throws BankException {
        try (Connection con = DBConnection.getConnection()) {
            String number = generateAccountNumber(con);
            Account account = new Account(number, userId, type);
            accountDao.save(con, account);
            return account;
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public Account getUserAccount(long userId, long accountId) throws BankException {
        try (Connection con = DBConnection.getConnection()) {
            return findOwnedAccount(con, userId, accountId);
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public List<Transaction> getHistory(long userId, long accountId) throws BankException {
        try (Connection con = DBConnection.getConnection()) {
            findOwnedAccount(con, userId, accountId);
            return transactionDao.findByAccountId(con, accountId);
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    public void closeAccount(long userId, long accountId) throws BankException {
        try (Connection con = DBConnection.getConnection()) {
            Account account = findOwnedAccount(con, userId, accountId);
            if (!account.isActive()) {
                throw new BankException("Account is already closed");
            }
            if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new BankException("Withdraw or transfer the balance before closing the account");
            }
            accountDao.updateStatus(con, accountId, AccountStatus.CLOSED);
        } catch (SQLException e) {
            throw new BankException(ERROR);
        }
    }

    private Account findOwnedAccount(Connection con, long userId, long accountId) throws SQLException, BankException {
        Account account = accountDao.findById(con, accountId);
        if (account == null || account.getUserId() != userId) {
            throw new BankException("Account not found");
        }
        return account;
    }

    private String generateAccountNumber(Connection con) throws SQLException {
        String number;
        do {
            number = "10" + String.format("%08d", random.nextInt(100_000_000));
        } while (accountDao.findByNumber(con, number) != null);
        return number;
    }
}
