package ru.itis.dis403.sw_quiz.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.itis.dis403.sw_quiz.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    final static Logger logger = LogManager.getLogger(UserRepository.class);

    public void addUser(User user) throws Exception {
        Connection connection = DbConnection.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement statement = connection.prepareStatement(
                "insert into users (username, password, score, role) values (?, ?, ?, ?)");
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getHashPassword());
        statement.setInt(3, user.getScore());
        statement.setString(4, user.getRole());

        int countRow = statement.executeUpdate();
        statement.close();

        connection.commit();
        connection.close();

        logger.info("User added successfully: {}", user.getUsername());
    }

    public User findByUsername(String username) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("select id, username, password, score, role from users where username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        User user = null;
        if (resultSet.next()) {
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"),
                    resultSet.getString("role")
            );
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("User found by username: {}", username);
        return user;
    }

    public void updateUserScore(Integer userId, Integer additionalScore) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("update users set score = score + ? where id = ?");
        statement.setInt(1, additionalScore);
        statement.setInt(2, userId);

        int countRow = statement.executeUpdate();
        statement.close();
        connection.close();

        logger.debug("Score updated for user ID: {}, additional: {}", userId, additionalScore);
    }

    public boolean userExists(String username) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("select count(*) as count from users where username = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        boolean exists = false;
        if (resultSet.next()) {
            exists = resultSet.getInt("count") > 0;
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("User exists check for {}: {}", username, exists);
        return exists;
    }

    public List<User> getTopUsers(int limit) throws Exception {
        List<User> users = new ArrayList<>();
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement =
                connection.prepareStatement("select id, username, password, score, role from users order by score desc limit ?");
        statement.setInt(1, limit);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"),
                    resultSet.getString("role")
            );
            users.add(user);
        }
        resultSet.close();
        statement.close();
        connection.close();

        logger.debug("Found {} top users", users.size());
        return users;
    }

    public User findById(int userId) throws Exception {
        Connection connection = DbConnection.getConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT id, username, score, role FROM users WHERE id = ?");
        statement.setInt(1, userId);
        ResultSet resultSet = statement.executeQuery();

        User user = null;
        if (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setScore(resultSet.getInt("score"));
            user.setRole(resultSet.getString("role"));
        }

        resultSet.close();
        statement.close();
        connection.close();

        return user;
    }
}