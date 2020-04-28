package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.ShiftFormDTO;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.ShiftService;
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
public class ShiftController {

    @Autowired
    UserService userService;

    @Autowired
    ShiftService shiftService;

    @GetMapping("/shifts")
    public String displayShiftsHome() {
        return "shift/home";
    }

    @GetMapping("/shift/new")
    public String displayNewShiftForm(Model model) {
        model.addAttribute("shiftFormDTO", new ShiftFormDTO());
        return "shift/form";
    }

    @PostMapping("/shift/new")
    public String processNewShiftForm(@ModelAttribute @Valid ShiftFormDTO shiftFormDTO,
                                         Errors errors,
                                         Principal principal) {
        if (errors.hasErrors()) {
            return "shift/form";
        }
        try {
            User user = userService.findUserByUsername(principal.getName());
            Shift newShift = new Shift(user, shiftFormDTO);
            shiftService.addShift(newShift);
        } catch (ShiftExistsException e) {
            errors.reject("shift.overlap", e.getMessage());
            return "shift/form";
        }
        return "redirect:/shifts";
    }

}
