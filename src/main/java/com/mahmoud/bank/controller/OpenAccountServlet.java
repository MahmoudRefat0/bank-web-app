package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.Account;
import com.mahmoud.bank.model.AccountType;
import com.mahmoud.bank.model.User;
import com.mahmoud.bank.service.AccountService;
import com.mahmoud.bank.service.BankException;
import com.mahmoud.bank.util.RequestUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/accounts/open")
public class OpenAccountServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = RequestUtil.currentUser(req);

        try {
            AccountType type = AccountType.valueOf(req.getParameter("accountType"));
            Account account = accountService.openAccount(user.getId(), type);
            RequestUtil.flash(req, "success", "Account " + account.getAccountNumber() + " opened successfully");
        } catch (IllegalArgumentException | NullPointerException e) {
            RequestUtil.flash(req, "error", "Please choose a valid account type");
        } catch (BankException e) {
            RequestUtil.flash(req, "error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }
}
