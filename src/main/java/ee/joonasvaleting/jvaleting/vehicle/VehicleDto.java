package ee.joonasvaleting.jvaleting.vehicle;

import ee.joonasvaleting.jvaleting.vehicletype.VehicleTypeDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleDto {
    private Long id;
    private String licencePlate;
    private String vinCode;
    private VehicleTypeDto vehicleType;

    public VehicleDto id(Long id) {
        this.id = id;
        return this;
    }

    public VehicleDto licencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
        return this;
    }

    public VehicleDto vinCode(String vinCode) {
        this.vinCode = vinCode;
        return this;
    }

    public VehicleDto vehicleType(VehicleTypeDto vehicleTypeDto) {
        this.vehicleType = vehicleTypeDto;
        return this;
    }
}
