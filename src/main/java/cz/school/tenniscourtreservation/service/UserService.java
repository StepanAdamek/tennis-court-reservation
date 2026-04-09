package cz.school.tenniscourtreservation.service;

import cz.school.tenniscourtreservation.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
}