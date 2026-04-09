package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.dto.CreateReservationRequest;
import cz.school.tenniscourtreservation.exception.InvalidReservationException;
import cz.school.tenniscourtreservation.exception.ResourceNotFoundException;
import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.model.ReservationStatus;
import cz.school.tenniscourtreservation.model.User;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import cz.school.tenniscourtreservation.repository.ReservationRepository;
import cz.school.tenniscourtreservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserRepository userRepository,
                                  CourtRepository courtRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.courtRepository = courtRepository;
    }

    @Override
    public Reservation createReservation(CreateReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCourt(court);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());

        return createReservation(reservation);
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

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}