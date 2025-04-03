package cat.itacademy.s05.t02.virtualpet.auth.service;

import cat.itacademy.s05.t02.virtualpet.auth.dto.AuthResponse;
import cat.itacademy.s05.t02.virtualpet.auth.dto.LoginRequest;
import cat.itacademy.s05.t02.virtualpet.auth.dto.RegisterRequest;
import cat.itacademy.s05.t02.virtualpet.exception.custom.EmailAlreadyExistsException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.UserNotFoundException;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest) throws EmailAlreadyExistsException;
    AuthResponse login(LoginRequest loginRequest) throws UserNotFoundException;
}