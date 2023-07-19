package com.example.Accommodationservice.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Document(collection = "accommodation")
public class Accommodation {

    @Id
    private String id;
    private String name;
    private String location;
    private String benefits;
    private byte[] pic;
    private Integer min_guests;
    private Integer max_guests;
    @DBRef
    private List<Appointments> appointments;
    private String user_id;

    public Accommodation(){}

    public Accommodation(String name, String location, String benefits, Integer min_guests, Integer max_guests, List<Appointments> appointments, String user_id) {

        this.name = name;
        this.location = location;
        this.benefits = benefits;
        this.min_guests = min_guests;
        this.max_guests = max_guests;
        this.appointments = appointments;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Accommodation(String name, String location, String benefits, Integer min_guests, Integer max_guests, List<Appointments> appointments) {

        this.name = name;
        this.location = location;
        this.benefits = benefits;
        this.min_guests = min_guests;
        this.max_guests = max_guests;
        this.appointments = appointments;
    }

    public Accommodation(String name, String location, String benefits, Integer min_guests, Integer max_guests) {
        this.name = name;
        this.location = location;
        this.benefits = benefits;
        this.min_guests = min_guests;
        this.max_guests = max_guests;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public Integer getMin_guests() {
        return min_guests;
    }

    public void setMin_guests(Integer min_guests) {
        this.min_guests = min_guests;
    }

    public Integer getMax_guests() {
        return max_guests;
    }

    public void setMax_guests(Integer max_guests) {
        this.max_guests = max_guests;
    }

    public List<Appointments> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointments> appointments) {
        this.appointments = appointments;
    }

}