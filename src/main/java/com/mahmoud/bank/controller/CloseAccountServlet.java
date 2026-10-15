package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.User;
import com.mahmoud.bank.service.AccountService;
import com.mahmoud.bank.service.BankException;
import com.mahmoud.bank.util.RequestUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/accounts/close")
public class CloseAccountServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = RequestUtil.currentUser(req);
        long accountId = RequestUtil.parseId(req.getParameter("id"));

        try {
            accountService.closeAccount(user.getId(), accountId);
            RequestUtil.flash(req, "success", "Account closed");
        } catch (BankException e) {
            RequestUtil.flash(req, "error", e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/accounts/view?id=" + accountId);
    }
}
