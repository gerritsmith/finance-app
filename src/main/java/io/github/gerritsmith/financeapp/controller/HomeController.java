package io.github.gerritsmith.financeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String displayHomePage() {
        return "<h1>Hello, World!</h1>";
    }

}
