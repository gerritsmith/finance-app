package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.LoginFormDTO;
import io.github.gerritsmith.financeapp.dto.RegisterFormDTO;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.exception.UserExistsException;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String displayHomePage() {
        return "index";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute("loginFormDTO", new LoginFormDTO());
        return "user/login";
    }

    @GetMapping("/user")
    public String displayUserPage(Model model) {
        model.addAttribute("title", "User Home");
        return "user/home";
    }

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/admin";
    }

    @GetMapping("/register")
    public String displayRegisterUserForm(Model model) {
        model.addAttribute("registerFormDTO", new RegisterFormDTO());
        return "user/register";
    }

    @PostMapping("/register")
    public String processRegisterUserForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors) {
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("verifyPassword",
                               "verifyPassword.mismatch",
                               "passwords do not match");
        }
        if (errors.hasErrors()) {
            return "user/register";
        }
        try {
            String username = registerFormDTO.getUsername();
            User newUser = new User(username, password);
            userService.saveUser(newUser);
        } catch (UserExistsException e) {
            errors.rejectValue("username",
                               "username.alreadyExists",
                               e.getMessage());
            return "user/register";
        }
        return "redirect:/login";
    }

}
