package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.LocationFormDTO;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.LocationService;
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
public class LocationController {

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @GetMapping("/locations")
    public String displayLocationsHome() {
        return "location/home";
    }

    @GetMapping("/location/new")
    public String displayNewLocationForm(Model model) {
        model.addAttribute("locationFormDTO", new LocationFormDTO());
        return "location/form";
    }

    @PostMapping("/location/new")
    public String processNewLocationForm(@ModelAttribute @Valid LocationFormDTO locationFormDTO,
                                         Errors errors,
                                         Principal principal) {
        if (errors.hasErrors()) {
            return "location/form";
        }
        try {
            User user = userService.findUserByUsername(principal.getName());
            Location newLocation = new Location(user, locationFormDTO);
            locationService.addLocation(newLocation);
        } catch (LocationExistsException e) {
            errors.reject("location.alreadyExists", e.getMessage());
            return "location/form";
        }
        return "redirect:/locations";
    }

}
