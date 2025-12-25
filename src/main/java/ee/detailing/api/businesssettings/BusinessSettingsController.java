package ee.detailing.api.businesssettings;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/business-settings")
@RequiredArgsConstructor
public class BusinessSettingsController {

    private final BusinessSettingsService service;

    @GetMapping
    public ResponseEntity<BusinessSettingsDto> getSettings() {
        return ResponseEntity.ok(service.getSettings());
    }

    @PutMapping
    public ResponseEntity<BusinessSettingsDto> updateSettings(@RequestBody BusinessSettingsDto dto) {
        return ResponseEntity.ok(service.updateSettings(dto));
    }
}
