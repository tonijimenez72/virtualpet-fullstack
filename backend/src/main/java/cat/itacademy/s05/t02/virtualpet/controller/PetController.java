package cat.itacademy.s05.t02.virtualpet.controller;

import cat.itacademy.s05.t02.virtualpet.dto.PetRequest;
import cat.itacademy.s05.t02.virtualpet.dto.PetResponse;
import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import cat.itacademy.s05.t02.virtualpet.service.PetGameService;
import cat.itacademy.s05.t02.virtualpet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Operations related to virtual pets")
public class PetController {

    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;
    private final PetGameService petGameService;

    @Operation(summary = "Create new pet", description = "Creates a pet for the current user.")
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetResponse> createPet(@RequestBody PetRequest petRequest) {
        Pet pet = petService.createPet(petRequest);
        return ResponseEntity.ok(PetResponse.from(pet));
    }

    @Operation(summary = "Get all pets", description = "Admin only: fetches all pets with pagination.")
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PetResponse>> getAllPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pet> petsPage = petService.getAllPets(pageable);
        Page<PetResponse> responsePage = petsPage.map(PetResponse::from);
        return ResponseEntity.ok(responsePage);
    }

    @Operation(summary = "Get my pets", description = "Returns pets owned by the current user.")
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PetResponse>> getMyPets() {
        return ResponseEntity.ok(petService.getPetsForCurrentUser().stream()
                .map(PetResponse::from)
                .toList());
    }

    @Operation(summary = "Get pet by ID", description = "Fetches pet by ID.")
    @GetMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetResponse> getPetById(@PathVariable String petId) {
        Pet pet = petService.getPetById(petId);
        return ResponseEntity.ok(PetResponse.from(pet));
    }

    @Operation(summary = "Get pets by user ID", description = "Admin only: fetches pets for given user ID.")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PetResponse>> getPetsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(petService.getPetsByUserId(userId).stream()
                .map(PetResponse::from)
                .toList());
    }

    @Operation(summary = "Delete pet by ID", description = "Deletes a pet owned by the current user.")
    @DeleteMapping("/{petId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable String petId) {
        petService.deletePetById(petId);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Perform activity on a pet", description = "Performs an activity (play, feed, train) on the pet.")
    @PutMapping("/{petId}/activity")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetResponse> performActivity(
            @PathVariable String petId,
            @RequestParam PetActivity activity
    ) {
        Pet pet = petGameService.petAction(petId, activity);
        return ResponseEntity.ok(PetResponse.from(pet));
    }

    @Operation(summary = "Move pet to a location", description = "Move a pet to a new location.")
    @PutMapping("/{petId}/move")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetResponse> move(@PathVariable String petId, @RequestParam Location location) {
        Pet pet = petGameService.moveToLocation(petId, location);
        return ResponseEntity.ok(PetResponse.from(pet));
    }

    @Operation(summary = "Get allowed gadgets for a location", description = "Returns the valid gadgets for a given location.")
    @GetMapping("/gadgets")
    public ResponseEntity<List<Gadget>> getGadgetsForLocation(@RequestParam Location location) {
        return ResponseEntity.ok(petGameService.getAllowedGadgetsForLocation(location));
    }
}