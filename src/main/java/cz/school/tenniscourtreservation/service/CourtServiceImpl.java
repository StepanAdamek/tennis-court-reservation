package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.exception.ResourceNotFoundException;
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

    @Override
    public Court createCourt(Court court) {
        return courtRepository.save(court);
    }

    @Override
    public void deleteCourt(Long id) {
        if (!courtRepository.existsById(id)) {
            throw new ResourceNotFoundException("Court not found with id: " + id);
        }

        courtRepository.deleteById(id);
    }
}