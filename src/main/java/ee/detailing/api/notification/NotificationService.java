package ee.detailing.api.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    @Transactional(readOnly = true)
    public List<NotificationDto> getAllNotifications() {
        return mapper.toDtoList(notificationRepository.findAll());
    }

    @Transactional
    public NotificationDto updateNotification(NotificationType type, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findByType(type)
                .orElseThrow(() -> new IllegalArgumentException("Notification template not found: " + type));

        mapper.updateEntityFromRequest(request, notification);
        return mapper.toDto(notificationRepository.save(notification));
    }
}
