package com.xws.user.entity;

public class Rating {
    private int guestId;
    private int hostId;
    private int rating;
    private String date;
    private int ratingValue;

    public Rating(int guestId, int hostId, int rating, String date) {
        this.guestId = guestId;
        this.hostId = hostId;
        this.rating = rating;
        this.date = date;
    }

    public Rating() {

    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getGuestId() {
        return guestId;
    }

    public int getHostId() {
        return hostId;
    }

    public int getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingValue() {
        return ratingValue;
    }
}

