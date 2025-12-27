package ee.detailing.api.analytics;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

public enum AnalyticsPeriod {
    DAY,
    WEEK,
    MONTH;

    public LocalDateTime getStartDateTime() {
        LocalDate today = LocalDate.now();
        return switch (this) {
            case DAY -> today.atStartOfDay();
            case WEEK -> today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
            case MONTH -> today.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        };
    }

    public LocalDateTime getEndDateTime() {
        LocalDate today = LocalDate.now();
        return switch (this) {
            case DAY -> today.atTime(LocalTime.MAX);
            case WEEK -> today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).atTime(LocalTime.MAX);
            case MONTH -> today.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        };
    }

    public LocalDate getStartDate() {
        return getStartDateTime().toLocalDate();
    }

    public LocalDate getEndDate() {
        return getEndDateTime().toLocalDate();
    }
}
