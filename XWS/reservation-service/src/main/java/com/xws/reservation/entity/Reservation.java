package com.xws.reservation.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "reservation")
public class Reservation {
    @MongoId
    private String id;
    private String sourceUser;
    private String idAccommodation;
    private Date startDate;
    private Date endDate;
    private int numGuests;
    private String state;

    public Reservation() {
    }

    public Reservation(String sourceUser, String idAccommodation, Date startDate, Date endDate, int numGuests) {
        this.sourceUser = sourceUser;
        this.idAccommodation = idAccommodation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.state = "Pending";
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getSourceUser() {
        return sourceUser;
    }

    public String getIdAccommodation() {
        return idAccommodation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getNumGuests() {
        return numGuests;
    }
}