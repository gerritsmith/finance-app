package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.ExpenseFormDTO;
import io.github.gerritsmith.financeapp.exception.ExpenseExistsException;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.ExpenseService;
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
public class ExpenseController {

    @Autowired
    UserService userService;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/expenses")
    public String displayExpensesHome(Model model) {
        return "expense/home";
    }

    @GetMapping("/expense/new")
    public String displayNewExpenseForm(Model model) {
        model.addAttribute("expenseFormDTO", new ExpenseFormDTO());
        return "expense/form";
    }

    @PostMapping("/expense/new")
    public String processNewExpenseForm(@ModelAttribute @Valid ExpenseFormDTO expenseFormDTO,
                                        Errors errors,
                                        Principal principal) {
        if (errors.hasErrors()) {
            return "expense/form";
        }
        try {
            User user = userService.findUserByUsername(principal.getName());
            Expense newExpense = new Expense(user, expenseFormDTO);
            expenseService.addExpense(newExpense);
        } catch (ExpenseExistsException e) {
            errors.reject("expense.alreadyExists", e.getMessage());
            return "expense/form";
        }
        return "redirect:/expenses";
    }

}
