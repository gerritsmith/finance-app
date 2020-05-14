package io.github.gerritsmith.financeapp.dto.form;

import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import io.github.gerritsmith.financeapp.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class DeliveryLegFormDTO {

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "food total must be in the form 0.00")
    private String foodTotal;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "tip must be in the form 0.00")
    private String tip;

    @NotNull(message = "pickup location is required")
    private Location pickup;

    private Location dropoff;
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
        return "DeliveryLegFormDTO{" +
                "foodTotal='" + foodTotal + '\'' +
                ", tip='" + tip + '\'' +
                ", pickup='" + pickup + '\'' +
                ", dropoff='" + dropoff + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

}
