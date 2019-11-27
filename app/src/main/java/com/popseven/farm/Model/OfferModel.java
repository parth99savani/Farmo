package com.popseven.farm.Model;

public class OfferModel {

    private String offerImageUrl;

    public OfferModel(){

    }

    public OfferModel(String offerImageUrl) {
        this.offerImageUrl = offerImageUrl;
    }

    public String getOfferImageUrl() {
        return offerImageUrl;
    }

    public void setOfferImageUrl(String offerImageUrl) {
        this.offerImageUrl = offerImageUrl;
    }
}
