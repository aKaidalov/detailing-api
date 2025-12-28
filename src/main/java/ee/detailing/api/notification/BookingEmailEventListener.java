package ee.detailing.api.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEmailEventListener {

    private final EmailService emailService;

    /**
     * Listens for BookingEmailEvent and triggers email sending AFTER the transaction commits.
     * This ensures the booking exists in the database before an attempt to reference it.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookingEmailEvent(BookingEmailEvent event) {
        log.info("Handling BookingEmailEvent for booking {} after transaction commit", event.getBookingId());
        emailService.sendBookingEmailAsync(event.getBookingId(), event.getNotificationType());
    }
}
