package ee.detailing.api.pkg;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PackageMapper {

    PackageDto toDto(Package entity);

    List<PackageDto> toDtoList(List<Package> entities);

    Package toEntity(PackageDto dto);

    void updateEntityFromDto(PackageDto dto, @MappingTarget Package entity);
}
