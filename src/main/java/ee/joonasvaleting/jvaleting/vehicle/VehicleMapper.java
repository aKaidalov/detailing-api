package ee.joonasvaleting.jvaleting.vehicle;

import ee.joonasvaleting.jvaleting.vehicletype.VehicleTypeMapper;

public class VehicleMapper {

    public static VehicleDto fromDbToDto(Vehicle vehicle) {
        return new VehicleDto()
                .id(vehicle.getId())
                .licencePlate(vehicle.getLicencePlate())
                .vinCode(vehicle.getVinCode())
                .vehicleType(VehicleTypeMapper.fromDbToDto(vehicle.getVehicleType()));
    }
}
