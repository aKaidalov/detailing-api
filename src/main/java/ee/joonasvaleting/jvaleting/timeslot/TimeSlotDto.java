package ee.joonasvaleting.jvaleting.timeslot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto {
    private Long id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Boolean isAvailable;
    private Integer maxBookings;
}
