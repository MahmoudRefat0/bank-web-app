package com.mahmoud.bank.controller;

import com.mahmoud.bank.service.AuthService;
import com.mahmoud.bank.service.BankException;
import com.mahmoud.bank.util.RequestUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/register.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");

        try {
            if (password == null || !password.equals(confirm)) {
                throw new BankException("Passwords do not match");
            }
            authService.register(fullName, email, password);
            RequestUtil.flash(req, "success", "Registration successful, please login");
            resp.sendRedirect(req.getContextPath() + "/login");
        } catch (BankException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("fullName", fullName);
            req.setAttribute("email", email);
            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }
}
