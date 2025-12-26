package ee.detailing.api.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeSlotTemplateController {

    private final TimeSlotTemplateService service;

    @GetMapping("/api/v1/admin/time-slot-templates")
    public ResponseEntity<List<TimeSlotTemplateDto>> getAllTemplates() {
        return ResponseEntity.ok(service.getAllTemplates());
    }

    @PostMapping("/api/v1/admin/time-slot-templates")
    public ResponseEntity<TimeSlotTemplateDto> createTemplate(@RequestBody TimeSlotTemplateDto dto) {
        return ResponseEntity.ok(service.createTemplate(dto));
    }

    @PutMapping("/api/v1/admin/time-slot-templates/{id}")
    public ResponseEntity<TimeSlotTemplateDto> updateTemplate(
            @PathVariable Integer id,
            @RequestBody TimeSlotTemplateDto dto) {
        return ResponseEntity.ok(service.updateTemplate(id, dto));
    }

    @DeleteMapping("/api/v1/admin/time-slot-templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Integer id) {
        service.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
