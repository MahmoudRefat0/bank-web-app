package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.User;
import com.mahmoud.bank.service.AccountService;
import com.mahmoud.bank.service.BankException;
import com.mahmoud.bank.service.TransactionService;
import com.mahmoud.bank.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/deposit")
public class DepositServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = RequestUtil.currentUser(req);
        try {
            req.setAttribute("accounts", accountService.getUserAccounts(user.getId()));
        } catch (BankException e) {
            req.setAttribute("error", e.getMessage());
        }
        req.setAttribute("selectedId", RequestUtil.parseId(req.getParameter("accountId")));
        req.getRequestDispatcher("/WEB-INF/views/deposit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = RequestUtil.currentUser(req);
        long accountId = RequestUtil.parseId(req.getParameter("accountId"));
        BigDecimal amount = RequestUtil.parseAmount(req.getParameter("amount"));

        try {
            transactionService.deposit(user.getId(), accountId, amount);
            RequestUtil.flash(req, "success", "Deposit completed successfully");
            resp.sendRedirect(req.getContextPath() + "/accounts/view?id=" + accountId);
        } catch (BankException e) {
            RequestUtil.flash(req, "error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/deposit?accountId=" + accountId);
        }
    }
}
