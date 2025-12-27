package ee.detailing.api.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationLogDto {
    private Integer id;
    private LocalDateTime sentAt;
    private NotificationLogStatus status;
    private Integer bookingId;
    private String bookingReference;
    private NotificationType notificationType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
