package xws.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xws.model.Flight;

public interface FlightRepository  extends MongoRepository<Flight, String> {
}
