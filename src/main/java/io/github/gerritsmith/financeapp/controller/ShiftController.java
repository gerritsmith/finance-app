package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.ShiftFormDTO;
import io.github.gerritsmith.financeapp.exception.DeliveryWithoutShiftException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class ShiftController {

    @Autowired
    UserService userService;

    @Autowired
    ShiftService shiftService;

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/shifts")
    public String displayShiftsHome(Model model, @ModelAttribute User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        shifts.sort((o1, o2) -> {
            if (o1.getDate().isAfter(o2.getDate())) {
                return -1;
            } else if (o1.getDate().isEqual(o2.getDate())) {
                if (o1.getStartTime().isAfter(o2.getStartTime())) {
                    return -1;
                }
            }
            return 1;
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
                                      @ModelAttribute User user) {
        if (!shiftFormDTO.getStartTime().isBefore(shiftFormDTO.getEndTime())) {
            errors.reject("shift.timeInterval", "The start time must be before the end time");
        }
        if (errors.hasErrors()) {
            return "shift/form";
        }
        try {
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
                                      Model model,
                                      @ModelAttribute User user) {
        Shift shift = shiftService.findByIdAsUser(shiftId, user);
        if (shift == null) {
            return "error/404";
        }
        ShiftFormDTO shiftFormDTO = new ShiftFormDTO(shift);
        model.addAttribute("shiftFormDTO", shiftFormDTO);
//        model.addAttribute("deliveries", shift.getDeliveries());
        return "shift/form";
    }

    @PostMapping("/shift/{shiftId}")
    public String processShiftUpdateForm(@PathVariable long shiftId,
                                         @ModelAttribute @Valid ShiftFormDTO shiftFormDTO,
                                         Errors errors,
                                         @ModelAttribute User user) {
        Shift shiftToUpdate = shiftService.findByIdAsUser(shiftId, user);
        shiftFormDTO.setDeliveries(shiftToUpdate.getDeliveries());
        if (!shiftFormDTO.getStartTime().isBefore(shiftFormDTO.getEndTime())) {
            errors.reject("shift.timeInterval", "The start time must be before the end time");
        }
        if (errors.hasErrors()) {
            return "shift/form";
        }
        try {
            Shift updatedShift = new Shift(user, shiftFormDTO);
            shiftService.updateShift(shiftId, updatedShift);
        } catch (DeliveryWithoutShiftException e) {
            errors.reject("shift.orphanDeliveries", e.getMessage());
            return "shift/form";
        } catch (ShiftExistsException e) {
            errors.reject("shift.overlap", e.getMessage());
            return "shift/form";
        }
        return "redirect:/shifts";
    }

}
