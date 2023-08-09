package com.xws.user.entity;

import jakarta.validation.constraints.Email;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String first_name;
    private String last_name;
    private String address;
    private String username;
    @Email
    private String email;
    private String password;
    @DBRef
    private Set<Role> roles = new HashSet<>();
    @DBRef
    private List<Reservation> reservations;
    @DBRef
    private List<Accommodation> accommodations;
    private Integer cancellation_number;

    public User() {
    }

    public User(String first_name, String last_name, String address, String username, String email, String password, Integer cancellation_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cancellation_number = cancellation_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public Integer getCancellation_number() {
        return cancellation_number;
    }

    public void setCancellation_number(Integer cancellation_number) {
        this.cancellation_number = cancellation_number;
    }
}