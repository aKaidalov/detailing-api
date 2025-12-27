package ee.detailing.api.booking;

import ee.detailing.api.addon.AddOn;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "vehicleType.id", target = "vehicleTypeId")
    @Mapping(source = "vehicleType.name", target = "vehicleTypeName")
    @Mapping(source = "vehicleType.basePrice", target = "vehicleTypeBasePrice")
    @Mapping(source = "pkg.id", target = "packageId")
    @Mapping(source = "pkg.name", target = "packageName")
    @Mapping(source = "pkg.price", target = "packagePrice")
    @Mapping(source = "timeSlot.id", target = "timeSlotId")
    @Mapping(source = "timeSlot.date", target = "timeSlotDate")
    @Mapping(source = "timeSlot.timeSlotTemplate.startTime", target = "timeSlotStartTime")
    @Mapping(source = "timeSlot.timeSlotTemplate.endTime", target = "timeSlotEndTime")
    @Mapping(source = "deliveryType.id", target = "deliveryTypeId")
    @Mapping(source = "deliveryType.name", target = "deliveryTypeName")
    @Mapping(source = "deliveryType.price", target = "deliveryTypePrice")
    @Mapping(source = "addOns", target = "addOns")
    BookingDto toDto(Booking entity);

    List<BookingDto> toDtoList(List<Booking> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    @Mapping(target = "deliveryType", ignore = true)
    @Mapping(target = "addOns", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "vehicleType", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "timeSlot", ignore = true)
    @Mapping(target = "deliveryType", ignore = true)
    @Mapping(target = "addOns", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(BookingUpdateRequest request, @MappingTarget Booking entity);

    BookingAddOnDto addOnToDto(AddOn addOn);

    default List<BookingAddOnDto> addOnsToDto(Set<AddOn> addOns) {
        if (addOns == null) {
            return List.of();
        }
        return addOns.stream()
                .map(this::addOnToDto)
                .sorted(Comparator.comparing(BookingAddOnDto::getId))
                .toList();
    }
}
