package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class DeliveryController {

    @GetMapping("/deliveries")
    public String displayDeliveriesHome() {
        return "delivery/home";
    }

    @GetMapping("/delivery/new")
    public String displayNewDeliveryForm(Model model) {
        model.addAttribute("deliveryFormDTO", new DeliveryFormDTO());
        return "delivery/form";
    }

    @PostMapping("/delivery/new")
    public String processNewDeliveryForm(@ModelAttribute @Valid DeliveryFormDTO deliveryFormDTO,
                                         Errors errors) {
        return "delivery/form";
    }

}
