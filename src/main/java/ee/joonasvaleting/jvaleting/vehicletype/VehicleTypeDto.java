package ee.joonasvaleting.jvaleting.vehicletype;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleTypeDto {
    private Long id;
    private String typeName;
}
