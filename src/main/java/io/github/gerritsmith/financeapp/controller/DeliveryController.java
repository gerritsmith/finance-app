package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

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
        if (errors.hasErrors()) {
            return "delivery/form";
        }
        try {
            double total = Double.parseDouble(deliveryFormDTO.getTotal());
            Delivery newDelivery = new Delivery(deliveryFormDTO.getDate(),
                                                deliveryFormDTO.getTime(),
                                                total);
            deliveryService.saveDelivery(newDelivery);
        } catch (DeliveryExistsException e) {
            errors.reject("delivery.alreadyExists", e.getMessage());
            return "delivery/form";
        }
        return "redirect:/deliveries";
    }

}
