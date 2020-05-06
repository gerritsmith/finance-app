package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryLegRepository extends CrudRepository<DeliveryLeg, Long> {
}
