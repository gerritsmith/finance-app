package io.github.gerritsmith.financeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocationController {

    @GetMapping("/locations")
    public String displayLocationsHome() {
        return "location/home";
    }

}
