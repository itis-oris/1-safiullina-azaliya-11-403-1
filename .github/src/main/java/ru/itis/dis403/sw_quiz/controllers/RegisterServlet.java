package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.services.UserService;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("contextPath", req.getContextPath());
        req.getRequestDispatcher("/register.ftlh").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        try {
            if (!password.equals(confirmPassword)) {
                req.setAttribute("error", "Passwords do not match");
                req.getRequestDispatcher("/register.ftlh").forward(req, resp);
                return;
            }

            if (userService.registerUser(username, password, req)) {
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                req.setAttribute("error", "Username already exists");
                req.getRequestDispatcher("register.ftlh").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Registration error: " + e.getMessage());
            req.getRequestDispatcher("register.ftlh").forward(req, resp);
        }
    }
}