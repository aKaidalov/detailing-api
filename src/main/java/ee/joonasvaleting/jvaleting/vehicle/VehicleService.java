package ee.joonasvaleting.jvaleting.vehicle;

import ee.joonasvaleting.jvaleting.vehicletype.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public List<VehicleDto> findAllByVehicleTypeName(String typeName) {
        return vehicleRepository.findAllByVehicleTypeName(typeName)
                .stream()
                .map(VehicleMapper::fromDbToDto)
                .toList(); // return immutable list
    }
}
