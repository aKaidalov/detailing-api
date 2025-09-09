package ee.joonasvaleting.jvaleting.bookingstatus;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingStatusMapper {
    BookingStatusDto fromDbToDto(BookingStatus bookingStatus);
    BookingStatus fromDtoToDb(BookingStatusDto bookingStatusDto);
}
