package ee.detailing.api.timeslot;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService service;

    @GetMapping("/api/v1/time-slots")
    public ResponseEntity<List<TimeSlotDto>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getAvailableSlots(date));
    }

    @GetMapping("/api/v1/admin/time-slots")
    public ResponseEntity<List<TimeSlotDto>> getSlotsByDateRange(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(service.getSlotsByDateRange(from, to));
    }

    @PostMapping("/api/v1/admin/time-slots")
    public ResponseEntity<TimeSlotDto> createSlot(@RequestBody TimeSlotDto dto) {
        return ResponseEntity.ok(service.createSlot(dto));
    }

    @PutMapping("/api/v1/admin/time-slots/{id}")
    public ResponseEntity<TimeSlotDto> updateSlot(
            @PathVariable Integer id,
            @RequestBody TimeSlotDto dto) {
        return ResponseEntity.ok(service.updateSlot(id, dto));
    }

    @DeleteMapping("/api/v1/admin/time-slots/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Integer id) {
        service.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
