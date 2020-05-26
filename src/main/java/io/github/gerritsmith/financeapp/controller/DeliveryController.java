package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.form.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.dto.form.LocationFormDTO;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.exception.DeliveryWithoutShiftException;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.DeliveryService;
import io.github.gerritsmith.financeapp.service.LocationService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@SessionAttributes("deliveryFormDTO")
public class DeliveryController {

    @Autowired
    DeliveryService deliveryService;

    @Autowired
    UserService userService;

    @Autowired
    LocationService locationService;

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("pickupLocations", locationService.findAllPickupLocationsByUser(user));
        model.addAttribute("dropoffLocations", locationService.findAllDropoffLocationsByUser(user));
    }

    @GetMapping("/deliveries")
    public String displayDeliveriesHome(Model model, @ModelAttribute User user) {
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
        model.addAttribute("deliveryFormDTO", new DeliveryFormDTO());
        return "delivery/home";
    }

    @GetMapping("/delivery/new")
    public String displayNewDeliveryForm(Model model) {
        if (!model.containsAttribute("deliveryFormDTO")) {
            model.addAttribute("deliveryFormDTO", new DeliveryFormDTO());
        }
        return "delivery/form";
    }

    @PostMapping("/delivery/new")
    public String processNewDeliveryForm(@ModelAttribute @Valid DeliveryFormDTO deliveryFormDTO,
                                         Errors errors,
                                         @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "delivery/form";
        }
        try {
            Delivery newDelivery = new Delivery(user, deliveryFormDTO);
            deliveryService.addDelivery(newDelivery);
        } catch (DeliveryWithoutShiftException e) {
            errors.reject("delivery.withoutShift", e.getMessage());
            return "delivery/form";
        } catch (DeliveryExistsException e) {
            errors.reject("delivery.alreadyExists", e.getMessage());
            return "delivery/form";
        }
        return "redirect:/deliveries";
    }

    @GetMapping("/delivery/{deliveryId}")
    public String displayDeliveryDetails(@PathVariable long deliveryId,
                                         Model model,
                                         @ModelAttribute User user) {
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
                                            @ModelAttribute User user) {
        if (errors.hasErrors()) {
            return "delivery/form";
        }
        try {
            Delivery updatedDelivery = new Delivery(user, deliveryFormDTO);
            deliveryService.updateDelivery(deliveryId, updatedDelivery);
        } catch (DeliveryWithoutShiftException e) {
            errors.reject("delivery.withoutShift", e.getMessage());
            return "delivery/form";
        } catch (DeliveryExistsException e) {
            errors.reject("delivery.alreadyExists", e.getMessage());
            return "delivery/form";
        }
        return "redirect:/deliveries";
    }

    @PostMapping("/delivery-form/location/new")
    public String displayLocationFormFromDeliveryForm(Model model,
                                                      @ModelAttribute LocationFormDTO locationFormDTO,
                                                      Errors errors,
                                                      @ModelAttribute DeliveryFormDTO deliveryFormDTO,
                                                      @ModelAttribute User user,
                                                      @RequestParam int legIndex,
                                                      @RequestParam String urlPath) {
        if (locationFormDTO.getAddress() == null) {
            model.addAttribute("locationFormDTO", new LocationFormDTO());
            model.addAttribute("legIndex", legIndex);
            model.addAttribute("urlPath", urlPath);
            return "location/form";
        } else {
            if (errors.hasErrors()) {
                return "location/form";
            }
            try {
                Location newLocation = new Location(user, locationFormDTO);
                locationService.addLocation(newLocation);
                deliveryFormDTO.getLegs().get(legIndex).setDropoff(newLocation);
            } catch (LocationExistsException e) {
                Location existingLocation = locationService.findByUserAndNameAndAddressAndApt(user,
                        locationFormDTO.getName(),
                        locationFormDTO.getAddress(),
                        locationFormDTO.getApt());
                deliveryFormDTO.getLegs().get(legIndex).setDropoff(existingLocation);
            }
            return "redirect:/delivery/new";
        }
    }

}
