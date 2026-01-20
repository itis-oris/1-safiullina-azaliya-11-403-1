package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.models.QuizResult;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.services.QuizService;
import ru.itis.dis403.sw_quiz.services.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class UserProfileServlet extends HttpServlet {

    private UserService userService = new UserService();
    private QuizService quizService = new QuizService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = userService.getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            List<QuizResult> userResults = quizService.getUserResults(user.getId());
            req.setAttribute("userResults", userResults);
            req.setAttribute("user", user);
            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/profile.ftlh").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading profile: " + e.getMessage());
            req.getRequestDispatcher("/profile.ftlh").forward(req, resp);
        }
    }
}