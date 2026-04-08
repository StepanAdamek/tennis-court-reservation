package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.repository.ReservationRepository;

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

        return reservationRepository.save(reservation);
    }
}