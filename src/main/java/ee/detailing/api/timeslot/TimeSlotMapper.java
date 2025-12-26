package ee.detailing.api.timeslot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {

    @Mapping(source = "timeSlotTemplate.id", target = "timeSlotTemplateId")
    @Mapping(source = "timeSlotTemplate.startTime", target = "startTime")
    @Mapping(source = "timeSlotTemplate.endTime", target = "endTime")
    TimeSlotDto toDto(TimeSlot entity);

    List<TimeSlotDto> toDtoList(List<TimeSlot> entities);

    @Mapping(target = "timeSlotTemplate", ignore = true)
    TimeSlot toEntity(TimeSlotDto dto);

    @Mapping(target = "timeSlotTemplate", ignore = true)
    void updateEntityFromDto(TimeSlotDto dto, @MappingTarget TimeSlot entity);
}
