package ee.detailing.api.timeslot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimeSlotTemplateMapper {

    TimeSlotTemplateDto toDto(TimeSlotTemplate entity);

    List<TimeSlotTemplateDto> toDtoList(List<TimeSlotTemplate> entities);

    TimeSlotTemplate toEntity(TimeSlotTemplateDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TimeSlotTemplateDto dto, @MappingTarget TimeSlotTemplate entity);
}
