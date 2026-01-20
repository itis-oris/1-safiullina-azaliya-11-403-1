package ru.itis.dis403.sw_quiz.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itis.dis403.sw_quiz.models.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    final static Logger logger = LogManager.getLogger(QuestionRepository.class);

    public List<Question> findByQuizId(int quizId) throws Exception {
        List<Question> questions = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("select id, quiz_id, question_text, option1, option2, option3, option4, correct_answer, points, question_order from questions where quiz_id = ? order by question_order");
        statement.setInt(1, quizId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Question question = new Question(
                    resultSet.getInt("id"),
                    resultSet.getInt("quiz_id"),
                    resultSet.getString("question_text"),
                    resultSet.getString("option1"),
                    resultSet.getString("option2"),
                    resultSet.getString("option3"),
                    resultSet.getString("option4"),
                    resultSet.getInt("correct_answer"),
                    resultSet.getInt("points"),
                    resultSet.getInt("question_order")
            );
            questions.add(question);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} questions for quiz ID: {}", questions.size(), quizId);
        return questions;
    }

    public void saveAll(List<Question> questions) throws Exception {
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);

        for (Question question : questions) {
            PreparedStatement statement = connection.prepareStatement(
                    "insert into questions (quiz_id, question_text, option1, option2, option3, option4, correct_answer, points, question_order) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            statement.setInt(1, question.getQuizId());
            statement.setString(2, question.getQuestionText());
            statement.setString(3, question.getOption1());
            statement.setString(4, question.getOption2());
            statement.setString(5, question.getOption3());
            statement.setString(6, question.getOption4());
            statement.setInt(7, question.getCorrectAnswer());
            statement.setInt(8, question.getPoints());
            statement.setInt(9, question.getQuestionOrder());

            statement.executeUpdate();
            statement.close();
        }

        connection.commit();
        connection.close();

        logger.info("Saved {} questions successfully", questions.size());
    }
}
