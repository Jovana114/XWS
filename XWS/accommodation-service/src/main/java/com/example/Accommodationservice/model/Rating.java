package com.example.Accommodationservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rating_accommodations")
public class Rating {
    @Id
    private Long id;
    private Long guestId;
    private Long accommodationId;
    private int ratingValue;

    public Rating(Long id, Long guestId, Long accommodationId, int ratingValue) {
        this.id = id;
        this.guestId = guestId;
        this.accommodationId = accommodationId;
        this.ratingValue = ratingValue;
    }

    public Rating() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Long getGuestId() {
        return guestId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public int getRatingValue() {
        return ratingValue;
    }
}
