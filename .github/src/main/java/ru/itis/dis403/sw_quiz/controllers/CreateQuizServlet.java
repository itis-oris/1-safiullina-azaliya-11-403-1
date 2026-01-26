package ru.itis.dis403.sw_quiz.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.sw_quiz.models.Quiz;
import ru.itis.dis403.sw_quiz.models.Question;
import ru.itis.dis403.sw_quiz.models.User;
import ru.itis.dis403.sw_quiz.repositories.QuizRepository;
import ru.itis.dis403.sw_quiz.repositories.QuestionRepository;
import ru.itis.dis403.sw_quiz.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/create-quiz")
public class CreateQuizServlet extends HttpServlet {
    private QuizRepository quizRepository = new QuizRepository();
    private QuestionRepository questionRepository = new QuestionRepository();
    private UserService userService = new UserService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = userService.getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("contextPath", req.getContextPath());
        req.getRequestDispatcher("/create-quiz.ftlh").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = userService.getSessionUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String category = req.getParameter("category");
            String difficultyParam = req.getParameter("difficulty");
            String timeLimitParam = req.getParameter("timeLimit");

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Название викторины обязательно");
            }
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Категория обязательна");
            }

            Quiz quiz = new Quiz();
            quiz.setTitle(title.trim());
            quiz.setDescription(description != null ? description.trim() : "");
            quiz.setCategory(category.trim());

            quiz.setDifficulty(parseIntSafe(difficultyParam, 1));
            quiz.setTimeLimit(parseIntSafe(timeLimitParam, 5));
            if ("ADMIN".equals(user.getRole())) {
                quiz.setApproved(true);
                quiz.setStatus("APPROVED");
                req.setAttribute("message", "Викторина создана и опубликована!");
            } else {
                quiz.setApproved(false);
                quiz.setStatus("PENDING");
                req.setAttribute("message", "Викторина создана! Она будет опубликована после модерации.");
            }

            quiz.setCreatedBy(user.getId());

            Integer quizId = quizRepository.save(quiz);

            if (quizId == null) {
                throw new Exception("Не удалось сохранить викторину в базу данных");
            }

            String[] questionTexts = req.getParameterValues("questionText");
            String[] option1s = req.getParameterValues("option1");
            String[] option2s = req.getParameterValues("option2");
            String[] option3s = req.getParameterValues("option3");
            String[] option4s = req.getParameterValues("option4");

            List<Question> questions = new ArrayList<>();
            if (questionTexts != null) {
                for (int i = 0; i < questionTexts.length; i++) {
                    if (questionTexts[i] != null && !questionTexts[i].trim().isEmpty()) {
                        Question question = new Question();
                        question.setQuizId(quizId);
                        question.setQuestionText(questionTexts[i].trim());

                        question.setOption1(option1s != null && i < option1s.length ?
                                safeGet(option1s[i]) : "");
                        question.setOption2(option2s != null && i < option2s.length ?
                                safeGet(option2s[i]) : "");
                        question.setOption3(option3s != null && i < option3s.length ?
                                safeGet(option3s[i]) : "");
                        question.setOption4(option4s != null && i < option4s.length ?
                                safeGet(option4s[i]) : "");

                        String correctAnswerParam = req.getParameter("correctAnswer_" + (i + 1));
                        int correctAnswer = parseIntSafe(correctAnswerParam, 1);
                        question.setCorrectAnswer(correctAnswer);

                        question.setPoints(10);
                        question.setQuestionOrder(i);

                        questions.add(question);
                    }
                }

                if (!questions.isEmpty()) {
                    questionRepository.saveAll(questions);
                }
            }

        } catch (Exception e) {
            req.setAttribute("error", "Ошибка создания викторины: " + e.getMessage());
            e.printStackTrace();
        }

        req.setAttribute("contextPath", req.getContextPath());
        req.getRequestDispatcher("/create-quiz.ftlh").forward(req, resp);
    }

    private int parseIntSafe(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String safeGet(String value) {
        return value != null ? value.trim() : "";
    }
}