package ee.joonasvaleting.jvaleting.timeslot;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {
    TimeSlotDto fromDbToDto(TimeSlot timeSlot);
    TimeSlot fromDtoToDb(TimeSlotDto timeSlotDto);
}
