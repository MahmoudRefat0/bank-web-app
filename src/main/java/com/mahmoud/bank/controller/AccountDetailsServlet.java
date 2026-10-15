package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.User;
import com.mahmoud.bank.service.AccountService;
import com.mahmoud.bank.service.BankException;
import com.mahmoud.bank.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/accounts/view")
public class AccountDetailsServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = RequestUtil.currentUser(req);
        long accountId = RequestUtil.parseId(req.getParameter("id"));

        try {
            req.setAttribute("account", accountService.getUserAccount(user.getId(), accountId));
            req.setAttribute("transactions", accountService.getHistory(user.getId(), accountId));
            req.getRequestDispatcher("/WEB-INF/views/account.jsp").forward(req, resp);
        } catch (BankException e) {
            RequestUtil.flash(req, "error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }
}
