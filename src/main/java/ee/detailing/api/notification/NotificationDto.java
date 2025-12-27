package ee.detailing.api.notification;

import lombok.Data;

@Data
public class NotificationDto {
    private Integer id;
    private NotificationType type;
    private String subject;
    private String body;
    private Boolean isActive;
}
