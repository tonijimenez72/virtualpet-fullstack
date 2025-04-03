package cat.itacademy.s05.t02.virtualpet.service.impl;

import cat.itacademy.s05.t02.virtualpet.dto.CurrentUser;
import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.enums.UserRole;
import cat.itacademy.s05.t02.virtualpet.exception.custom.PetNotFoundException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.UnauthorizedPetAccessException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import cat.itacademy.s05.t02.virtualpet.repository.PetRepository;
import cat.itacademy.s05.t02.virtualpet.service.CurrentUserService;
import cat.itacademy.s05.t02.virtualpet.service.PetGameService;
import cat.itacademy.s05.t02.virtualpet.service.PetRulesService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetGameServiceImpl implements PetGameService {

    private static final Logger logger = LoggerFactory.getLogger(PetGameServiceImpl.class);

    private final PetRepository petRepository;
    private final CurrentUserService currentUserService;
    private final PetRulesService petRulesService;

    @Override
    public Pet petAction(String petId, PetActivity activity) {
        Pet pet = getOwnedPet(petId);
        petRulesService.validate(pet, activity);
        Pet updatedPet = petRulesService.apply(pet, activity);

        return petRepository.save(updatedPet);
    }

    @Override
    public Pet moveToLocation(String petId, Location location) {
        Pet pet = getOwnedPet(petId);
        petRulesService.validateMove(pet, location);
        Pet updatedPet = petRulesService.applyMove(pet, location);

        return petRepository.save(updatedPet);
    }

    @Override
    public List<Gadget> getAllowedGadgetsForLocation(Location location) {
        return petRulesService.getAllowedGadgets(location);
    }

    private Pet getOwnedPet(String petId) {
        CurrentUser currentUser = currentUserService.getCurrentUserInfo();
        String userId = currentUser.getUserId();
        UserRole role = currentUser.getRole();

        Pet pet = petRepository.findById(petId)
                .orElseThrow(PetNotFoundException::new);

        String petOwner = pet.getUserId();

        if (!petOwner.equals(userId) && role != UserRole.ADMIN) {
            logger.warn("Unauthorized access attempt by user '{}' with role '{}' to pet '{}'",
                    currentUser.getEmail(), role, petId);
            throw new UnauthorizedPetAccessException();
        }

        return pet;
    }


}