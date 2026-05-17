package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.model.Court;

import java.util.List;

public interface CourtService {

    List<Court> getAllCourts();

    Court createCourt(Court court);

    void deleteCourt(Long id);
}