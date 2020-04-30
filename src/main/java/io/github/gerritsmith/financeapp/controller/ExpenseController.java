package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.ExpenseFormDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExpenseController {

    @GetMapping("/expenses")
    public String displayExpensesHome(Model model) {
        return "expense/home";
    }

    @GetMapping("/expense/new")
    public String displayNewExpenseForm(Model model) {
        model.addAttribute("expenseFormDTO", new ExpenseFormDTO());
        return "expense/form";
    }

}
