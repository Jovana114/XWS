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
    private EPrice price;
    private EPricePer price_per;

    public Appointments(){}

    public Appointments(Date start, Date end, Boolean reserved, EPrice price, EPricePer price_per) {
        this.start = start;
        this.end = end;
        this.reserved = reserved;
        this.price = price;
        this.price_per = price_per;
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
}