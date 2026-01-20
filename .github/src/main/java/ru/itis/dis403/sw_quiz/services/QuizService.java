package ru.itis.dis403.sw_quiz.services;

import jakarta.servlet.http.HttpServletRequest;
import ru.itis.dis403.sw_quiz.models.*;
import ru.itis.dis403.sw_quiz.repositories.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class QuizService {
    private QuizRepository quizRepository = new QuizRepository();
    private QuestionRepository questionRepository = new QuestionRepository();
    private QuizResultRepository resultRepository = new QuizResultRepository();
    private UserRepository userRepository = new UserRepository();
    private UserService userService = new UserService();

    public void fillQuizAttributes(HttpServletRequest request) throws Exception {
        List<Quiz> quizzes = quizRepository.findAllApproved();
        request.setAttribute("quizzes", quizzes);
    }

    public void fillQuizAttributes(HttpServletRequest request, String category) throws Exception {
        List<Quiz> quizzes = quizRepository.findByCategory(category);
        request.setAttribute("quizzes", quizzes);
        request.setAttribute("category", category);
    }

    public List<Question> getQuizQuestions(int quizId) throws Exception {
        return questionRepository.findByQuizId(quizId);
    }

    public Quiz getQuizById(int quizId) throws Exception {
        return quizRepository.findById(quizId);
    }

    public Quiz getQuizWithCreatorAndQuestions(int quizId) throws Exception {
        Quiz quiz = quizRepository.findByIdWithCreator(quizId);
        if (quiz != null) {
            List<Question> questions = questionRepository.findByQuizId(quizId);
            quiz.setQuestions(questions);
        }
        return quiz;
    }

    public QuizResult submitQuizFromForm(int userId, int quizId, Map<String, String[]> parameters, int timeSpent) throws Exception {
        List<Question> questions = getQuizQuestions(quizId);
        int correctAnswers = 0;
        int totalScore = 0;
        List<Integer> userAnswers = new ArrayList<>();

        for (Question question : questions) {
            String[] answers = parameters.get("question_" + question.getId());
            int userAnswer = 0;
            if (answers != null && answers.length > 0) {
                try {
                    userAnswer = Integer.parseInt(answers[0]);
                } catch (NumberFormatException e) {
                    userAnswer = 0;
                }
            }
            userAnswers.add(userAnswer);

            if (userAnswer == question.getCorrectAnswer()) {
                correctAnswers++;
                totalScore += question.getPoints();
            }
        }

        QuizResult result = new QuizResult(
                null, userId, quizId, totalScore,
                questions.size(), correctAnswers, timeSpent
        );

        resultRepository.save(result);
        userRepository.updateUserScore(userId, totalScore);

        return result;
    }

    public List<QuizResult> getUserResults(int userId) throws Exception {
        return resultRepository.findByUserId(userId);
    }

    public List<Quiz> getPendingQuizzes() throws Exception {
        return quizRepository.findPendingApproval();
    }

    public List<Quiz> getPendingQuizzesWithCreators() throws Exception {
        List<Quiz> allQuizzes = quizRepository.findAllWithCreators();
        return allQuizzes.stream()
                .filter(quiz -> "PENDING".equals(quiz.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Quiz> getApprovedQuizzesWithCreators() throws Exception {
        List<Quiz> allQuizzes = quizRepository.findAllWithCreators();
        return allQuizzes.stream()
                .filter(quiz -> "APPROVED".equals(quiz.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Quiz> getRejectedQuizzesWithCreators() throws Exception {
        List<Quiz> allQuizzes = quizRepository.findAllWithCreators();
        return allQuizzes.stream()
                .filter(quiz -> "REJECTED".equals(quiz.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Quiz> getAllQuizzesWithCreators() throws Exception {
        return quizRepository.findAllWithCreators();
    }

    public void rejectQuiz(int quizId) throws Exception {
        quizRepository.updateStatus(quizId, "REJECTED");
        quizRepository.updateApprovalStatus(quizId, false);
    }

    public void approveQuiz(int quizId) throws Exception {
        quizRepository.updateStatus(quizId, "APPROVED");
        quizRepository.updateApprovalStatus(quizId, true);
    }

    public void deleteQuiz(int quizId) throws Exception {
        quizRepository.deleteQuiz(quizId);
    }

    public List<Quiz> getAllApprovedQuizzes() throws Exception {
        return quizRepository.findAllApproved();
    }
}