package ee.detailing.api.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService service;

    @GetMapping("/bookings")
    public ResponseEntity<BookingAnalyticsDto> getBookingAnalytics(
            @RequestParam(defaultValue = "DAY") AnalyticsPeriod period) {
        return ResponseEntity.ok(service.getBookingAnalytics(period));
    }

    @GetMapping("/revenue")
    public ResponseEntity<RevenueAnalyticsDto> getRevenueAnalytics(
            @RequestParam(defaultValue = "DAY") AnalyticsPeriod period) {
        return ResponseEntity.ok(service.getRevenueAnalytics(period));
    }
}
