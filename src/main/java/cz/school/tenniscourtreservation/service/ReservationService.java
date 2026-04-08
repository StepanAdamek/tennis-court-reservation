package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.model.Reservation;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    Reservation cancelReservation(Reservation reservation);
}