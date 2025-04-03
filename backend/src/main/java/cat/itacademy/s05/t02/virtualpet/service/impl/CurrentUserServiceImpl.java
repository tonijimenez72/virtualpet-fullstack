package cat.itacademy.s05.t02.virtualpet.service.impl;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.exception.custom.*;
import cat.itacademy.s05.t02.virtualpet.model.User;
import cat.itacademy.s05.t02.virtualpet.repository.UserRepository;
import cat.itacademy.s05.t02.virtualpet.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private static final Logger logger = LoggerFactory.getLogger(CurrentUserServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "currentUserInfo", key = "#root.methodName + T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
    public CurrentUser getCurrentUserInfo() {
        User user = getCurrentUser();

        return CurrentUser.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .selectedPetId(user.getSelectedPetId())
                .build();
    }

    @Override
    public void clearSelectedPet() {
        User user = getCurrentUser();
        user.setSelectedPetId(null);
        userRepository.save(user);
        logger.info("Cleared selected pet for user '{}'", user.getId());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.debug("Fetching current user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User with email '{}' not found", email);
                    return new UserNotFoundException();
                });
    }
}