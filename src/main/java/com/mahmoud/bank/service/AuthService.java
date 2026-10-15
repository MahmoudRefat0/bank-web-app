package com.mahmoud.bank.service;

import com.mahmoud.bank.dao.UserDao;
import com.mahmoud.bank.model.User;
import com.mahmoud.bank.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthService {

    private final UserDao userDao = new UserDao();

    public User register(String fullName, String email, String password) throws BankException {
        if (isBlank(fullName) || isBlank(email) || isBlank(password)) {
            throw new BankException("All fields are required");
        }
        if (password.length() < 6) {
            throw new BankException("Password must be at least 6 characters");
        }
        String cleanEmail = email.trim().toLowerCase();

        try (Connection con = DBConnection.getConnection()) {
            if (userDao.findByEmail(con, cleanEmail) != null) {
                throw new BankException("Email is already registered");
            }
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            User user = new User(fullName.trim(), cleanEmail, hash);
            userDao.save(con, user);
            return user;
        } catch (SQLException e) {
            throw new BankException("Something went wrong, please try again");
        }
    }

    public User login(String email, String password) throws BankException {
        if (isBlank(email) || isBlank(password)) {
            throw new BankException("Email and password are required");
        }

        try (Connection con = DBConnection.getConnection()) {
            User user = userDao.findByEmail(con, email.trim().toLowerCase());
            if (user == null || !BCrypt.checkpw(password, user.getPasswordHash())) {
                throw new BankException("Invalid email or password");
            }
            return user;
        } catch (SQLException e) {
            throw new BankException("Something went wrong, please try again");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
