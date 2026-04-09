package cz.school.tenniscourtreservation.controller;

import cz.school.tenniscourtreservation.dto.CreateReservationRequest;
import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation createReservation(@Valid @RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request);
    }

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PutMapping("/{id}/cancel")
    public Reservation cancelReservation(@PathVariable Long id) {
        return reservationService.cancelReservationById(id);
    }
}