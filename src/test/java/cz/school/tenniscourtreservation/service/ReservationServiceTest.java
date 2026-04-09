package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.repository.ReservationRepository;
import cz.school.tenniscourtreservation.service.ReservationServiceImpl;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.model.ReservationStatus;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import cz.school.tenniscourtreservation.repository.UserRepository;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import cz.school.tenniscourtreservation.model.Court;
import static org.mockito.Mockito.when;
class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CourtRepository courtRepository = mock(CourtRepository.class);

    private final ReservationService reservationService =
            new ReservationServiceImpl(reservationRepository, userRepository, courtRepository);

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

    @Test
    void shouldThrowExceptionWhenUserHasMoreThanThreeFutureReservations() {
        User user = new User();
        user.setId(1L);

        Court court = new Court();
        court.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(LocalDateTime.now().plusDays(1));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).plusHours(1));

        when(reservationRepository.existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
                1L,
                reservation.getEndTime(),
                reservation.getStartTime()
        )).thenReturn(false);

        when(reservationRepository.countByUserIdAndStartTimeAfterAndStatus(
                1L,
                reservation.getStartTime(),
                ReservationStatus.CREATED
        )).thenReturn(3L);

        assertThrows(InvalidReservationException.class,
                () -> reservationService.createReservation(reservation));
    }

    @Test
    void shouldCalculateEveningPriceCorrectly() {
        User user = new User();
        user.setId(1L);

        Court court = new Court();
        court.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(LocalDateTime.now().plusDays(1).withHour(18).withMinute(0));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).withHour(19).withMinute(0));

        when(reservationRepository.existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
                1L,
                reservation.getEndTime(),
                reservation.getStartTime()
        )).thenReturn(false);

        when(reservationRepository.countByUserIdAndStartTimeAfterAndStatus(
                1L,
                reservation.getStartTime(),
                ReservationStatus.CREATED
        )).thenReturn(0L);

        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation createdReservation = reservationService.createReservation(reservation);

        assertEquals(new BigDecimal("300"), createdReservation.getTotalPrice());
    }

    @Test
    void shouldThrowExceptionWhenCancellingLessThan24HoursBeforeStart() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now().plusHours(10));
        reservation.setStatus(ReservationStatus.CREATED);

        assertThrows(InvalidReservationException.class,
                () -> reservationService.cancelReservation(reservation));
    }

    @Test
    void shouldCancelReservationWhenMoreThan24HoursBeforeStart() {
        Reservation reservation = new Reservation();
        reservation.setStartTime(LocalDateTime.now().plusDays(2));
        reservation.setStatus(ReservationStatus.CREATED);

        Reservation cancelledReservation = reservationService.cancelReservation(reservation);

        assertEquals(ReservationStatus.CANCELLED, cancelledReservation.getStatus());
    }
}

