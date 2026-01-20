package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.models.Quiz;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.services.QuizService;
import java.io.IOException;

@WebServlet("/quiz/*")
public class QuizServlet extends HttpServlet {
    private QuizService quizService = new QuizService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String pathInfo = req.getPathInfo();
            Integer quizId = null;
            boolean isAdminView = false;

            if (pathInfo != null && pathInfo.length() > 1) {
                if (pathInfo.startsWith("/admin/")) {
                    isAdminView = true;
                    try {
                        quizId = Integer.parseInt(pathInfo.substring(7));
                    } catch (NumberFormatException e) {
                        // Игнорируем
                    }
                } else {
                    try {
                        quizId = Integer.parseInt(pathInfo.substring(1));
                    } catch (NumberFormatException e) {
                        // Игнорируем
                    }
                }
            }

            if (quizId == null) {
                String idParam = req.getParameter("id");
                if (idParam != null) {
                    try {
                        quizId = Integer.parseInt(idParam);
                    } catch (NumberFormatException e) {
                        resp.sendError(400, "Неверный ID викторины");
                        return;
                    }
                }
            }

            if (quizId != null) {
                Quiz quiz = quizService.getQuizWithCreatorAndQuestions(quizId);

                if (quiz != null) {
                    User user = (User) req.getSession().getAttribute("user");

                    if (isAdminView) {
                        if (user != null && "ADMIN".equals(user.getRole())) {
                            req.setAttribute("quiz", quiz);
                            req.setAttribute("questions", quiz.getQuestions());
                            req.setAttribute("contextPath", req.getContextPath());
                            req.getRequestDispatcher("/admin/admin-quiz-view.ftlh").forward(req, resp);
                        } else {
                            resp.sendError(403, "Доступ запрещен. Требуются права администратора.");
                        }
                    } else {
                        req.setAttribute("quiz", quiz);
                        req.setAttribute("questions", quiz.getQuestions());
                        req.setAttribute("contextPath", req.getContextPath());
                        req.getRequestDispatcher("/quiz.ftlh").forward(req, resp);
                    }
                } else {
                    resp.sendError(404, "Викторина не найдена");
                }
            } else {
                resp.sendError(400, "ID викторины не указан");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Ошибка загрузки викторины: " + e.getMessage());
            req.getRequestDispatcher("/quiz.ftlh").forward(req, resp);
        }
    }
}