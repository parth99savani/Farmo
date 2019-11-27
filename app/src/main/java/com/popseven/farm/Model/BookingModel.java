package com.popseven.farm.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingModel {

    private String farmId;
    private ArrayList<String> bookedDates;
    private Integer amountPaid;
    private Integer guest;
    private String bookedDate;
    private Boolean ratingStatus;
    private Boolean cancelStatus;

    public BookingModel(){

    }

    public BookingModel(String farmId, ArrayList<String> bookedDates, Integer amountPaid, Integer guest, String bookedDate, Boolean ratingStatus, Boolean cancelStatus) {
        this.farmId = farmId;
        this.bookedDates = bookedDates;
        this.amountPaid = amountPaid;
        this.guest = guest;
        this.bookedDate = bookedDate;
        this.ratingStatus = ratingStatus;
        this.cancelStatus = cancelStatus;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public ArrayList<String> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(ArrayList<String> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Integer amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Integer getGuest() {
        return guest;
    }

    public void setGuest(Integer guest) {
        this.guest = guest;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public Boolean getRatingStatus() {
        return ratingStatus;
    }

    public void setRatingStatus(Boolean ratingStatus) {
        this.ratingStatus = ratingStatus;
    }

    public Boolean getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(Boolean cancelStatus) {
        this.cancelStatus = cancelStatus;
    }
}
