package com.example.Accommodationservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "appointments")
public class Appointments {

    @Id
    private String id;
    private Date start;
    private Date end;
    private Boolean reserved;
    private Integer price_per_guest;
    private Integer price_per_accommodation;

    public Appointments(){}

    public Appointments(String id, Date start, Date end, Boolean reserved, Integer price_per_guest, Integer price_per_accommodation) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.reserved = reserved;
        this.price_per_guest = price_per_guest;
        this.price_per_accommodation = price_per_accommodation;
    }

    public Appointments(Date start, Date end, Boolean reserved, Integer price_per_guest, Integer price_per_accommodation) {
        this.start = start;
        this.end = end;
        this.reserved = reserved;
        this.price_per_guest = price_per_guest;
        this.price_per_accommodation = price_per_accommodation;
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

    public Integer getPrice_per_guest() {
        return price_per_guest;
    }

    public void setPrice_per_guest(Integer price_per_guest) {
        this.price_per_guest = price_per_guest;
    }

    public Integer getPrice_per_accommodation() {
        return price_per_accommodation;
    }

    public void setPrice_per_accommodation(Integer price_per_accommodation) {
        this.price_per_accommodation = price_per_accommodation;
    }
}