package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

    Location findByIdAndUser(long id, User user);

    Location findByUserAndNameAndType(User user, String name, String type);

    List<Location> findAllByUser(User user);

}
