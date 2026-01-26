package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.services.QuizService;

import java.io.IOException;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
    private QuizService quizService = new QuizService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            quizService.fillQuizAttributes(req);
            req.setAttribute("contextPath", req.getContextPath());

            User user = (User) req.getSession().getAttribute("user");
            if (user != null && "ADMIN".equals(user.getRole())) {
                req.setAttribute("isAdmin", true);
            }

            req.getRequestDispatcher("/index.ftlh").forward(req, resp);

        } catch (Exception e) {
            req.setAttribute("error", "Error loading quizzes: " + e.getMessage());
            req.getRequestDispatcher("/index.ftlh").forward(req, resp);
        }
    }
}