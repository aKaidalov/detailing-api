package ee.detailing.api.notification;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BookingEmailEvent extends ApplicationEvent {

    private final Integer bookingId;
    private final NotificationType notificationType;

    public BookingEmailEvent(Object source, Integer bookingId, NotificationType notificationType) {
        super(source);
        this.bookingId = bookingId;
        this.notificationType = notificationType;
    }
}
