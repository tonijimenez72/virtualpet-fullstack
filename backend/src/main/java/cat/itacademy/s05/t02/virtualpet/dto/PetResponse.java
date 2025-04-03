package cat.itacademy.s05.t02.virtualpet.dto;

import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.model.Pet;

import java.util.List;
import java.util.Map;

public record PetResponse(
        String id,
        String name,
        String variety,
        String color,
        int happiness,
        int energy,
        int wisdom,
        Location location,
        String userId,
        Map<Location, List<Gadget>> gadgetsByLocation
) {
    public static PetResponse from(Pet pet) {
        return new PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getVariety(),
                pet.getColor(),
                pet.getHappiness(),
                pet.getEnergy(),
                pet.getWisdom(),
                pet.getLocation(),
                pet.getUserId(),
                pet.getGadgetsByLocation()
        );
    }
}
