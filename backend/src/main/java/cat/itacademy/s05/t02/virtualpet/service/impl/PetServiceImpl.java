package cat.itacademy.s05.t02.virtualpet.service.impl;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.dto.PetRequest;
import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.exception.custom.PetNotFoundException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.UnauthorizedPetAccessException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import cat.itacademy.s05.t02.virtualpet.repository.PetRepository;
import cat.itacademy.s05.t02.virtualpet.service.CurrentUserService;
import cat.itacademy.s05.t02.virtualpet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private static final Logger logger = LoggerFactory.getLogger(PetServiceImpl.class);

    private static final int DEFAULT_HAPPINESS = 5;
    private static final int DEFAULT_ENERGY = 5;
    private static final int DEFAULT_WISDOM = 0;

    private final PetRepository petRepository;
    private final CurrentUserService currentUserService;

    @Override
    public Pet createPet(PetRequest petRequest) {
        CurrentUser currentUser = currentUserService.getCurrentUserInfo();
        logger.info("Creating new pet for user: {}", currentUser.getEmail());

        Map<Location, List<Gadget>> initialGadgets = Map.of(
                Location.HOME, List.of(Gadget.BONE, Gadget.TOY)
        );

        Pet pet = Pet.builder()
                .name(petRequest.getName())
                .variety(petRequest.getVariety())
                .color(petRequest.getColor())
                .userId(currentUser.getUserId())
                .happiness(DEFAULT_HAPPINESS)
                .energy(DEFAULT_ENERGY)
                .wisdom(DEFAULT_WISDOM)
                .gadgetsByLocation(Map.of(Location.HOME, List.of()))
                .location(Location.HOME)
                .build();


        Pet savedPet = petRepository.save(pet);
        logger.debug("Pet created: {}", savedPet);

        return savedPet;
    }

    @Override
    public Page<Pet> getAllPets(Pageable pageable) {
        logger.info("Fetching all pets (ADMIN access) with pagination");
        return petRepository.findAll(pageable);
    }


    @Override
    public List<Pet> getPetsForCurrentUser() {
        String userId = currentUserService.getCurrentUserInfo().getUserId();
        logger.info("Fetching pets for current user ID: {}", userId);
        return petRepository.findByUserId(userId);
    }

    @Override
    @Cacheable(value = "petsByUserId", key = "#userId")
    public List<Pet> getPetsByUserId(String userId) {
        logger.info("Fetching pets for user ID: {}", userId);
        return petRepository.findByUserId(userId);
    }

    @Override
    @Cacheable(value = "petById", key = "#petId")
    public Pet getPetById(String petId) {
        logger.info("Fetching pet with ID: {}", petId);
        return petRepository.findById(petId)
                .orElseThrow(() -> {
                    logger.error("Pet with ID {} not found", petId);
                    return new PetNotFoundException();
                });
    }

    @Override
    public void deletePetById(String petId) {
        Pet pet = getPetById(petId);
        CurrentUser currentUser = currentUserService.getCurrentUserInfo();

        boolean isOwner = pet.getUserId().equals(currentUser.getUserId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            logger.warn("Unauthorized delete attempt by user '{}' with role '{}' to pet '{}'",
                    currentUser.getEmail(), currentUser.getRole(), petId);
            throw new UnauthorizedPetAccessException();
        }

        logger.info("Deleting pet with ID: {}", petId);
        petRepository.deleteById(petId);
    }


}