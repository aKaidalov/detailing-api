package ee.joonasvaleting.jvaleting.vehicle.type;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RestControllerAdvice
@RequestMapping("/api")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @GetMapping("/vehicle-type/{id}")
    public VehicleTypeDto findById(@PathVariable Long id){
        return vehicleTypeService.findById(id);
    }
}
