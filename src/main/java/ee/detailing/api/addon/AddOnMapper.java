package ee.detailing.api.addon;

import ee.detailing.api.pkg.Package;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AddOnMapper {

    @Mapping(target = "packageIds", source = "packages", qualifiedByName = "packagesToIds")
    AddOnDto toDto(AddOn entity);

    List<AddOnDto> toDtoList(List<AddOn> entities);

    @Mapping(target = "packages", ignore = true)
    AddOn toEntity(AddOnDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "packages", ignore = true)
    void updateEntityFromDto(AddOnDto dto, @MappingTarget AddOn entity);

    @Named("packagesToIds")
    default List<Integer> packagesToIds(Set<Package> packages) {
        if (packages == null) return null;
        return packages.stream()
                .map(Package::getId)
                .collect(Collectors.toList());
    }
}
