package ee.detailing.api.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Optional<Notification> findByType(NotificationType type);

    Optional<Notification> findByTypeAndIsActiveTrue(NotificationType type);
}
