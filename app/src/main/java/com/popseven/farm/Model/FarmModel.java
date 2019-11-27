package com.popseven.farm.Model;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class FarmModel {

    private String id;
    private String name;
    private Integer price;
    private Integer guestCapacity;
    private Integer finalPrice;
    private Integer discount;
    private ArrayList<String> imageUrl;
    private ArrayList<String> bookedDates;
    private String checkInTime;
    private String checkOutTime;
    private String description;
    private String policy;
    private String state;
    private String city;
    private String streetAddress;
    private Integer zip;
    private ArrayList<String> featuresName;
    private ArrayList<String> featuresIcon;
    private GeoPoint mapLocation;
    private ArrayList<Float> rating;
    private Float ratingTotal;
    private Integer noOfBooking;

    public FarmModel() {
    }

    public FarmModel(String id, String name, Integer price, Integer guestCapacity, Integer finalPrice, Integer discount, ArrayList<String> imageUrl, ArrayList<String> bookedDates, String checkInTime, String checkOutTime, String description, String policy, String state, String city, String streetAddress, Integer zip, ArrayList<String> featuresName, ArrayList<String> featuresIcon, GeoPoint mapLocation, ArrayList<Float> rating, Float ratingTotal, Integer noOfBooking) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.guestCapacity = guestCapacity;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.imageUrl = imageUrl;
        this.bookedDates = bookedDates;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.description = description;
        this.policy = policy;
        this.state = state;
        this.city = city;
        this.streetAddress = streetAddress;
        this.zip = zip;
        this.featuresName = featuresName;
        this.featuresIcon = featuresIcon;
        this.mapLocation = mapLocation;
        this.rating = rating;
        this.ratingTotal = ratingTotal;
        this.noOfBooking = noOfBooking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getGuestCapacity() {
        return guestCapacity;
    }

    public void setGuestCapacity(Integer guestCapacity) {
        this.guestCapacity = guestCapacity;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(ArrayList<String> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }

    public ArrayList<String> getFeaturesName() {
        return featuresName;
    }

    public void setFeaturesName(ArrayList<String> featuresName) {
        this.featuresName = featuresName;
    }

    public ArrayList<String> getFeaturesIcon() {
        return featuresIcon;
    }

    public void setFeaturesIcon(ArrayList<String> featuresIcon) {
        this.featuresIcon = featuresIcon;
    }

    public GeoPoint getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(GeoPoint mapLocation) {
        this.mapLocation = mapLocation;
    }

    public ArrayList<Float> getRating() {
        return rating;
    }

    public void setRating(ArrayList<Float> rating) {
        this.rating = rating;
    }

    public Float getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(Float ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public Integer getNoOfBooking() {
        return noOfBooking;
    }

    public void setNoOfBooking(Integer noOfBooking) {
        this.noOfBooking = noOfBooking;
    }
}
