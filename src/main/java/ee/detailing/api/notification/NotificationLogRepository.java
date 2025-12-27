package ee.detailing.api.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Integer> {

    List<NotificationLog> findByBookingIdOrderByCreatedAtDesc(Integer bookingId);

    List<NotificationLog> findByStatus(NotificationLogStatus status);
}
