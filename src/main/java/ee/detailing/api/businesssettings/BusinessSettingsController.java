package ee.detailing.api.businesssettings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BusinessSettingsController {

    private final BusinessSettingsService service;

    // Public endpoint - for landing page
    @GetMapping("/api/v1/business-settings")
    public ResponseEntity<BusinessSettingsDto> getPublicSettings() {
        return ResponseEntity.ok(service.getSettings());
    }

    // Admin endpoints
    @GetMapping("/api/v1/admin/business-settings")
    public ResponseEntity<BusinessSettingsDto> getSettings() {
        return ResponseEntity.ok(service.getSettings());
    }

    @PutMapping("/api/v1/admin/business-settings")
    public ResponseEntity<BusinessSettingsDto> updateSettings(@RequestBody BusinessSettingsDto dto) {
        return ResponseEntity.ok(service.updateSettings(dto));
    }
}
