package ee.joonasvaleting.jvaleting.serviceitem;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    ServiceItemDto fromDbToDto(ServiceItem serviceItem);
    ServiceItem fromDtoToDb(ServiceItemDto serviceItemDto);
}
