package cat.itacademy.s05.t02.virtualpet.dto;

import cat.itacademy.s05.t02.virtualpet.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CurrentUser {
    private String userId;
    private String email;
    private UserRole role;
    private String selectedPetId;
}