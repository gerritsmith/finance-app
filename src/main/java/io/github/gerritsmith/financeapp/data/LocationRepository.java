package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.LocationType;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    Location findByIdAndUser(long id, User user);

    Location findByUserAndNameAndAddressAndApt(User user, String name, String address, String apt);

    List<Location> findAllByUser(User user);

    List<Location> findAllByUserAndType(User user, LocationType type);

}
