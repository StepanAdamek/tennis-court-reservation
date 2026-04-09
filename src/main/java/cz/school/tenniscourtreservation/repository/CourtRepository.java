package cz.school.tenniscourtreservation.repository;

import cz.school.tenniscourtreservation.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}