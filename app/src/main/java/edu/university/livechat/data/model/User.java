package edu.university.livechat.data.model;

import androidx.annotation.NonNull;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

public class User {

    private String username;
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private String language;
    private boolean optIn;

    // constructor
    public User(String username, String name, String surname, String phoneNumber, String password, String language, boolean optIn) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.language = language;
        this.optIn = optIn;
    }

    public User(String name) {
        this.name = name;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOptIn() {
        return optIn;
    }

    public void setOptIn(boolean optIn) {
        this.optIn = optIn;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", language='" + language + '\'' +
                ", optIn=" + optIn +
                '}';
    }
}