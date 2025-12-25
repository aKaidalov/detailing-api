package ee.detailing.api.deliverytype;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DeliveryTypeController {

    private final DeliveryTypeService service;

    // Public endpoint - returns only active delivery types
    @GetMapping("/api/v1/delivery-types")
    public ResponseEntity<List<DeliveryTypeDto>> getActiveDeliveryTypes() {
        return ResponseEntity.ok(service.getActiveDeliveryTypes());
    }

    // Admin endpoints
    @GetMapping("/api/v1/admin/delivery-types")
    public ResponseEntity<List<DeliveryTypeDto>> getAllDeliveryTypes() {
        return ResponseEntity.ok(service.getAllDeliveryTypes());
    }

    @PostMapping("/api/v1/admin/delivery-types")
    public ResponseEntity<DeliveryTypeDto> createDeliveryType(@RequestBody DeliveryTypeDto dto) {
        return ResponseEntity.ok(service.createDeliveryType(dto));
    }

    @PutMapping("/api/v1/admin/delivery-types/{id}")
    public ResponseEntity<DeliveryTypeDto> updateDeliveryType(@PathVariable Integer id, @RequestBody DeliveryTypeDto dto) {
        return ResponseEntity.ok(service.updateDeliveryType(id, dto));
    }

    @DeleteMapping("/api/v1/admin/delivery-types/{id}")
    public ResponseEntity<Void> deleteDeliveryType(@PathVariable Integer id) {
        service.deleteDeliveryType(id);
        return ResponseEntity.noContent().build();
    }
}
