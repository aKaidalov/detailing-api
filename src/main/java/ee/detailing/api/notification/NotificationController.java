package ee.detailing.api.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    @PutMapping("/{type}")
    public ResponseEntity<NotificationDto> updateNotification(
            @PathVariable NotificationType type,
            @RequestBody NotificationUpdateRequest request) {
        return ResponseEntity.ok(service.updateNotification(type, request));
    }
}
