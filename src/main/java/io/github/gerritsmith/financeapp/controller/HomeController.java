package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.ChangePasswordFormDTO;
import io.github.gerritsmith.financeapp.dto.form.LoginFormDTO;
import io.github.gerritsmith.financeapp.dto.form.RegisterFormDTO;
import io.github.gerritsmith.financeapp.dto.UserStatsDTO;
import io.github.gerritsmith.financeapp.exception.UserPasswordIncorrectException;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.exception.UserExistsException;
import io.github.gerritsmith.financeapp.service.StatsService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    StatsService statsService;

    @GetMapping("/")
    public String displayHomePage() {
        return "index";
    }

    @GetMapping("/user")
    public String displayUserPage(Model model,
                                  Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        UserStatsDTO userStatsDTO = statsService.getUserStats(user);
        model.addAttribute("userStatsDTO", userStatsDTO);
        model.addAttribute("title", "User Home");
        return "user/home";
    }

    @GetMapping("/user/settings")
    public String displayUserSettingsPage(Model model) {
        model.addAttribute("changePasswordFormDTO", new ChangePasswordFormDTO());
        return "user/settings";
    }

    @PostMapping("/user/change-password")
    public String processChangePasswordForm(@ModelAttribute ChangePasswordFormDTO changePasswordFormDTO,
                                            Errors errors,
                                            Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        String newPassword = changePasswordFormDTO.getNewPassword();
        String verifyPassword = changePasswordFormDTO.getVerifyPassword();
        if (!newPassword.equals(verifyPassword)) {
            errors.rejectValue("verifyPassword",
                    "verifyPassword.mismatch",
                    "passwords do not match");
        }
        if (errors.hasErrors()) {
            return "user/settings";
        }
        try {
            userService.updateUserPassword(user, changePasswordFormDTO);
        } catch (UserPasswordIncorrectException e) {
            errors.rejectValue("currentPassword",
                    "currentPassword.incorrect", e.getMessage());
            return "user/settings";
        }
        return "redirect:/user/settings?update=true";
    }

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "user/admin";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute("loginFormDTO", new LoginFormDTO());
        return "user/login";
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
