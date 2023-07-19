package com.example.Accommodationservice.repository;

import com.example.Accommodationservice.model.Appointments;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<Appointments, String> {
}
