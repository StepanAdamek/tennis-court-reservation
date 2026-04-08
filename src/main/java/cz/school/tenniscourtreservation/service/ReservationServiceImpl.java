package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.repository.ReservationRepository;
import cz.school.tenniscourtreservation.model.ReservationStatus;
import java.math.BigDecimal;

import java.time.LocalDateTime;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {

        if (reservation.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidReservationException("Reservation cannot be in the past");
        }

        boolean overlaps = reservationRepository.existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
                reservation.getCourt().getId(),
                reservation.getEndTime(),
                reservation.getStartTime()
        );

        if (overlaps) {
            throw new InvalidReservationException("Reservation overlaps with an existing reservation");
        }

        long activeFutureReservations = reservationRepository.countByUserIdAndStartTimeAfterAndStatus(
                reservation.getUser().getId(),
                reservation.getStartTime(),
                ReservationStatus.CREATED
        );

        if (activeFutureReservations >= 3) {
            throw new InvalidReservationException("User cannot have more than 3 active future reservations");
        }

        BigDecimal pricePerHour = reservation.getStartTime().getHour() >= 17
                ? new BigDecimal("300")
                : new BigDecimal("200");

        reservation.setTotalPrice(pricePerHour);

        return reservationRepository.save(reservation);
    }
}