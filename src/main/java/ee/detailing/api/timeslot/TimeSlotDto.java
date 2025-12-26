package ee.detailing.api.timeslot;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimeSlotDto {

    private Integer id;
    private LocalDate date;
    private TimeSlotStatus status;
    private Integer timeSlotTemplateId;

    // Flattened fields from template for convenience
    private LocalTime startTime;
    private LocalTime endTime;
}
