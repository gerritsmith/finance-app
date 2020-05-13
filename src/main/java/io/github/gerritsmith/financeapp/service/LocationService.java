package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.LocationRepository;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Location findByUserAndNameAndType(User user, String name, String type) {
        return locationRepository.findByUserAndNameAndType(user, name, type);
    }

    public List<Location> findAllLocationsByUser(User user) {
        return locationRepository.findAllByUser(user);
    }

    // Create
    @Transactional
    public Location addLocation(Location location) throws LocationExistsException {
        Location locationExists = findByUserAndNameAndType(location.getUser(),
                                                           location.getName(),
                                                           location.getType());
        if (locationExists != null) {
            throw new LocationExistsException("Already have " +
                    location.getType() + " location " + location.getName());
        }
        return locationRepository.save(location);
    }

}
