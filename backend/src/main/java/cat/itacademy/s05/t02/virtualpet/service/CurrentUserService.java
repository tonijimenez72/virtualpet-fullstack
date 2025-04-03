package cat.itacademy.s05.t02.virtualpet.service;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.exception.custom.*;

public interface CurrentUserService {
    CurrentUser getCurrentUserInfo() throws UserNotFoundException;
    void clearSelectedPet() throws UserNotFoundException;
}
