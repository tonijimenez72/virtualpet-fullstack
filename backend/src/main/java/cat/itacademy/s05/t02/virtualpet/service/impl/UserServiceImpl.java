package cat.itacademy.s05.t02.virtualpet.service.impl;

import cat.itacademy.s05.t02.virtualpet.exception.custom.UserNotFoundException;
import cat.itacademy.s05.t02.virtualpet.model.User;
import cat.itacademy.s05.t02.virtualpet.repository.UserRepository;
import cat.itacademy.s05.t02.virtualpet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        logger.info("Fetching all users (admin only)");
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "usersByEmail", key = "#email")
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User with email '{}' not found", email);
                    return new UserNotFoundException();
                });
    }
}