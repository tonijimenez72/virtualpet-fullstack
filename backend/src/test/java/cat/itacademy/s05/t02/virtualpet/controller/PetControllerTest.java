package cat.itacademy.s05.t02.virtualpet.controller;

import cat.itacademy.s05.t02.virtualpet.dto.PetRequest;
import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import cat.itacademy.s05.t02.virtualpet.service.PetGameService;
import cat.itacademy.s05.t02.virtualpet.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PetController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class
        })
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {PetController.class, PetControllerTest.MockConfig.class})
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private PetGameService petGameService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PetService petService() {
            return Mockito.mock(PetService.class);
        }

        @Bean
        public PetGameService petGameService() {
            return Mockito.mock(PetGameService.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    private Pet buildMockPet(String id, String userId, String name) {
        return Pet.builder()
                .id(id)
                .userId(userId)
                .name(name)
                .variety("Cat")
                .color("White")
                .happiness(100)
                .energy(80)
                .wisdom(50)
                .location(Location.HOME)
                .gadgetsByLocation(Map.of(Location.HOME, List.of(Gadget.BONE, Gadget.TOY)))
                .build();
    }

    @Test
    public void testCreatePet() throws Exception {
        PetRequest petRequest = new PetRequest("Fluffy", "Cat", "White");

        Pet pet = buildMockPet("pet1", "user123", "Fluffy");

        when(petService.createPet(petRequest)).thenReturn(pet);

        mockMvc.perform(post("/pet/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("pet1")))
                .andExpect(jsonPath("$.name", is("Fluffy")));
    }

    @Test
    public void testGetAllPetsWithPagination() throws Exception {
        List<Pet> pets = Arrays.asList(
                buildMockPet("pet1", "user1", "Fluffy"),
                buildMockPet("pet2", "user2", "Buddy")
        );

        Page<Pet> page = new PageImpl<>(pets, PageRequest.of(0, 10), pets.size());
        when(petService.getAllPets(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/pet/all")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)));
    }

    @Test
    public void testGetMyPets() throws Exception {
        when(petService.getPetsForCurrentUser()).thenReturn(List.of(buildMockPet("pet1", "user123", "Fluffy")));

        mockMvc.perform(get("/pet/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("pet1")));
    }

    @Test
    public void testGetPetById() throws Exception {
        when(petService.getPetById("pet1")).thenReturn(buildMockPet("pet1", "user123", "Fluffy"));

        mockMvc.perform(get("/pet/{petId}", "pet1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("pet1")))
                .andExpect(jsonPath("$.name", is("Fluffy")));
    }

    @Test
    public void testGetPetsByUserId() throws Exception {
        when(petService.getPetsByUserId("user1")).thenReturn(List.of(buildMockPet("pet1", "user1", "Fluffy")));

        mockMvc.perform(get("/pet/user/{userId}", "user1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("pet1")));
    }

    @Test
    public void testDeletePet() throws Exception {
        doNothing().when(petService).deletePetById("pet1");

        mockMvc.perform(delete("/pet/{petId}", "pet1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testPerformActivity() throws Exception {
        when(petGameService.petAction("pet1", PetActivity.PLAY))
                .thenReturn(buildMockPet("pet1", "user123", "Fluffy"));

        mockMvc.perform(put("/pet/{petId}/activity", "pet1")
                        .param("activity", "PLAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("pet1")));
    }

    @Test
    public void testMove() throws Exception {
        when(petGameService.moveToLocation("pet1", Location.HOME))
                .thenReturn(buildMockPet("pet1", "user123", "Fluffy"));

        mockMvc.perform(put("/pet/{petId}/move", "pet1")
                        .param("location", "HOME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("pet1")));
    }

    @Test
    public void testGetGadgetsForLocation() throws Exception {
        List<Gadget> gadgets = List.of(Gadget.TOY, Gadget.BONE);
        when(petGameService.getAllowedGadgetsForLocation(Location.HOME)).thenReturn(gadgets);

        mockMvc.perform(get("/pet/gadgets")
                        .param("location", "HOME"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("TOY")))
                .andExpect(jsonPath("$[1]", is("BONE")));
    }
}
