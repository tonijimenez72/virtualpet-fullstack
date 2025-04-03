package cat.itacademy.s05.t02.virtualpet.model;

import cat.itacademy.s05.t02.virtualpet.enums.Gadget;
import cat.itacademy.s05.t02.virtualpet.enums.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "pets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pet {
    @Id
    private String id;
    private String userId;
    private String variety;
    private String name;
    private String color;
    private int happiness;
    private int energy;
    private int wisdom;
    private Location location;
    private Map<Location, List<Gadget>> gadgetsByLocation;

}