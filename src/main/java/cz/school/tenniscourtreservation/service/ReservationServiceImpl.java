package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.model.ReservationStatus;
import cz.school.tenniscourtreservation.repository.ReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        validateReservationNotInPast(reservation);
        validateNoOverlap(reservation);
        validateUserFutureReservationLimit(reservation);

        reservation.setTotalPrice(calculatePrice(reservation));
        reservation.setStatus(ReservationStatus.CREATED);

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation cancelReservation(Reservation reservation) {
        if (reservation.getStartTime().isBefore(LocalDateTime.now().plusHours(24))) {
            throw new InvalidReservationException("Reservation can only be cancelled at least 24 hours before start");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservation;
    }

    private void validateReservationNotInPast(Reservation reservation) {
        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Reservation cannot be in the past");
        }
    }

    private void validateNoOverlap(Reservation reservation) {
        boolean overlaps = reservationRepository.existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
                reservation.getCourt().getId(),
                reservation.getEndTime(),
                reservation.getStartTime()
        );

        if (overlaps) {
            throw new InvalidReservationException("Reservation overlaps with an existing reservation");
        }
    }

    private void validateUserFutureReservationLimit(Reservation reservation) {
        long activeFutureReservations = reservationRepository.countByUserIdAndStartTimeAfterAndStatus(
                reservation.getUser().getId(),
                reservation.getStartTime(),
                ReservationStatus.CREATED
        );

        if (activeFutureReservations >= 3) {
            throw new InvalidReservationException("User cannot have more than 3 active future reservations");
        }
    }

    private BigDecimal calculatePrice(Reservation reservation) {
        return reservation.getStartTime().getHour() >= 17
                ? new BigDecimal("300")
                : new BigDecimal("200");
    }
}