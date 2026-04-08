package cz.school.tenniscourtreservation.repository;

import cz.school.tenniscourtreservation.model.Reservation;
import cz.school.tenniscourtreservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByCourtIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long courtId,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    long countByUserIdAndStartTimeAfterAndStatus(
            Long userId,
            LocalDateTime dateTime,
            ReservationStatus status
    );
}