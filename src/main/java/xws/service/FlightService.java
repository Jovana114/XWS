package xws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xws.repository.FlightRepository;


@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

}
