package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private final ReservationService reservationService = new ReservationServiceImpl();

    @Test
    void shouldThrowExceptionWhenReservationIsInPast() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now().minusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(InvalidReservationException.class,
                () -> reservationService.createReservation(reservation));
    }
}