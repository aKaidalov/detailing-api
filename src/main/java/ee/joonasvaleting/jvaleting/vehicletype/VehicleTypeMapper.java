package ee.joonasvaleting.jvaleting.vehicletype;

public class VehicleTypeMapper {

    public static VehicleTypeDto fromDbToDto(VehicleType vehicleType) {
        return new VehicleTypeDto(vehicleType.getId(), vehicleType.getTypeName());
    }
}
