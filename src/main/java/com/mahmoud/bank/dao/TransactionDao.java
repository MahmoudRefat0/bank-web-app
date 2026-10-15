package com.mahmoud.bank.dao;

import com.mahmoud.bank.model.Transaction;
import com.mahmoud.bank.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public void save(Connection con, Transaction tx) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, tx_type, amount, balance_after, related_account) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, tx.getAccountId());
            ps.setString(2, tx.getType().name());
            ps.setBigDecimal(3, tx.getAmount());
            ps.setBigDecimal(4, tx.getBalanceAfter());
            ps.setString(5, tx.getRelatedAccount());
            ps.executeUpdate();
        }
    }

    public List<Transaction> findByAccountId(Connection con, long accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC, id DESC";
        List<Transaction> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        Transaction tx = new Transaction();
        tx.setId(rs.getLong("id"));
        tx.setAccountId(rs.getLong("account_id"));
        tx.setType(TransactionType.valueOf(rs.getString("tx_type")));
        tx.setAmount(rs.getBigDecimal("amount"));
        tx.setBalanceAfter(rs.getBigDecimal("balance_after"));
        tx.setRelatedAccount(rs.getString("related_account"));
        tx.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return tx;
    }
}
