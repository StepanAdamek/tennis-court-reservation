package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.dto.CreateReservationRequest;
import cz.school.tenniscourtreservation.model.Reservation;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    Reservation createReservation(CreateReservationRequest request);

    Reservation cancelReservation(Reservation reservation);
}