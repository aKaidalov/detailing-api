package ee.detailing.api.addon;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddOnMapper {

    AddOnDto toDto(AddOn entity);

    List<AddOnDto> toDtoList(List<AddOn> entities);

    AddOn toEntity(AddOnDto dto);

    void updateEntityFromDto(AddOnDto dto, @MappingTarget AddOn entity);
}
