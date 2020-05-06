package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.DeliveryLeg;

public class DeliveryLegFormDTO {

    private String foodTotal;
    private String tip;
    private String pickup;
    private String dropoff;
    private String note;

    // Constructors
    public DeliveryLegFormDTO() {}

    public DeliveryLegFormDTO(DeliveryLeg deliveryLeg) {
        foodTotal = deliveryLeg.getFoodTotal();
        tip = deliveryLeg.getTip();
        pickup = deliveryLeg.getPickup();
        dropoff = deliveryLeg.getDropoff();
        note = deliveryLeg.getNote();
    }

    // Getters and Setters
    public String getFoodTotal() {
        return foodTotal;
    }

    public void setFoodTotal(String foodTotal) {
        this.foodTotal = foodTotal;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
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
        return "DeliveryLegFormDTO{" +
                "foodTotal='" + foodTotal + '\'' +
                ", tip='" + tip + '\'' +
                ", pickup='" + pickup + '\'' +
                ", dropoff='" + dropoff + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

}