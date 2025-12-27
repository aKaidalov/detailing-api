package ee.detailing.api.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    // === Public Endpoints ===

    @PostMapping("/api/v1/bookings")
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingCreateRequest request) {
        return ResponseEntity.ok(service.createBooking(request));
    }

    @GetMapping("/api/v1/bookings/{reference}")
    public ResponseEntity<BookingDto> getBookingByReference(@PathVariable String reference) {
        return ResponseEntity.ok(service.getBookingByReference(reference));
    }

    @DeleteMapping("/api/v1/bookings/{reference}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String reference) {
        service.cancelBookingByReference(reference);
        return ResponseEntity.noContent().build();
    }

    // === Admin Endpoints ===

    @GetMapping("/api/v1/admin/bookings")
    public ResponseEntity<List<BookingDto>> getAllBookings() {
        return ResponseEntity.ok(service.getAllBookings());
    }

    @GetMapping("/api/v1/admin/bookings/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getBookingById(id));
    }

    @PutMapping("/api/v1/admin/bookings/{id}")
    public ResponseEntity<BookingDto> updateBooking(
            @PathVariable Integer id,
            @RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(service.updateBooking(id, request));
    }

    @PutMapping("/api/v1/admin/bookings/{id}/status")
    public ResponseEntity<BookingDto> updateBookingStatus(
            @PathVariable Integer id,
            @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateBookingStatus(id, request));
    }

    @DeleteMapping("/api/v1/admin/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        service.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
