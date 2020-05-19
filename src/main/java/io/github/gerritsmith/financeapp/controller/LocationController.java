package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.LocationFormDTO;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.LocationType;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.LocationService;
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
public class LocationController {

    @Autowired
    LocationService locationService;

    @Autowired
    UserService userService;

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/locations")
    public String displayLocationsHome(Model model, @ModelAttribute User user) {
        List<Location> locations = locationService.findAllLocationsByUser(user);
        locations.sort((o1, o2) -> {
            if (o1.getType().compareTo(o2.getType()) > 0) {
                return -1;
            } else if (o1.getType().equals(o2.getType())) {
                if (o1.getType() == LocationType.PICKUP) {
                    if (o1.getName().compareTo(o2.getName()) < 0) {
                        return -1;
                    } else if (o1.getName().equals(o2.getName())) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    return 0;
                }
            }
            return 1;
        });
        model.addAttribute("locations", locations);
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
                                         @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "location/form";
        }
        try {
            Location newLocation = new Location(user, locationFormDTO);
            locationService.addLocation(newLocation);
        } catch (LocationExistsException e) {
            errors.reject("location.alreadyExists", e.getMessage());
            return "location/form";
        }
        return "redirect:/locations";
    }

    @GetMapping("/location/{locationId}")
    public String displayLocationDetails(@PathVariable long locationId,
                                         Model model,
                                         @ModelAttribute User user) {
        Location location = locationService.findByIdAsUser(locationId, user);
        if (location == null) {
            return "error/404";
        }
        LocationFormDTO locationFormDTO = new LocationFormDTO(location);
        model.addAttribute("locationFormDTO", locationFormDTO);
        return "location/form";
    }

    @PostMapping("/location/{locationId}")
    public String processLocationUpdateForm(@PathVariable long locationId,
                                            @ModelAttribute @Valid LocationFormDTO locationFormDTO,
                                            Errors errors,
                                            @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "location/form";
        }
        try {
            Location updatedLocation = new Location(user, locationFormDTO);
            locationService.updateLocation(locationId, updatedLocation);
        } catch (LocationExistsException e) {
            errors.reject("location.alreadyExists", e.getMessage());
            return "location/form";
        }
        return "redirect:/locations";
    }

}
