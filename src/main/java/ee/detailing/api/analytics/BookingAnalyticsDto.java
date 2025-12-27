package ee.detailing.api.analytics;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingAnalyticsDto {
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalBookings;
    private Long pendingCount;
    private Long confirmedCount;
    private Long completedCount;
    private Long cancelledCount;
}
