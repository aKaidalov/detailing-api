package ee.joonasvaleting.jvaleting.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping("/vehicles")
    public List<VehicleDto> getAllVehiclesByVehicleTypeName(@RequestParam String typeName) {
        return vehicleService.findAllByVehicleTypeName(typeName);
    }
}
