package ee.joonasvaleting.jvaleting.vehicle.type;

import ee.joonasvaleting.jvaleting.common.exceptions.VehicleNotFoudException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeDto findById(Long id) {

        VehicleType vehicle = vehicleTypeRepository
                .findById(id)
                .orElseThrow(() -> new VehicleNotFoudException("Vehicle type with id " + id + " not found"));

        return VehicleTypeMapper.fromDbToDto(vehicle);
    }
}
