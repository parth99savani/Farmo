package com.popseven.farm.Model;

public class IdModel {

    private Integer bookingId;
    private Integer farmId;

    public IdModel(){

    }

    public IdModel(Integer bookingId, Integer farmId) {
        this.bookingId = bookingId;
        this.farmId = farmId;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getFarmId() {
        return farmId;
    }

    public void setFarmId(Integer farmId) {
        this.farmId = farmId;
    }
}
