package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.dis403.sw_quiz.models.QuizResult;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.services.QuizService;
import ru.itis.dis403.sw_quiz.services.UserService;
import java.io.IOException;
import java.util.List;

@WebServlet("/quiz/results")
public class QuizResultsServlet extends HttpServlet {
    private QuizService quizService = new QuizService();
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = userService.getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            HttpSession session = req.getSession();
            QuizResult latestResult = (QuizResult) session.getAttribute("latestQuizResult");

            if (latestResult != null) {
                System.out.println("Showing latest result for quiz: " + latestResult.getQuizId());
                req.setAttribute("quiz", quizService.getQuizById(latestResult.getQuizId()));
                req.setAttribute("result", latestResult);
                req.setAttribute("score", latestResult.getScore());
                req.setAttribute("correctAnswers", latestResult.getCorrectAnswers());
                req.setAttribute("totalQuestions", latestResult.getTotalQuestions());
                req.setAttribute("timeSpent", latestResult.getTimeSpent());

                session.removeAttribute("latestQuizResult");
            } else {
                System.out.println("No latest result found in session");
                req.setAttribute("error", "Нет данных о последней викторине. Пройдите викторину сначала.");
            }

            req.setAttribute("contextPath", req.getContextPath());
            req.getRequestDispatcher("/quiz-results.ftlh").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Не удалось загрузить результаты: " + e.getMessage());
            req.getRequestDispatcher("/quiz-results.ftlh").forward(req, resp);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = userService.getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int quizId = Integer.parseInt(req.getParameter("quizId"));
            int timeSpent = Integer.parseInt(req.getParameter("timeSpent"));

            System.out.println("Submitting quiz: " + quizId + ", time: " + timeSpent + "s, user: " + user.getId());

            QuizResult result = quizService.submitQuizFromForm(
                    user.getId(),
                    quizId,
                    req.getParameterMap(),
                    timeSpent
            );

            System.out.println("Quiz submitted successfully. Score: " + result.getScore());

            HttpSession session = req.getSession();
            session.setAttribute("latestQuizResult", result);

            resp.sendRedirect(req.getContextPath() + "/quiz/results");

        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = req.getSession();
            session.setAttribute("error", "Ошибка при обработке результатов: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/quiz/results");
        }
    }
}