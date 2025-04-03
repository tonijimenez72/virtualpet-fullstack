package cat.itacademy.s05.t02.virtualpet.controller;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.dto.UserResponse;
import cat.itacademy.s05.t02.virtualpet.enums.UserRole;
import cat.itacademy.s05.t02.virtualpet.model.User;
import cat.itacademy.s05.t02.virtualpet.service.CurrentUserService;
import cat.itacademy.s05.t02.virtualpet.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {UserController.class, UserControllerTest.MockConfig.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private UserService userService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CurrentUserService currentUserService() {
            return Mockito.mock(CurrentUserService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        CurrentUser currentUser = CurrentUser.builder()
                .userId("user123")
                .email("user@example.com")
                .role(UserRole.USER)
                .selectedPetId("pet123")
                .build();

        when(currentUserService.getCurrentUserInfo()).thenReturn(currentUser);

        mockMvc.perform(get("/user/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.selectedPetId").value("pet123"));
    }

    @Test
    public void testClearSelectedPet() throws Exception {
        doNothing().when(currentUserService).clearSelectedPet();

        mockMvc.perform(put("/user/clear-selected-pet")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Selected pet cleared."));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId("user1");
        user1.setEmail("user1@example.com");
        user1.setRole(UserRole.USER);
        user1.setSelectedPetId("pet1");

        User user2 = new User();
        user2.setId("user2");
        user2.setEmail("user2@example.com");
        user2.setRole(UserRole.ADMIN);
        user2.setSelectedPetId("pet2");

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[0].selectedPetId").value("pet1"))
                .andExpect(jsonPath("$[1].id").value("user2"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"))
                .andExpect(jsonPath("$[1].selectedPetId").value("pet2"));
    }
}