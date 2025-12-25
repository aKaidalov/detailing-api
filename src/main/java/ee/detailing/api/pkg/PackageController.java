package ee.detailing.api.pkg;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PackageController {

    private final PackageService service;

    // Public endpoint - returns packages for a vehicle type
    @GetMapping("/api/v1/vehicle-types/{vehicleTypeId}/packages")
    public ResponseEntity<List<PackageDto>> getPackagesForVehicleType(@PathVariable Integer vehicleTypeId) {
        return ResponseEntity.ok(service.getPackagesForVehicleType(vehicleTypeId));
    }

    // Admin endpoints
    @GetMapping("/api/v1/admin/packages")
    public ResponseEntity<List<PackageDto>> getAllPackages() {
        return ResponseEntity.ok(service.getAllPackages());
    }

    @PostMapping("/api/v1/admin/packages")
    public ResponseEntity<PackageDto> createPackage(@RequestBody PackageDto dto) {
        return ResponseEntity.ok(service.createPackage(dto));
    }

    @PutMapping("/api/v1/admin/packages/{id}")
    public ResponseEntity<PackageDto> updatePackage(@PathVariable Integer id, @RequestBody PackageDto dto) {
        return ResponseEntity.ok(service.updatePackage(id, dto));
    }

    @DeleteMapping("/api/v1/admin/packages/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable Integer id) {
        service.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}
