package io.github.gerritsmith.financeapp.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class User extends AbstractEntity {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    // Constructors
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return username;
    }

}
