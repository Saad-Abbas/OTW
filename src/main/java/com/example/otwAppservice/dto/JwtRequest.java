package com.example.otwAppservice.dto;

public class JwtRequest {

    private String username;
    private String password;

    // Constructor, getters, and setters
    // You can generate these using your IDE or manually implement them

    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}