package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.services.QuizService;
import java.io.IOException;

@WebServlet("/quizzes")
public class QuizzesServlet extends HttpServlet {
    private QuizService quizService = new QuizService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String category = req.getParameter("category");
            if (category != null && !category.isEmpty()) {
                quizService.fillQuizAttributes(req, category);
            } else {
                quizService.fillQuizAttributes(req);
            }
            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/quizzes.ftlh").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error loading quizzes: " + e.getMessage());
            req.getRequestDispatcher("/quizzes.ftlh").forward(req, resp);
        }
    }
}