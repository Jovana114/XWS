package com.example.Accommodationservice.model;

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
    private Boolean approved;

    public Reservation() {
    }

    public Reservation(String id, String sourceUser, String idAccommodation, Date startDate, Date endDate, int numGuests, Boolean approved) {
        this.id = id;
        this.sourceUser = sourceUser;
        this.idAccommodation = idAccommodation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.approved = approved;
    }

    public Reservation(String sourceUser, String idAccommodation, Date startDate, Date endDate, int numGuests, Boolean approved) {
        this.sourceUser = sourceUser;
        this.idAccommodation = idAccommodation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.approved = approved;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public void setIdAccommodation(String idAccommodation) {
        this.idAccommodation = idAccommodation;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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