package cat.itacademy.s05.t02.virtualpet.dto;

import cat.itacademy.s05.t02.virtualpet.enums.UserRole;
import cat.itacademy.s05.t02.virtualpet.model.User;

public record UserResponse(
        String id,
        String email,
        UserRole role,
        String selectedPetId
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getSelectedPetId()
        );
    }
}