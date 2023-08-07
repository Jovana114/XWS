package com.xws.reservation.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reservation")
public class Reservation {

    @Id
    private String id;
    private String sourceUser;
    private String idAppointment;
    private Date startDate;
    private Date endDate;
    private int numGuests;
    private Boolean approved;

    public Reservation() {
    }

    public Reservation(String id, String sourceUser, String idAppointment, Date startDate, Date endDate, int numGuests, Boolean approved) {
        this.id = id;
        this.sourceUser = sourceUser;
        this.idAppointment = idAppointment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.approved = approved;
    }

    public Reservation(String sourceUser, String idAppointment, Date startDate, Date endDate, int numGuests, Boolean approved) {
        this.sourceUser = sourceUser;
        this.idAppointment = idAppointment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.approved = approved;
    }

    public Reservation(Date startDate, Date endDate, int numGuests) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
    }

    public void setIdAccommodation(String idAccommodation) {
        this.idAppointment = idAccommodation;
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
        return idAppointment;
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