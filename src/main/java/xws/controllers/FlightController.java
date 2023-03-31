package xws.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xws.model.Flight;
import xws.model.User;
import xws.payload.response.MessageResponse;
import xws.repository.FlightRepository;
import xws.repository.UserRepository;
import xws.service.FlightService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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
    @PreAuthorize("hasRole('GUEST') or hasRole('HOST') or hasRole('ADMIN')")
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

    @GetMapping("/allTicketsPerUser/{user_id}")
    @PreAuthorize("hasRole('GUEST')")
    public ResponseEntity<?> allTicketsPerUser(@PathVariable String user_id) {
        Optional<User> user = userRepository.findById(user_id);
        if(user.isPresent()){
            User _user = user.get();

            List<Flight> flights = new ArrayList<>();
            for (Flight fl: _user.getFlights()) {
                flights.add(fl);
            }
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/admin/create-flight")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createFlight(@RequestParam("dateTakingOff") Date dateTakingOff,
                                          @RequestParam("placeTakingOff") String placeTakingOff,
                                          @RequestParam("dateLanding") Date dateLanding,
                                          @RequestParam("placeLanding") String placeLanding,
                                          @RequestParam("numTickets") int numTickets,
                                          @RequestParam("price") int price) {

        Flight newFlight = new Flight(dateTakingOff, placeTakingOff, dateLanding, placeLanding, numTickets, price);
        flightRepository.save(newFlight);

        return ResponseEntity.ok(new MessageResponse("Flight created successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteFlightAsAdmin(@PathVariable("id") String id) {
        Optional<Flight> flightData = flightRepository.findById(id);
        if (flightData.isPresent()) {
            flightRepository.deleteById(id);
            return new ResponseEntity<>(new MessageResponse("Flight deleted successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Flight not found!"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('HOST') or hasRole('GUEST')")
    public ResponseEntity<?> searchFlights(@RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                           @RequestParam(name = "from") String from,
                                           @RequestParam(name = "to") String to,
                                           @RequestParam(name = "passengers") int passengers) {

        List<Flight> flights = flightRepository.findAll();
        List<Flight> searchResults = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.MONTH, date.getMonthValue() - 1);
        cal.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());

        for (Flight flight : flights) {
            Calendar flightCal = Calendar.getInstance();
            flightCal.setTime(flight.getDate_and_time_taking_off());
            if (flightCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                    flightCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                    flightCal.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
                if (flight.getPlace_taking_off().equalsIgnoreCase(from) && flight.getPlace_landing().equalsIgnoreCase(to)) {
                    if (flight.getNumber_of_tickets() >= passengers) {
                        searchResults.add(flight);
                    }
                }
            }
        }

        if (searchResults.isEmpty()) {
            return new ResponseEntity<>("No flights found for the given search criteria.", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> results = new ArrayList<>();
        for (Flight flight : searchResults) {
            Map<String, Object> flightDetails = new HashMap<>();
            flightDetails.put("id", flight.getId());
            flightDetails.put("date", date);
            flightDetails.put("from", flight.getPlace_taking_off());
            flightDetails.put("to", flight.getPlace_landing());
            flightDetails.put("availableSeats", flight.getNumber_of_tickets());

            double ticketPrice = flight.getPrice() / flight.getNumber_of_tickets();
            double totalPrice = ticketPrice * passengers;

            flightDetails.put("ticketPrice", ticketPrice);
            flightDetails.put("totalPrice", totalPrice);
            results.add(flightDetails);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }


}
