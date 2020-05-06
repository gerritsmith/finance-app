package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.DeliveryLeg;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DeliveryLegFormDTO {

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "food total must be in the form 0.00")
    private String foodTotal;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "tip must be in the form 0.00")
    private String tip;

    @NotBlank(message = "pickup location is required")
    private String pickup;

    private String dropoff;
    private String note;

    // Constructors
    public DeliveryLegFormDTO() {}

    public DeliveryLegFormDTO(DeliveryLeg deliveryLeg) {
        foodTotal = (deliveryLeg.getFoodTotal() == null) ? "" : Double.toString(deliveryLeg.getFoodTotal());
        tip = (deliveryLeg.getTip() == null) ? "" : Double.toString(deliveryLeg.getTip());
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
