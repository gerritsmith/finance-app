package io.github.gerritsmith.financeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExpenseController {

    @GetMapping("/expenses")
    public String displayExpensesHome(Model model) {
        return "expense/home";
    }

}
