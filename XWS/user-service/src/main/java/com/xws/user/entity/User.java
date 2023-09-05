package com.xws.user.entity;

import com.xws.user.repo.RatingRepository;
import com.xws.user.service.impl.RatingService;
import jakarta.validation.constraints.Email;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

import static com.xws.user.entity.ERole.ROLE_HOST;

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
    private boolean highlighted;

    private RatingRepository ratingRepository;
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

    public boolean isIstaknutiHost() {
        
        double minRating = 4.7;
        double maxCancellationRate = 5.0;
        int minPastReservations = 5;
        int minTotalReservationDays = 50;

        // Replace the placeholders with actual logic based on your data
        boolean meetsRatingCondition = getRating() > minRating;
        boolean meetsCancellationRateCondition = calculateCancellationRate() < maxCancellationRate;
        boolean meetsPastReservationsCondition = getReservations() != null && getReservations().size() >= minPastReservations;
        boolean meetsTotalReservationDaysCondition = calculateTotalReservationDays() > minTotalReservationDays;

        return meetsRatingCondition &&
                meetsCancellationRateCondition &&
                meetsPastReservationsCondition &&
                meetsTotalReservationDaysCondition;
    }


    private double getRating() {
        Optional<Rating> userRatingOptional = ratingRepository.findByUserId(id); // Replace with your actual method in the repository
        if (userRatingOptional.isPresent()) {
            Rating userRating = userRatingOptional.get();
            return userRating.getRatingValue(); // Assuming Rating has a field for the rating value
        }
        return 0.0;
    }

    private double calculateCancellationRate() {
        if (reservations == null || reservations.isEmpty()) {
            // Handle the case when the user has no reservations
            return 0.0; // Return 0% cancellation rate
        }

        int totalReservations = reservations.size();
        int canceledReservations = 0;

        // Iterate through the user's reservations to count canceled reservations
        for (Reservation res : reservations) {
            if (res.getCanceled()) {
                canceledReservations++;
            }
        }

        // Calculate the cancellation rate as a percentage
        double cancellationRate = ((double) canceledReservations / totalReservations) * 100.0;

        return cancellationRate;
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