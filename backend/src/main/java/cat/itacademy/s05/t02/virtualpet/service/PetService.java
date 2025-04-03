package cat.itacademy.s05.t02.virtualpet.service;

import cat.itacademy.s05.t02.virtualpet.dto.PetRequest;
import cat.itacademy.s05.t02.virtualpet.exception.custom.PetNotFoundException;
import cat.itacademy.s05.t02.virtualpet.exception.custom.UnauthorizedPetAccessException;
import cat.itacademy.s05.t02.virtualpet.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PetService {
    Pet createPet(PetRequest petRequest);
    Page<Pet> getAllPets(Pageable pageable);
    List<Pet> getPetsForCurrentUser();
    List<Pet> getPetsByUserId(String userId);
    void deletePetById(String petId) throws UnauthorizedPetAccessException;
    Pet getPetById(String petId) throws PetNotFoundException;
}