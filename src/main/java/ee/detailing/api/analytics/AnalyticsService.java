package ee.detailing.api.analytics;

import ee.detailing.api.booking.BookingRepository;
import ee.detailing.api.booking.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final BookingRepository bookingRepository;

    @Transactional(readOnly = true)
    public BookingAnalyticsDto getBookingAnalytics(AnalyticsPeriod period) {
        LocalDateTime start = period.getStartDateTime();
        LocalDateTime end = period.getEndDateTime();

        Long totalBookings = bookingRepository.countByCreatedAtBetween(start, end);
        Long pendingCount = bookingRepository.countByStatusAndCreatedAtBetween(BookingStatus.PENDING, start, end);
        Long confirmedCount = bookingRepository.countByStatusAndCreatedAtBetween(BookingStatus.CONFIRMED, start, end);
        Long completedCount = bookingRepository.countByStatusAndCreatedAtBetween(BookingStatus.COMPLETED, start, end);
        Long cancelledByCustomer = bookingRepository.countByStatusAndCreatedAtBetween(BookingStatus.CANCELLED_BY_CUSTOMER, start, end);
        Long cancelledByAdmin = bookingRepository.countByStatusAndCreatedAtBetween(BookingStatus.CANCELLED_BY_ADMIN, start, end);

        return BookingAnalyticsDto.builder()
                .period(period.name().toLowerCase())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .totalBookings(totalBookings != null ? totalBookings : 0L)
                .pendingCount(pendingCount != null ? pendingCount : 0L)
                .confirmedCount(confirmedCount != null ? confirmedCount : 0L)
                .completedCount(completedCount != null ? completedCount : 0L)
                .cancelledCount((cancelledByCustomer != null ? cancelledByCustomer : 0L) +
                               (cancelledByAdmin != null ? cancelledByAdmin : 0L))
                .build();
    }

    @Transactional(readOnly = true)
    public RevenueAnalyticsDto getRevenueAnalytics(AnalyticsPeriod period) {
        LocalDateTime start = period.getStartDateTime();
        LocalDateTime end = period.getEndDateTime();

        BigDecimal totalRevenue = bookingRepository.sumTotalPriceByStatusAndCreatedAtBetween(
                BookingStatus.COMPLETED, start, end);
        Long completedBookings = bookingRepository.countByStatusAndCreatedAtBetween(
                BookingStatus.COMPLETED, start, end);

        BigDecimal averageOrderValue = BigDecimal.ZERO;
        if (completedBookings != null && completedBookings > 0 && totalRevenue != null) {
            averageOrderValue = totalRevenue.divide(
                    BigDecimal.valueOf(completedBookings), 2, RoundingMode.HALF_UP);
        }

        return RevenueAnalyticsDto.builder()
                .period(period.name().toLowerCase())
                .startDate(period.getStartDate())
                .endDate(period.getEndDate())
                .totalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO)
                .completedBookings(completedBookings != null ? completedBookings : 0L)
                .averageOrderValue(averageOrderValue)
                .build();
    }
}
