package ee.detailing.api.businesssettings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusinessSettingsMapper {

    BusinessSettingsDto toDto(BusinessSettings entity);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(BusinessSettingsDto dto, @MappingTarget BusinessSettings entity);
}
