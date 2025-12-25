package ee.detailing.api.deliverytype;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryTypeMapper {

    DeliveryTypeDto toDto(DeliveryType entity);

    List<DeliveryTypeDto> toDtoList(List<DeliveryType> entities);

    DeliveryType toEntity(DeliveryTypeDto dto);

    void updateEntityFromDto(DeliveryTypeDto dto, @MappingTarget DeliveryType entity);
}
