package ee.detailing.api.notification;

import lombok.Data;

@Data
public class NotificationUpdateRequest {
    private String subject;
    private String body;
    private Boolean isActive;
}
