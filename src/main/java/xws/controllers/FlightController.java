package xws.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xws.model.Flight;
import xws.model.User;
import xws.payload.response.MessageResponse;
import xws.payload.response.UserWithTicket;
import xws.repository.FlightRepository;
import xws.repository.UserRepository;
import xws.service.FlightService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flight")
public class FlightController {

    @Autowired
    FlightRepository flightRepository;

    @Autowired
    FlightService flightService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/create/")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<?> createCenterProfile(@RequestBody Flight flight) {

        Flight new_flight = new Flight(flight.getDate_and_time_taking_off(),
                flight.getPlace_taking_off(), flight.getDate_and_time_landing(),
                flight.getPlace_landing(), flight.getNumber_of_tickets(), flight.getPrice());

        flightRepository.save(new_flight);

        return ResponseEntity.ok(new MessageResponse("Flight created successfully!"));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HOST') or hasRole('GUEST')")
    public ResponseEntity<?> allFlight() {
        List<Flight> flights = flightRepository.findAll();
        if(!flights.isEmpty())
            return new ResponseEntity<>(flights, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<Flight> updateFlight(@PathVariable("id") String id, @RequestBody Flight flight) {
        Optional<Flight> tutorialData = flightRepository.findById(id);

        if (tutorialData.isPresent()) {
            Flight _flight = tutorialData.get();
            _flight.setDate_and_time_taking_off(flight.getDate_and_time_taking_off());
            _flight.setPlace_taking_off(flight.getPlace_taking_off());
            _flight.setDate_and_time_landing(flight.getDate_and_time_landing());
            _flight.setPlace_landing(flight.getPlace_landing());
            _flight.setNumber_of_tickets(flight.getNumber_of_tickets());
            _flight.setPrice(flight.getPrice());
            return new ResponseEntity<>(flightRepository.save(_flight), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('HOST')")
    public ResponseEntity<HttpStatus> deleteFlight(@PathVariable("id") String id) {
        try {
            flightRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // karte:

    @PutMapping("/buyTicket/{tiket_id}_{user_id}")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<?> buyTicket(@PathVariable("tiket_id") String tiket_id, @PathVariable("user_id") String user_id) {
        try {
            Optional<Flight> flight = flightRepository.findById(tiket_id);
            Optional<User> user = userRepository.findById(user_id);
            if(flight.isPresent() && user.isPresent()){
                Flight _flight = flight.get();
                User _user = user.get();

                if(_flight.getNumber_of_tickets() != 0) {
                    _user.getFlights().add(_flight);
                    userRepository.save(_user);

                    _flight.setNumber_of_tickets(_flight.getNumber_of_tickets() - 1);
                    flightRepository.save(_flight);

                    return new ResponseEntity<>(_user.getFlights(), HttpStatus.OK);
                }
            } return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
