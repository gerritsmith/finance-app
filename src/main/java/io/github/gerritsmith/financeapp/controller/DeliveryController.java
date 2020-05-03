package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.DeliveryService;
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
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    UserService userService;

    @GetMapping("/deliveries")
    public String displayDeliveriesHome(Model model,
                                        Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<Delivery> deliveries = deliveryService.findAllDeliveriesByUser(user);
        deliveries.sort((o1, o2) -> {
            if (o1.getDate().isAfter(o2.getDate())) {
                return -1;
            } else if (o1.getDate().isEqual(o2.getDate())) {
                if (o1.getTime().isAfter(o2.getTime())) {
                    return -1;
                }
            }
            return 1;
        });
        model.addAttribute("deliveries", deliveries);
        return "delivery/home";
    }

    @GetMapping("/delivery/new")
    public String displayNewDeliveryForm(Model model) {
        model.addAttribute("deliveryFormDTO", new DeliveryFormDTO());
        return "delivery/form";
    }

    @PostMapping("/delivery/new")
    public String processNewDeliveryForm(@ModelAttribute @Valid DeliveryFormDTO deliveryFormDTO,
                                         Errors errors,
                                         Principal principal) {
        ////////////////////////////////////
        System.out.println(deliveryFormDTO.getLegs());
        if (deliveryFormDTO.getLegs() != null) {
            for (DeliveryLeg deliveryLeg : deliveryFormDTO.getLegs()) {
                System.out.println(deliveryLeg);
            }
        }
        ////////////////////////////////////
        if (errors.hasErrors()) {
            return "delivery/form";
        }
        try {
            User user = userService.findUserByUsername(principal.getName());
            Delivery newDelivery = new Delivery(user, deliveryFormDTO);
            deliveryService.addDelivery(newDelivery);
        } catch (DeliveryExistsException e) {
            errors.reject("delivery.alreadyExists", e.getMessage());
            return "delivery/form";
        }
        return "redirect:/deliveries";
    }

    @GetMapping("/delivery/{deliveryId}")
    public String displayDeliveryDetails(@PathVariable long deliveryId,
                                         Principal principal,
                                         Model model) {
        User user = userService.findUserByUsername(principal.getName());
        Delivery delivery = deliveryService.findByIdAsUser(deliveryId, user);
        if (delivery == null) {
            return "error/404";
        }
        DeliveryFormDTO deliveryFormDTO = new DeliveryFormDTO(delivery);
        model.addAttribute("deliveryFormDTO", deliveryFormDTO);
        return "delivery/form";
    }

    @PostMapping("/delivery/{deliveryId}")
    public String processDeliveryUpdateForm(@PathVariable long deliveryId,
                                            @ModelAttribute @Valid DeliveryFormDTO deliveryFormDTO,
                                            Errors errors,
                                            Principal principal) {
        ////////////////////////////////////
        System.out.println(deliveryFormDTO.getLegs());
        if (deliveryFormDTO.getLegs() != null) {
            for (DeliveryLeg deliveryLeg : deliveryFormDTO.getLegs()) {
                System.out.println(deliveryLeg);
            }
        }
        ////////////////////////////////////
        if (errors.hasErrors()) {
            return "delivery/form";
        }
        try {
            User user = userService.findUserByUsername(principal.getName());
            Delivery updatedDelivery = new Delivery(user, deliveryFormDTO);
            deliveryService.updateDelivery(deliveryId, updatedDelivery);
        } catch (DeliveryExistsException e) {
            errors.reject("delivery.alreadyExists", e.getMessage());
            return "delivery/form";
        }
        return "redirect:/deliveries";
    }

}
