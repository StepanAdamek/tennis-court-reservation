package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.repository.ReservationRepository;
import cz.school.tenniscourtreservation.service.ReservationServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import cz.school.tenniscourtreservation.model.Court;
import static org.mockito.Mockito.when;
class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final ReservationService reservationService = new ReservationServiceImpl(reservationRepository);

    @Test
    void shouldThrowExceptionWhenReservationIsInPast() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now().minusHours(1));
        reservation.setEndTime(LocalDateTime.now().plusHours(1));

        assertThrows(InvalidReservationException.class,
                () -> reservationService.createReservation(reservation));
    }

    @Test
    void shouldThrowExceptionWhenReservationOverlapsExistingReservation() {
        Court court = new Court();
        court.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).withHour(11).withMinute(0));

        when(reservationRepository.existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
                1L,
                reservation.getEndTime(),
                reservation.getStartTime()
        )).thenReturn(true);

        assertThrows(InvalidReservationException.class,
                () -> reservationService.createReservation(reservation));
    }
}

