package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

    @Override
    List<User> findAll();

}
