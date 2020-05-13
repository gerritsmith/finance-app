package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.dto.form.LocationFormDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocationController {

    @GetMapping("/locations")
    public String displayLocationsHome() {
        return "location/home";
    }

    @GetMapping("/location/new")
    public String displayNewLocationForm(Model model) {
        model.addAttribute("locationFormDTO", new LocationFormDTO());
        return "location/form";
    }

}
