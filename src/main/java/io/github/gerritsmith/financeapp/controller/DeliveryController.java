package io.github.gerritsmith.financeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeliveryController {

    @GetMapping("/deliveries")
    public String displayDeliveriesHome() {
        return "delivery/home";
    }

}
