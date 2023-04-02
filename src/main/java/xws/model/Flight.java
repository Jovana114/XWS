package xws.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "flights")
public class Flight {

    @Id
    private String id;
    private Date date_and_time_taking_off;
    private String place_taking_off;
    private Date date_and_time_landing;
    private String place_landing;
    private Integer number_of_seats; //maksimalan broj mesta
    private Integer number_of_tickets; //ostalo karata
    private Integer price;

    public Flight(){}

    public Flight(String id, Date date_and_time_taking_off, String place_taking_off, Date date_and_time_landing, String place_landing, Integer number_of_tickets, Integer price) {
        this.id = id;
        this.date_and_time_taking_off = date_and_time_taking_off;
        this.place_taking_off = place_taking_off;
        this.date_and_time_landing = date_and_time_landing;
        this.place_landing = place_landing;
        this.number_of_tickets = number_of_tickets;
        this.price = price;
    }

    public Flight(Date date_and_time_taking_off, String place_taking_off, Date date_and_time_landing, String place_landing, Integer number_of_tickets, Integer number_of_seats, Integer price) {
        this.date_and_time_taking_off = date_and_time_taking_off;
        this.place_taking_off = place_taking_off;
        this.date_and_time_landing = date_and_time_landing;
        this.place_landing = place_landing;
        this.number_of_seats = number_of_seats;
        this.number_of_tickets = number_of_tickets;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate_and_time_taking_off() {
        return date_and_time_taking_off;
    }

    public void setDate_and_time_taking_off(Date date_and_time_taking_off) {
        this.date_and_time_taking_off = date_and_time_taking_off;
    }

    public String getPlace_taking_off() {
        return place_taking_off;
    }

    public void setPlace_taking_off(String place_taking_off) {
        this.place_taking_off = place_taking_off;
    }

    public Date getDate_and_time_landing() {
        return date_and_time_landing;
    }

    public void setDate_and_time_landing(Date date_and_time_landing) {
        this.date_and_time_landing = date_and_time_landing;
    }

    public String getPlace_landing() {
        return place_landing;
    }

    public void setPlace_landing(String place_landing) {
        this.place_landing = place_landing;
    }

    public Integer getNumber_of_tickets() {
        return number_of_tickets;
    }

    public void setNumber_of_tickets(Integer number_of_tickets) {
        this.number_of_tickets = number_of_tickets;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNumber_of_seats() {
        return number_of_seats;
    }

    public void setNumber_of_seats(Integer number_of_seats) {
        this.number_of_seats = number_of_seats;
    }
}
