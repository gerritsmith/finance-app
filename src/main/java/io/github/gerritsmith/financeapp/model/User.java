package io.github.gerritsmith.financeapp.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User extends AbstractEntity {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @ElementCollection
    private Set<String> roles = new HashSet<>();

    // Constructors
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Methods
    public void addRole(String role) {
        roles.add(role);
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return username;
    }

}
