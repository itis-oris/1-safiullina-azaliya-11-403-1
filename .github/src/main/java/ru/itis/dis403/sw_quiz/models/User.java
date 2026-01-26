package ru.itis.dis403.sw_quiz.models;

public class User {
    private Integer id;
    private String username;
    private String hashPassword;
    private Integer score;
    private String role;

    public User() {}

    public User(Integer id, String username, String hashPassword, Integer score, String role) {
        this.id = id;
        this.username = username;
        this.hashPassword = hashPassword;
        this.score = score;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
