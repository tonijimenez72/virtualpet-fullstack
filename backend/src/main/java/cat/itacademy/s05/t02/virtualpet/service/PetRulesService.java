package cat.itacademy.s05.t02.virtualpet.service;

import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import cat.itacademy.s05.t02.virtualpet.exception.custom.MinValueException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;

import java.util.List;

public interface PetRulesService {
    void validate(Pet pet, PetActivity activity) throws MinValueException;
    Pet apply(Pet pet, PetActivity activity);
    void validateMove(Pet pet, Location location) throws MinValueException;
    Pet applyMove(Pet pet, Location location);
    List<Gadget> getAllowedGadgets(Location location);
}
