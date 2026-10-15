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

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {

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
        req.getRequestDispatcher("/WEB-INF/views/transfer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = RequestUtil.currentUser(req);
        long fromAccountId = RequestUtil.parseId(req.getParameter("fromAccountId"));
        String toAccountNumber = req.getParameter("toAccountNumber");
        BigDecimal amount = RequestUtil.parseAmount(req.getParameter("amount"));

        try {
            transactionService.transfer(user.getId(), fromAccountId, toAccountNumber, amount);
            RequestUtil.flash(req, "success", "Transfer completed successfully");
            resp.sendRedirect(req.getContextPath() + "/accounts/view?id=" + fromAccountId);
        } catch (BankException e) {
            RequestUtil.flash(req, "error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/transfer?accountId=" + fromAccountId);
        }
    }
}
