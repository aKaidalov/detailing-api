package ee.detailing.api.analytics;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RevenueAnalyticsDto {
    private String period;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
    private Long completedBookings;
    private BigDecimal averageOrderValue;
}
