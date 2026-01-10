package ee.detailing.api.notification;

public enum NotificationType {
    BOOKING_CREATED,              // Client creates booking (was BOOKING_CONFIRMATION)
    BOOKING_CONFIRMED,            // Admin confirms booking (new)
    BOOKING_MODIFIED,             // Admin modifies booking (was BOOKING_MODIFICATION)
    BOOKING_COMPLETED,            // Admin completes booking (new)
    BOOKING_CANCELLED_BY_CUSTOMER, // Customer cancels (was BOOKING_CANCELLATION)
    BOOKING_CANCELLED_BY_ADMIN    // Admin cancels (new)
}
