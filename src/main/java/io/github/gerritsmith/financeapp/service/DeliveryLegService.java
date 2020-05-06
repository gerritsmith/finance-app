package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.DeliveryLegRepository;
import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryLegService {

    private DeliveryLegRepository deliveryLegRepository;

    // Constructors
    @Autowired
    public DeliveryLegService(DeliveryLegRepository deliveryLegRepository) {
        this.deliveryLegRepository = deliveryLegRepository;
    }

    // Delete
    @Transactional
    public void deleteDeliveryLeg(DeliveryLeg deliveryLeg) {
        deliveryLegRepository.delete(deliveryLeg);
    }

}
