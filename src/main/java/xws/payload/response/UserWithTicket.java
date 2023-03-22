package xws.payload.response;

import xws.model.Flight;
import xws.model.User;

public class UserWithTicket {

    private User user;
    private Flight flight;

    public UserWithTicket(){}

    public UserWithTicket(User user, Flight flight) {
        this.user = user;
        this.flight = flight;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }
}
