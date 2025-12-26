package ee.detailing.api.timeslot;

import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeSlotTemplateDto {

    private Integer id;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isActive;
}
