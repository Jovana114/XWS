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
    private EPrice price_type;
    private EPricePer price_per;
    @DBRef
    private List<Reservation> reservations;
    private boolean auto_reservation;
    private Integer price;

    public Appointments(){}

    public Appointments(Date start, Date end, Boolean reserved, EPrice price_type, EPricePer price_per, List<Reservation> reservations, boolean auto_reservation, Integer price) {
        this.start = start;
        this.end = end;
        this.reserved = reserved;
        this.price_type = price_type;
        this.price_per = price_per;
        this.reservations = reservations;
        this.auto_reservation = auto_reservation;
        this.price = price;
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

    public EPrice getPrice_type() {
        return price_type;
    }

    public void setPrice_type(EPrice price_type) {
        this.price_type = price_type;
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}