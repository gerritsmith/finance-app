package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.ExpenseFormDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class ExpenseController {

    @Autowired
    UserService userService;

    @Autowired
    ExpenseService expenseService;

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/expenses")
    public String displayExpensesHome(Model model, @ModelAttribute User user) {
        List<Expense> expenses = expenseService.findAllExpensesByUser(user);
        expenses.sort((o1, o2) -> {
            if (o1.getDate().isAfter(o2.getDate())) {
                return -1;
            } else if (o1.getDate().isEqual(o2.getDate())) {
                if (o1.getTime().isAfter(o2.getTime())) {
                    return -1;
                }
            }
            return 1;
        });
        model.addAttribute("expenses", expenses);
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
                                        @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "expense/form";
        }
        try {
            Expense newExpense = new Expense(user, expenseFormDTO);
            expenseService.addExpense(newExpense);
        } catch (ExpenseExistsException e) {
            errors.reject("expense.alreadyExists", e.getMessage());
            return "expense/form";
        }
        return "redirect:/expenses";
    }

    @GetMapping("/expense/{expenseId}")
    public String displayExpenseDetails(@PathVariable long expenseId,
                                        Model model,
                                        @ModelAttribute User user) {
        Expense expense = expenseService.findByIdAsUser(expenseId, user);
        if (expense == null) {
            return "error/404";
        }
        ExpenseFormDTO expenseFormDTO = new ExpenseFormDTO(expense);
        model.addAttribute("expenseFormDTO", expenseFormDTO);
        return "expense/form";
    }

    @PostMapping("/expense/{expenseId}")
    public String processExpenseUpdateForm(@PathVariable long expenseId,
                                           @ModelAttribute @Valid ExpenseFormDTO expenseFormDTO,
                                           Errors errors,
                                           @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "expense/form";
        }
        try {
            Expense updatedExpense = new Expense(user, expenseFormDTO);
            expenseService.updateExpense(expenseId, updatedExpense);
        } catch (ExpenseExistsException e) {
            errors.reject("expense.alreadyExists", e.getMessage());
            return "expense/form";
        }
        return "redirect:/expenses";
    }

}
