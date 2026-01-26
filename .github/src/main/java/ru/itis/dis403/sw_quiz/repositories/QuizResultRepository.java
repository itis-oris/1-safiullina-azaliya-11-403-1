package ru.itis.dis403.sw_quiz.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itis.dis403.sw_quiz.models.QuizResult;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizResultRepository {

    final static Logger logger = LogManager.getLogger(QuizResultRepository.class);

    public void save(QuizResult result) throws Exception {
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);

        String sql = "INSERT INTO quiz_results (user_id, quiz_id, score, total_questions, correct_answers, time_spent) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, result.getUserId());
        statement.setInt(2, result.getQuizId());
        statement.setInt(3, result.getScore());
        statement.setInt(4, result.getTotalQuestions());
        statement.setInt(5, result.getCorrectAnswers());
        statement.setInt(6, result.getTimeSpent());

        int countRow = statement.executeUpdate();
        statement.close();

        connection.commit();
        connection.close();

        logger.info("Quiz result saved for user ID: {}, quiz ID: {}, score: {}",
                result.getUserId(), result.getQuizId(), result.getScore());
    }

    public List<QuizResult> findByUserId(int userId) throws Exception {
        List<QuizResult> results = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("select id, user_id, quiz_id, score, total_questions, correct_answers, time_spent from quiz_results where user_id = ? order by id desc");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            QuizResult result = new QuizResult(
                    resultSet.getInt("id"),
                    resultSet.getInt("user_id"),
                    resultSet.getInt("quiz_id"),
                    resultSet.getInt("score"),
                    resultSet.getInt("total_questions"),
                    resultSet.getInt("correct_answers"),
                    resultSet.getInt("time_spent")
            );
            results.add(result);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} quiz results for user ID: {}", results.size(), userId);
        return results;
    }

}
