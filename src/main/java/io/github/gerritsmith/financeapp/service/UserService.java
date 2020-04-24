package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.UserRepository;
import io.github.gerritsmith.financeapp.exception.UserExistsException;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Constructors
    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        initializeUserDatabase();
    }

    // Read
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Create
    @Transactional
    public User saveUser(User user) throws UserExistsException {
        User userExists = findUserByUsername(user.getUsername());
        if (userExists != null) {
            throw new UserExistsException("The username " + user.getUsername() + " is already taken!");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.addRole("USER");
        return userRepository.save(user);
    }

    // TODO: Remove before deployment
    public void initializeUserDatabase() {
        Map<String, String> users = new HashMap<>();
        users.put("user", "pass");
        users.put("admin", "admin");
        Map<String, Set<String>> userRoles = new HashMap<>();
        userRoles.put("user", new HashSet<>(Arrays.asList("USER")));
        userRoles.put("admin", new HashSet<>(Arrays.asList("USER", "ADMIN")));
        for (String username : users.keySet()) {
            User newUser = new User(username, users.get(username));
            for (String role : userRoles.get(username)) {
                newUser.addRole(role);
            }
            try {
                saveUser(newUser);
            } catch (UserExistsException e) {
                // Doesn't matter if this happens it is expected
                //System.out.println("User " + username + " already exists.");
            }
        }
    }

}
