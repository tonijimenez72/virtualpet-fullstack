package cat.itacademy.s05.t02.virtualpet.auth.service.impl;

import cat.itacademy.s05.t02.virtualpet.auth.config.jwt.JwtTokenUtil;
import cat.itacademy.s05.t02.virtualpet.auth.dto.AuthResponse;
import cat.itacademy.s05.t02.virtualpet.auth.dto.LoginRequest;
import cat.itacademy.s05.t02.virtualpet.auth.dto.RegisterRequest;
import cat.itacademy.s05.t02.virtualpet.enums.UserRole;
import cat.itacademy.s05.t02.virtualpet.auth.service.AuthService;
import cat.itacademy.s05.t02.virtualpet.exception.custom.InvalidCredentialsException;
import cat.itacademy.s05.t02.virtualpet.model.User;
import cat.itacademy.s05.t02.virtualpet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = request.getEmail();

        logger.info("Attempt to register user with email: {}", email);
        checkEmailAvailability(email);
        User user = buildNewUser(request);
        return saveAndGenerateToken(user);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        logger.info("Attempt to login with email: {}", email);

        User user = getUserOrThrow(email);
        authenticateOrThrow(email, password);

        return generateAuthToken(user);
    }

    private void checkEmailAvailability(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Registration failed: email '{}' already exists", email);
            throw new RuntimeException("Email already exists");
        }
    }

    private User buildNewUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                .build();
    }

    private AuthResponse saveAndGenerateToken(User user) {
        userRepository.save(user);
        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().name());
        logger.info("User registered successfully: {}", user.getEmail());
        return new AuthResponse(token);
    }

    private User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Login failed: user '{}' not found", email);
                    return new InvalidCredentialsException();
                });
    }

    private void authenticateOrThrow(String email, String password) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException ex) {
            logger.warn("Login failed: bad credentials for '{}'", email);
            throw new InvalidCredentialsException();
        }
    }

    private AuthResponse generateAuthToken(User user) {
        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().name());
        logger.info("Login successful for user: {}", user.getEmail());
        return new AuthResponse(token);
    }
}
