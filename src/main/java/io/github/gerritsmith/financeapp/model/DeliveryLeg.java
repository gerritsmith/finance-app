package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.DeliveryLegFormDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class DeliveryLeg extends AbstractEntity {

    @ManyToOne
    private Delivery delivery;

    private Double foodTotal;
    private Double tip;
    private String pickup;
    private String dropoff;
    private String note;

    // Constructors
    public DeliveryLeg() {}

    public DeliveryLeg(Delivery delivery, DeliveryLegFormDTO deliveryLegFormDTO) {
        this.delivery = delivery;
        String foodTotalString = deliveryLegFormDTO.getFoodTotal();
        foodTotal = foodTotalString.isEmpty() ? null : Double.parseDouble(foodTotalString);
        String tipString = deliveryLegFormDTO.getTip();
        tip = tipString.isEmpty() ? null : Double.parseDouble(tipString);
        pickup = deliveryLegFormDTO.getPickup();
        dropoff = deliveryLegFormDTO.getDropoff();
        note = deliveryLegFormDTO.getNote();
    }

    // Methods
    public void update(DeliveryLeg deliveryLeg) {
        foodTotal = deliveryLeg.getFoodTotal();
        tip = deliveryLeg.getTip();
        pickup = deliveryLeg.getPickup();
        dropoff = deliveryLeg.getDropoff();
        note = deliveryLeg.getNote();
    }

    // Getters and Setters
    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Double getFoodTotal() {
        return foodTotal;
    }

    public void setFoodTotal(Double foodTotal) {
        this.foodTotal = foodTotal;
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDropoff() {
        return dropoff;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "DeliveryLeg{" +
                "foodTotal='" + foodTotal + '\'' +
                ", tip='" + tip + '\'' +
                ", pickup='" + pickup + '\'' +
                ", dropoff='" + dropoff + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

}
