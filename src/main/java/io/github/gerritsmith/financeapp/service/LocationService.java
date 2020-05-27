package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.LocationRepository;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.LocationType;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {

    private LocationRepository locationRepository;

    // Constructors
    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    // Read
    public Location findByUserAndNameAndAddressAndApt(User user, String name, String address, String apt) {
        return locationRepository.findByUserAndNameAndAddressAndApt(user, name, address, apt);
    }

    public Location findByUserAndName(User user, String name) {
        return locationRepository.findByUserAndName(user, name);
    }

    public Location findByIdAsUser(long id, User user) {
        return locationRepository.findByIdAndUser(id, user);
    }

    public List<Location> findAllLocationsByUser(User user) {
        return locationRepository.findAllByUser(user);
    }

    public List<Location> findAllPickupLocationsByUser(User user) {
        return locationRepository.findAllByUserAndType(user, LocationType.PICKUP);
    }

    public List<Location> findAllDropoffLocationsByUser(User user) {
        return locationRepository.findAllByUserAndType(user, LocationType.DROPOFF);
    }

    // Create
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    public Location addLocation(Location location) throws LocationExistsException {
        String errorMessage;
        Location locationExists;
        if (location.getType() == LocationType.DROPOFF) {
            locationExists = findByUserAndNameAndAddressAndApt(location.getUser(),
                                                                        location.getName(),
                                                                        location.getAddress(),
                                                                        location.getApt());
            errorMessage = "Already have " + location.getName() + " location at "
                    + location.getAddress() + " in apt " + location.getApt();
        } else {
            locationExists = findByUserAndName(location.getUser(),
                                               location.getName());
            errorMessage = "Already have " + location.getName() + " location";
        }
        if (locationExists != null) {
            throw new LocationExistsException(errorMessage);
        }
        return locationRepository.save(location);
    }

    // Update
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    public Location updateLocation(long locationId,
                                   Location updatedLocation) throws LocationExistsException {
        Location locationToUpdate = findByIdAsUser(locationId, updatedLocation.getUser());
        Location locationExistsWithNameAtAddress = findByUserAndNameAndAddressAndApt(updatedLocation.getUser(),
                                                                                     updatedLocation.getName(),
                                                                                     updatedLocation.getAddress(),
                                                                                     updatedLocation.getApt());
        if (locationExistsWithNameAtAddress != null &&
                !locationToUpdate.equals(locationExistsWithNameAtAddress)) {
            throw new LocationExistsException("Already have " +
                    locationExistsWithNameAtAddress.getName() + " location at " +
                    locationExistsWithNameAtAddress.getAddress() + " in apt " +
                    locationExistsWithNameAtAddress.getApt());
        }
        locationToUpdate.update(updatedLocation);
        return locationRepository.save(locationToUpdate);
    }

}
