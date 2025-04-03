package cat.itacademy.s05.t02.virtualpet.dto;

import cat.itacademy.s05.t02.virtualpet.enums.PetActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PetActionRequest {
    private String petId;
    private PetActivity activity;
}