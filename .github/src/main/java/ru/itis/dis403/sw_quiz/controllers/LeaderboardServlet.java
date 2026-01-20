package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.services.UserService;
import java.io.IOException;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("topUsers", userService.getTopUsers(20));
            req.getRequestDispatcher("/leaderboard.ftlh").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading leaderboard: " + e.getMessage());
            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/leaderboard.ftlh").forward(req, resp);
        }
    }
}