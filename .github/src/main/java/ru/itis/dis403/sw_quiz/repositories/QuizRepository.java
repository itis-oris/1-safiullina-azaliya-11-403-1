package ru.itis.dis403.sw_quiz.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itis.dis403.sw_quiz.models.Quiz;
import ru.itis.dis403.sw_quiz.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    final static Logger logger = LogManager.getLogger(QuizRepository.class);

    public List<Quiz> findAllApproved() throws Exception {
        List<Quiz> quizzes = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, description, category, difficulty, time_limit, approved, created_by, status " +
                        "FROM quizzes WHERE status = 'APPROVED'");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quiz quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));
            quizzes.add(quiz);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} approved quizzes", quizzes.size());
        return quizzes;
    }

    public List<Quiz> findPendingApproval() throws Exception {
        List<Quiz> quizzes = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, description, category, difficulty, time_limit, approved, created_by, status " +
                        "FROM quizzes WHERE status = 'PENDING'");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quiz quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));
            quizzes.add(quiz);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} quizzes pending approval", quizzes.size());
        return quizzes;
    }

    public Quiz findById(int id) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, description, category, difficulty, time_limit, approved, created_by, status " +
                        "FROM quizzes WHERE id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();

        Quiz quiz = null;
        if (resultSet.next()) {
            quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Quiz found by ID: {}", id);
        return quiz;
    }

    public List<Quiz> findAllWithCreators() throws Exception {
        List<Quiz> quizzes = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT q.id, q.title, q.description, q.category, q.difficulty, " +
                        "q.time_limit, q.approved, q.created_by, q.status, u.username as creator_username " +
                        "FROM quizzes q " +
                        "LEFT JOIN users u ON q.created_by = u.id " +
                        "ORDER BY q.status, q.id DESC");
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quiz quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));

            User creator = new User();
            creator.setId(resultSet.getInt("created_by"));
            creator.setUsername(resultSet.getString("creator_username"));
            quiz.setCreator(creator);

            quizzes.add(quiz);
        }

        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} quizzes with creators", quizzes.size());
        return quizzes;
    }

    public Quiz findByIdWithCreator(int quizId) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT q.id, q.title, q.description, q.category, q.difficulty, " +
                        "q.time_limit, q.approved, q.created_by, q.status, u.username as creator_username " +
                        "FROM quizzes q " +
                        "LEFT JOIN users u ON q.created_by = u.id " +
                        "WHERE q.id = ?");
        statement.setInt(1, quizId);
        ResultSet resultSet = statement.executeQuery();

        Quiz quiz = null;
        if (resultSet.next()) {
            quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));

            User creator = new User();
            creator.setId(resultSet.getInt("created_by"));
            creator.setUsername(resultSet.getString("creator_username"));
            quiz.setCreator(creator);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return quiz;
    }

    public Integer save(Quiz quiz) throws Exception {
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO quizzes (title, description, category, difficulty, time_limit, approved, created_by, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, quiz.getTitle());
        statement.setString(2, quiz.getDescription());
        statement.setString(3, quiz.getCategory());
        statement.setInt(4, quiz.getDifficulty());
        statement.setInt(5, quiz.getTimeLimit());
        statement.setBoolean(6, quiz.getApproved());
        statement.setInt(7, quiz.getCreatedBy());
        statement.setString(8, quiz.getStatus() != null ? quiz.getStatus() : "PENDING");

        statement.executeUpdate();
        statement.close();

        PreparedStatement idStatement = connection.prepareStatement("SELECT lastval()");
        ResultSet resultSet = idStatement.executeQuery();

        Integer generatedId = null;
        if (resultSet.next()) {
            generatedId = resultSet.getInt(1);
        }

        resultSet.close();
        idStatement.close();
        connection.commit();
        connection.close();

        logger.info("Quiz saved successfully: {} with ID: {}", quiz.getTitle(), generatedId);
        return generatedId;
    }

    public void updateApprovalStatus(int quizId, boolean approved) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE quizzes SET approved = ? WHERE id = ?");
        statement.setBoolean(1, approved);
        statement.setInt(2, quizId);

        int countRow = statement.executeUpdate();
        statement.close();
        connection.close();

        logger.info("Quiz approval status updated for ID: {}, approved: {}", quizId, approved);
    }

    public void updateStatus(int quizId, String status) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE quizzes SET status = ? WHERE id = ?");
        statement.setString(1, status);
        statement.setInt(2, quizId);

        statement.executeUpdate();
        statement.close();
        connection.close();

        logger.info("Quiz status updated for ID: {}, status: {}", quizId, status);
    }

    public List<Quiz> findByCategory(String category) throws Exception {
        List<Quiz> quizzes = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, title, description, category, difficulty, time_limit, approved, created_by, status " +
                        "FROM quizzes WHERE category = ? AND status = 'APPROVED'");
        statement.setString(1, category);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Quiz quiz = new Quiz(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("category"),
                    resultSet.getInt("difficulty"),
                    resultSet.getInt("time_limit"),
                    resultSet.getBoolean("approved"),
                    resultSet.getInt("created_by")
            );
            quiz.setStatus(resultSet.getString("status"));
            quizzes.add(quiz);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} quizzes in category: {}", quizzes.size(), category);
        return quizzes;
    }

    public void deleteQuiz(int quizId) throws Exception {
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);

        try {
            PreparedStatement deleteResults = connection.prepareStatement(
                    "DELETE FROM quiz_results WHERE quiz_id = ?");
            deleteResults.setInt(1, quizId);
            deleteResults.executeUpdate();
            deleteResults.close();

            PreparedStatement deleteQuestions = connection.prepareStatement(
                    "DELETE FROM questions WHERE quiz_id = ?");
            deleteQuestions.setInt(1, quizId);
            deleteQuestions.executeUpdate();
            deleteQuestions.close();

            PreparedStatement deleteQuiz = connection.prepareStatement(
                    "DELETE FROM quizzes WHERE id = ?");
            deleteQuiz.setInt(1, quizId);
            int affectedRows = deleteQuiz.executeUpdate();
            deleteQuiz.close();

            connection.commit();

            if (affectedRows > 0) {
                logger.info("Quiz deleted successfully, ID: {}", quizId);
            } else {
                logger.warn("No quiz found with ID: {}", quizId);
            }

        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }
    }

}