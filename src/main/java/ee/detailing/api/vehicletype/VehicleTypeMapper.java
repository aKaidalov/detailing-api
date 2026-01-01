package ee.detailing.api.vehicletype;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleTypeMapper {

    VehicleTypeDto toDto(VehicleType entity);

    List<VehicleTypeDto> toDtoList(List<VehicleType> entities);

    VehicleType toEntity(VehicleTypeDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(VehicleTypeDto dto, @MappingTarget VehicleType entity);
}
