package ee.joonasvaleting.jvaleting.serviceitem;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    ServiceItemDto fromDbToDto(ServiceItem serviceItem);
    ServiceItem fromDtoToDb(ServiceItemDto serviceItemDto);
}
