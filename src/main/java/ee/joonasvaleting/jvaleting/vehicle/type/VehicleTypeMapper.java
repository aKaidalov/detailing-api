package ee.joonasvaleting.jvaleting.vehicle.type;

public class VehicleTypeMapper {

    public static VehicleTypeDto fromDbToDto(VehicleType vehicleType) {
        return new VehicleTypeDto(vehicleType.getId(), vehicleType.getTypeName());
    }
}
