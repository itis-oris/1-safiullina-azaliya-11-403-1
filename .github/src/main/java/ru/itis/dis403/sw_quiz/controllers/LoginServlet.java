package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.services.UserService;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.ftlh").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            if (userService.authenticateUser(username, password, req)) {
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                req.setAttribute("error", "Invalid username or password");
                req.getRequestDispatcher("/login.ftlh").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Login error: " + e.getMessage());
            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/login.ftlh").forward(req, resp);
        }
    }
}