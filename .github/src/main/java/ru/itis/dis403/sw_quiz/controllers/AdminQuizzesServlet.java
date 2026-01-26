package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itis.dis403.sw_quiz.services.QuizService;

import java.io.IOException;

@WebServlet("/admin/admin-quizzes")
public class AdminQuizzesServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(AdminQuizzesServlet.class);
    private QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("approvedQuizzes", quizService.getApprovedQuizzesWithCreators());
            req.setAttribute("pendingQuizzes", quizService.getPendingQuizzesWithCreators());
            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/admin/admin-quizzes.ftlh").forward(req, resp);
        } catch (Exception e) {
            logger.atError().withThrowable(e).log();
            req.setAttribute("error", "Error loading quizzes: " + e.getMessage());
            req.getRequestDispatcher("/admin/admin-quizzes.ftlh").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String action = req.getParameter("action");
            String quizIdParam = req.getParameter("quizId");

            if (quizIdParam == null || action == null) {
                throw new IllegalArgumentException("Неверные параметры запроса");
            }

            int quizId = Integer.parseInt(quizIdParam);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            String message;
            switch (action) {
                case "delete":
                    quizService.deleteQuiz(quizId);
                    message = "Викторина успешно удалена!";
                    break;
                case "reject":
                    quizService.rejectQuiz(quizId);
                    message = "Викторина отклонена!";
                    break;
                case "approve":
                    quizService.approveQuiz(quizId);
                    message = "Викторина одобрена!";
                    break;
                default:
                    throw new IllegalArgumentException("Неизвестное действие: " + action);
            }

            resp.getWriter().write("{\"success\":true,\"message\":\"" + message + "\"}");

        } catch (Exception e) {
            logger.atError().withThrowable(e).log();
            resp.getWriter().write("{\"success\":false,\"message\":\"Ошибка: " +
                    e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }
}