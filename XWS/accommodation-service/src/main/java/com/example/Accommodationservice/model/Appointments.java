package com.example.Accommodationservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "appointments")
public class Appointments {

    @Id
    private String id;
    private Date start;
    private Date end;
    private Boolean reserved;
    private EPrice price;
    private EPricePer price_per;
    @DBRef
    private List<Reservation> reservations;
    private boolean auto_reservation;

    public Appointments(){}

    public Appointments(Date start, Date end, Boolean reserved, EPrice price, EPricePer price_per, List<Reservation> reservations, boolean auto_reservation) {
        this.start = start;
        this.end = end;
        this.reserved = reserved;
        this.price = price;
        this.price_per = price_per;
        this.reservations = reservations;
        this.auto_reservation = auto_reservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public EPrice getPrice() {
        return price;
    }

    public void setPrice(EPrice price) {
        this.price = price;
    }

    public EPricePer getPrice_per() {
        return price_per;
    }

    public void setPrice_per(EPricePer price_per) {
        this.price_per = price_per;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isAuto_reservation() {
        return auto_reservation;
    }

    public void setAuto_reservation(boolean auto_reservation) {
        this.auto_reservation = auto_reservation;
    }
}