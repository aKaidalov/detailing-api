package ee.detailing.api.addon;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AddOnController {

    private final AddOnService service;

    // Public endpoint - returns add-ons for a package
    @GetMapping("/api/v1/packages/{packageId}/add-ons")
    public ResponseEntity<List<AddOnDto>> getAddOnsForPackage(@PathVariable Integer packageId) {
        return ResponseEntity.ok(service.getAddOnsForPackage(packageId));
    }

    // Admin endpoints
    @GetMapping("/api/v1/admin/add-ons")
    public ResponseEntity<List<AddOnDto>> getAllAddOns() {
        return ResponseEntity.ok(service.getAllAddOns());
    }

    @PostMapping("/api/v1/admin/add-ons")
    public ResponseEntity<AddOnDto> createAddOn(@RequestBody AddOnDto dto) {
        return ResponseEntity.ok(service.createAddOn(dto));
    }

    @PutMapping("/api/v1/admin/add-ons/{id}")
    public ResponseEntity<AddOnDto> updateAddOn(@PathVariable Integer id, @RequestBody AddOnDto dto) {
        return ResponseEntity.ok(service.updateAddOn(id, dto));
    }

    @DeleteMapping("/api/v1/admin/add-ons/{id}")
    public ResponseEntity<Void> deleteAddOn(@PathVariable Integer id) {
        service.deleteAddOn(id);
        return ResponseEntity.noContent().build();
    }
}
