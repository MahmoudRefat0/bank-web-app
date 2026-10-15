package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.Account;
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
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = RequestUtil.currentUser(req);

        try {
            List<Account> accounts = accountService.getUserAccounts(user.getId());
            BigDecimal total = BigDecimal.ZERO;
            for (Account account : accounts) {
                if (account.isActive()) {
                    total = total.add(account.getBalance());
                }
            }
            req.setAttribute("accounts", accounts);
            req.setAttribute("totalBalance", total);
        } catch (BankException e) {
            req.setAttribute("error", e.getMessage());
        }

        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}
