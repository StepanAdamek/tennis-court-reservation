package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;

import java.time.LocalDateTime;

public class ReservationServiceImpl implements ReservationService {

    @Override
    public Reservation createReservation(Reservation reservation) {

        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Reservation cannot be in the past");
        }

        return reservation;
    }
}