package com.mahmoud.bank.controller;

import com.mahmoud.bank.model.User;
import com.mahmoud.bank.service.AuthService;
import com.mahmoud.bank.service.BankException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final String VIEW = "/WEB-INF/views/login.jsp";

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(VIEW).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = authService.login(email, password);
            user.setPasswordHash(null);

            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            req.getSession(true).setAttribute("user", user);

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (BankException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("email", email);
            req.getRequestDispatcher(VIEW).forward(req, resp);
        }
    }
}
