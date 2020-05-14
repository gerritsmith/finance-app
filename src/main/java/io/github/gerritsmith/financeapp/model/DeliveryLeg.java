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

    @ManyToOne
    private Location pickup;

    @ManyToOne
    private Location dropoff;

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

    public Location getPickup() {
        return pickup;
    }

    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }

    public Location getDropoff() {
        return dropoff;
    }

    public void setDropoff(Location dropoff) {
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
