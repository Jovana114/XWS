package com.example.Accommodationservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reservation_ac")
public class Reservation {

    @Id
    private String id;
    private String sourceUser;
    private String idAppointment;
    private Date startDate;
    private Date endDate;
    private int numGuests;
    private Boolean approved;
    private Integer users_cancellation_number;

    public Reservation() {
    }

    public Reservation(String id, String sourceUser, String idAppointment, Date startDate, Date endDate, int numGuests, Boolean approved, Integer users_cancellation_number) {
        this.id = id;
        this.sourceUser = sourceUser;
        this.idAppointment = idAppointment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
        this.approved = approved;
        this.users_cancellation_number = users_cancellation_number;
    }

    public Reservation(String id, Date startDate, Date endDate, int numGuests) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numGuests = numGuests;
    }

    public void setSourceUser(String sourceUser) {
        this.sourceUser = sourceUser;
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public String getIdAppointment() {
        return idAppointment;
    }

    public void setIdAppointment(String idAppointment) {
        this.idAppointment = idAppointment;
    }

    public Integer getUsers_cancellation_number() {
        return users_cancellation_number;
    }

    public void setUsers_cancellation_number(Integer users_cancellation_number) {
        this.users_cancellation_number = users_cancellation_number;
    }
}