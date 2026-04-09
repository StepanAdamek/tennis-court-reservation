package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.model.Court;
import cz.school.tenniscourtreservation.repository.CourtRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    public CourtServiceImpl(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    @Override
    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }
}