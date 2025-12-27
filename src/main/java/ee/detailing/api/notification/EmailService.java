package ee.detailing.api.notification;

import ee.detailing.api.booking.Booking;
import ee.detailing.api.businesssettings.BusinessSettings;
import ee.detailing.api.businesssettings.BusinessSettingsRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final BusinessSettingsRepository businessSettingsRepository;

    private static final String BASE_URL = "http://localhost:8080";

    @Async("emailTaskExecutor")
    @Transactional
    public void sendBookingEmailAsync(Booking booking, NotificationType notificationType) {
        log.info("Starting async email send for booking {} with type {}", booking.getReference(), notificationType);

        // Create log entry with PENDING status
        NotificationLog notificationLog = createNotificationLog(booking, notificationType);

        try {
            // Load template
            Notification template = notificationRepository.findByTypeAndIsActiveTrue(notificationType)
                    .orElseThrow(() -> new IllegalStateException("No active template found for " + notificationType));

            // Load business settings
            BusinessSettings settings = businessSettingsRepository.findById(1)
                    .orElseThrow(() -> new IllegalStateException("Business settings not configured"));

            // Build email context
            EmailContext context = buildEmailContext(booking, settings);

            // Process template
            String subject = processTemplate(template.getSubject(), context);
            String body = processTemplate(template.getBody(), context);

            // Send email
            sendHtmlEmail(booking.getEmail(), subject, body, settings.getEmail());

            // Update log to SENT
            notificationLog.setStatus(NotificationLogStatus.SENT);
            notificationLog.setSentAt(LocalDateTime.now());
            notificationLogRepository.save(notificationLog);

            log.info("Email sent successfully for booking {}", booking.getReference());

        } catch (Exception e) {
            log.error("Failed to send email for booking {}: {}", booking.getReference(), e.getMessage(), e);

            // Update log to FAILED
            notificationLog.setStatus(NotificationLogStatus.FAILED);
            notificationLogRepository.save(notificationLog);
        }
    }

    private NotificationLog createNotificationLog(Booking booking, NotificationType notificationType) {
        Notification template = notificationRepository.findByType(notificationType)
                .orElseThrow(() -> new IllegalStateException("Template not found: " + notificationType));

        NotificationLog notificationLog = new NotificationLog();
        notificationLog.setBooking(booking);
        notificationLog.setNotification(template);
        notificationLog.setStatus(NotificationLogStatus.PENDING);
        return notificationLogRepository.save(notificationLog);
    }

    private EmailContext buildEmailContext(Booking booking, BusinessSettings settings) {
        String clientName = booking.getFirstName() + " " + booking.getLastName();
        String services = booking.getVehicleType().getName() + ", " + booking.getPkg().getName();

        String subServices = booking.getAddOns().isEmpty() ? "None" :
                booking.getAddOns().stream()
                        .map(addOn -> addOn.getName())
                        .collect(Collectors.joining(", "));

        String deliveryOption = booking.getDeliveryType().getName();
        if (booking.getAddress() != null && !booking.getAddress().isBlank()) {
            deliveryOption += " (" + booking.getAddress() + ")";
        }

        return EmailContext.builder()
                .clientName(clientName)
                .bookingRef(booking.getReference())
                .date(booking.getTimeSlot().getDate())
                .startTime(booking.getTimeSlot().getTimeSlotTemplate().getStartTime())
                .endTime(booking.getTimeSlot().getTimeSlotTemplate().getEndTime())
                .services(services)
                .subServices(subServices)
                .deliveryOption(deliveryOption)
                .totalPrice(booking.getTotalPrice())
                .businessName(settings.getName())
                .businessPhone(settings.getPhone())
                .businessAddress(settings.getAddress())
                .cancellationLink(BASE_URL + "/api/v1/bookings/" + booking.getReference())
                .rebookingLink(BASE_URL)
                .build();
    }

    private String processTemplate(String template, EmailContext context) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String timeRange = context.getStartTime() + " - " + context.getEndTime();
        String formattedPrice = String.format("%.2f", context.getTotalPrice());

        return template
                .replace("#{bookingRef}", context.getBookingRef())
                .replace("{clientName}", context.getClientName())
                .replace("{bookingRef}", context.getBookingRef())
                .replace("{date}", context.getDate().format(dateFormatter))
                .replace("{time}", timeRange)
                .replace("{services}", context.getServices())
                .replace("{subServices}", context.getSubServices())
                .replace("{deliveryOption}", context.getDeliveryOption())
                .replace("{totalPrice}", formattedPrice + " EUR")
                .replace("{businessName}", context.getBusinessName())
                .replace("{businessPhone}", context.getBusinessPhone())
                .replace("{businessAddress}", context.getBusinessAddress())
                .replace("{cancellationLink}", context.getCancellationLink())
                .replace("{rebookingLink}", context.getRebookingLink());
    }

    private void sendHtmlEmail(String to, String subject, String body, String from) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);

        // Wrap body in basic HTML structure
        String htmlBody = "<html><body style='font-family: Arial, sans-serif;'>" +
                body.replace("\n", "<br>") +
                "</body></html>";

        helper.setText(htmlBody, true);

        mailSender.send(message);
    }
}
