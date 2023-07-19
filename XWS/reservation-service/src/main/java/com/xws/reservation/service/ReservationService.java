package com.xws.reservation.service;


import com.xws.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService{
    @Autowired
    ReservationRepository reservationRepository;
}