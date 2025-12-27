package ee.detailing.api.notification;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto toDto(Notification entity);

    List<NotificationDto> toDtoList(List<Notification> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(NotificationUpdateRequest request, @MappingTarget Notification entity);

    @Mapping(source = "booking.id", target = "bookingId")
    @Mapping(source = "booking.reference", target = "bookingReference")
    @Mapping(source = "notification.type", target = "notificationType")
    NotificationLogDto toLogDto(NotificationLog entity);

    List<NotificationLogDto> toLogDtoList(List<NotificationLog> entities);
}
