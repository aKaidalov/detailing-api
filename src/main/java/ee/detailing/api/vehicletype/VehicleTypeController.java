package ee.detailing.api.vehicletype;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService service;

    // Public endpoint - returns only active vehicle types
    @GetMapping("/api/v1/vehicle-types")
    public ResponseEntity<List<VehicleTypeDto>> getActiveVehicleTypes() {
        return ResponseEntity.ok(service.getActiveVehicleTypes());
    }

    // Admin endpoints
    @GetMapping("/api/v1/admin/vehicle-types")
    public ResponseEntity<List<VehicleTypeDto>> getAllVehicleTypes() {
        return ResponseEntity.ok(service.getAllVehicleTypes());
    }

    @PostMapping("/api/v1/admin/vehicle-types")
    public ResponseEntity<VehicleTypeDto> createVehicleType(@RequestBody VehicleTypeDto dto) {
        return ResponseEntity.ok(service.createVehicleType(dto));
    }

    @PutMapping("/api/v1/admin/vehicle-types/{id}")
    public ResponseEntity<VehicleTypeDto> updateVehicleType(@PathVariable Integer id, @RequestBody VehicleTypeDto dto) {
        return ResponseEntity.ok(service.updateVehicleType(id, dto));
    }

    @DeleteMapping("/api/v1/admin/vehicle-types/{id}")
    public ResponseEntity<Void> deleteVehicleType(@PathVariable Integer id) {
        service.deleteVehicleType(id);
        return ResponseEntity.noContent().build();
    }
}
