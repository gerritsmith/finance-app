package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.DeliveryLegFormDTO;
import io.github.gerritsmith.financeapp.dto.upload.DeliveryCSVRow;

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

    private Double cash;

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
        String cashString = deliveryLegFormDTO.getCash();
        cash = cashString.isEmpty() ? null : Double.parseDouble(cashString);
        note = deliveryLegFormDTO.getNote();
    }

    public DeliveryLeg(DeliveryCSVRow deliveryCSVRow,
                       Location pickup,
                       Location dropoff) {
        foodTotal = deliveryCSVRow.getFoodTotal();
        tip = deliveryCSVRow.getTip();
        cash = deliveryCSVRow.getCash();
        note = deliveryCSVRow.getNote();
        this.pickup = pickup;
        this.dropoff = dropoff;
    }

    // Methods
    public void update(DeliveryLeg deliveryLeg) {
        foodTotal = deliveryLeg.getFoodTotal();
        tip = deliveryLeg.getTip();
        pickup = deliveryLeg.getPickup();
        dropoff = deliveryLeg.getDropoff();
        cash = deliveryLeg.getCash();
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

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
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
