package cat.itacademy.s05.t02.virtualpet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String variety;

    @NotBlank
    private String color;
}
