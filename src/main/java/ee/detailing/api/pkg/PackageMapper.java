package ee.detailing.api.pkg;

import ee.detailing.api.vehicletype.VehicleType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    @Mapping(target = "vehicleTypeIds", source = "vehicleTypes", qualifiedByName = "vehicleTypesToIds")
    PackageDto toDto(Package entity);

    List<PackageDto> toDtoList(List<Package> entities);

    @Mapping(target = "vehicleTypes", ignore = true)
    Package toEntity(PackageDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicleTypes", ignore = true)
    void updateEntityFromDto(PackageDto dto, @MappingTarget Package entity);

    @Named("vehicleTypesToIds")
    default List<Integer> vehicleTypesToIds(Set<VehicleType> vehicleTypes) {
        if (vehicleTypes == null) return null;
        return vehicleTypes.stream()
                .map(VehicleType::getId)
                .collect(Collectors.toList());
    }
}
