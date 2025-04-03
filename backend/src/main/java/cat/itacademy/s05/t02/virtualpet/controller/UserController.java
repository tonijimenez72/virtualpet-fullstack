package cat.itacademy.s05.t02.virtualpet.controller;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.dto.UserResponse;
import cat.itacademy.s05.t02.virtualpet.model.User;
import cat.itacademy.s05.t02.virtualpet.service.CurrentUserService;
import cat.itacademy.s05.t02.virtualpet.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations related to user profile")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile.")
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CurrentUser> getCurrentUser() {
        return ResponseEntity.ok(currentUserService.getCurrentUserInfo());
    }

    @Operation(summary = "Clear selected pet", description = "Removes the currently selected pet for the user.")
    @PutMapping("/clear-selected-pet")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> clearSelectedPet() {
        currentUserService.clearSelectedPet();
        return ResponseEntity.ok("Selected pet cleared.");
    }

    @Operation(summary = "Get all users", description = "Admin only: fetches all users.")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> response = users.stream()
                .map(UserResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }
}