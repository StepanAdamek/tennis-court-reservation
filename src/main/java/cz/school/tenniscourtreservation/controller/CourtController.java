package cz.school.tenniscourtreservation.controller;

import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

    @GetMapping
    public List<Court> getAllCourts() {
        return courtService.getAllCourts();
    }

    @PostMapping
    public ResponseEntity<Court> createCourt(@RequestBody Court court) {
        Court createdCourt = courtService.createCourt(court);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        courtService.deleteCourt(id);
        return ResponseEntity.noContent().build();
    }
}