package cat.itacademy.s05.t02.virtualpet.service.impl;

import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.exception.custom.MinValueException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import cat.itacademy.s05.t02.virtualpet.service.PetRulesService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class PetRulesServiceImpl implements PetRulesService {

    private static final int DEFAULT_VALUE = 1;

    private static final Map<Location, List<Gadget>> allowedGadgets = Map.of(
            Location.HOME, List.of(Gadget.BONE, Gadget.TOY),
            Location.PARK, List.of(Gadget.BONE, Gadget.BALL),
            Location.COUNTRY, List.of(Gadget.STICK),
            Location.BEACH, List.of(Gadget.BALL)
    );

    @Override
    public void validate(Pet pet, PetActivity activity) {
        switch (activity) {
            case PLAY -> validatePlay(pet);
            case FEED -> {}
            case TRAIN -> validateTrain(pet);
            default -> throw new UnsupportedOperationException("Validation not supported for activity: " + activity);
        }
    }

    @Override
    public Pet apply(Pet pet, PetActivity activity) {
        return switch (activity) {
            case PLAY -> applyPlay(pet);
            case FEED -> applyFeed(pet);
            case TRAIN -> applyTrain(pet);
            default -> throw new UnsupportedOperationException("Application not supported for activity: " + activity);
        };
    }

    @Override
    public void validateMove(Pet pet, Location destination) {
        if (pet.getHappiness() <= DEFAULT_VALUE) {
            throw new MinValueException("happiness to move");
        }

        if (pet.getEnergy() == 0 && destination != Location.HOME) {
            String message = pet.getLocation() == Location.HOME
                    ? "energy to leave home"
                    : "no energy to move anywhere but home";
            throw new MinValueException(message);
        }
    }

    @Override
    public Pet applyMove(Pet pet, Location location) {
        pet.setLocation(location);
        pet.setEnergy(pet.getEnergy() - DEFAULT_VALUE);

        List<Gadget> gadgetsInLocation = allowedGadgets.getOrDefault(location, List.of());

        if (pet.getGadgetsByLocation() == null) {
            pet.setGadgetsByLocation(new HashMap<>());
        }

        pet.getGadgetsByLocation().put(location, gadgetsInLocation);

        return pet;
    }



    @Override
    @Cacheable(value = "allowedGadgets", key = "#location")
    public List<Gadget> getAllowedGadgets(Location location) {
        return allowedGadgets.getOrDefault(location, List.of());
    }

    private void validatePlay(Pet pet) {
        if (pet.getEnergy() < DEFAULT_VALUE) {
            throw new MinValueException("energy to play. Please, feed me.");
        }
    }

    private void validateTrain(Pet pet) {
        if (pet.getHappiness() <= DEFAULT_VALUE) {
            throw new MinValueException("happiness to train. Please, let's play.");
        }
    }

    private Pet applyPlay(Pet pet) {
        pet.setHappiness(pet.getHappiness() + DEFAULT_VALUE);
        pet.setEnergy(pet.getEnergy() - DEFAULT_VALUE);
        return pet;
    }

    private Pet applyFeed(Pet pet) {
        pet.setEnergy(pet.getEnergy() + DEFAULT_VALUE);
        return pet;
    }

    private Pet applyTrain(Pet pet) {
        pet.setWisdom(pet.getWisdom() + DEFAULT_VALUE);
        pet.setHappiness(pet.getHappiness() - DEFAULT_VALUE);
        return pet;
    }
}
