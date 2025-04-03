package cat.itacademy.s05.t02.virtualpet.service;

import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.exception.custom.MinValueException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.PetNotFoundException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.UnauthorizedPetAccessException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;

import java.util.List;

public interface PetGameService {
    Pet petAction(String petId, PetActivity activity) throws PetNotFoundException, UnauthorizedPetAccessException, MinValueException;
    Pet moveToLocation(String petId, Location location) throws PetNotFoundException, UnauthorizedPetAccessException, MinValueException;
    List<Gadget> getAllowedGadgetsForLocation(Location location);
}
