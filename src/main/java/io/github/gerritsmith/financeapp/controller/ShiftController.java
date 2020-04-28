package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.dto.ShiftFormDTO;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Controller
public class ShiftController {

    @Autowired
    UserService userService;

    @Autowired
    ShiftService shiftService;

    @GetMapping("/shifts")
    public String displayShiftsHome(Model model,
                                    Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<Shift> shifts = new ArrayList<>((Collection<Shift>) shiftService.findAllShiftsByUser(user));
        shifts.sort(new Comparator<Shift>() {
            @Override
            public int compare(Shift o1, Shift o2) {
                if (o1.getDate().isAfter(o2.getDate())) {
                    return -1;
                } else if (o1.getDate().isEqual(o2.getDate())) {
                    if (o1.getStartTime().isAfter(o2.getStartTime())) {
                        return -1;
                    }
                }
                return 1;
            }
        });
        model.addAttribute("shifts", shifts);
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

    @GetMapping("/shift/{shiftId}")
    public String displayShiftDetails(@PathVariable long shiftId,
                                         Principal principal,
                                         Model model) {
        User user = userService.findUserByUsername(principal.getName());
        Shift shift = shiftService.findByIdAsUser(shiftId, user);
        if (shift == null) {
            return "error/404";
        }
        ShiftFormDTO shiftFormDTO = new ShiftFormDTO(shift);
        model.addAttribute("shiftFormDTO", shiftFormDTO);
        return "shift/form";
    }

}
