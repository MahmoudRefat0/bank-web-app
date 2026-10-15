package com.mahmoud.bank.dao;

import com.mahmoud.bank.model.Account;
import com.mahmoud.bank.model.AccountStatus;
import com.mahmoud.bank.model.AccountType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public void save(Connection con, Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, user_id, account_type, balance, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, new String[]{"ID"})) {
            ps.setString(1, account.getAccountNumber());
            ps.setLong(2, account.getUserId());
            ps.setString(3, account.getAccountType().name());
            ps.setBigDecimal(4, account.getBalance());
            ps.setString(5, account.getStatus().name());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    account.setId(keys.getLong(1));
                }
            }
        }
    }

    public List<Account> findByUserId(Connection con, long userId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE user_id = ? ORDER BY created_at";
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapRow(rs));
                }
            }
        }
        return accounts;
    }

    public Account findById(Connection con, long id) throws SQLException {
        return findOne(con, "SELECT * FROM accounts WHERE id = ?", id);
    }

    public Account findByIdForUpdate(Connection con, long id) throws SQLException {
        return findOne(con, "SELECT * FROM accounts WHERE id = ? FOR UPDATE", id);
    }

    public Account findByNumber(Connection con, String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void updateBalance(Connection con, long id, BigDecimal balance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, balance);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    public void updateStatus(Connection con, long id, AccountStatus status) throws SQLException {
        String sql = "UPDATE accounts SET status = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }

    private Account findOne(Connection con, String sql, long id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setUserId(rs.getLong("user_id"));
        account.setAccountType(AccountType.valueOf(rs.getString("account_type")));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return account;
    }
}
