package cat.itacademy.s05.t02.virtualpet.service;

import cat.itacademy.s05.t02.virtualpet.exception.custom.*;
import cat.itacademy.s05.t02.virtualpet.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User getUserByEmail(String email) throws UserNotFoundException;
}