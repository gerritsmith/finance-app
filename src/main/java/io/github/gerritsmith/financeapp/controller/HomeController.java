package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.LoginFormDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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

}
