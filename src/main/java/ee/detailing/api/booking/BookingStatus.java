package ee.detailing.api.booking;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED_BY_CUSTOMER,
    CANCELLED_BY_ADMIN;

    public boolean isTerminal() {
        return this == COMPLETED ||
               this == CANCELLED_BY_CUSTOMER ||
               this == CANCELLED_BY_ADMIN;
    }

    public boolean canTransitionTo(BookingStatus target) {
        if (this.isTerminal()) {
            return false;
        }
        if (this == PENDING) {
            return target == CONFIRMED ||
                   target == CANCELLED_BY_CUSTOMER ||
                   target == CANCELLED_BY_ADMIN;
        }
        if (this == CONFIRMED) {
            return target == COMPLETED ||
                   target == CANCELLED_BY_CUSTOMER ||
                   target == CANCELLED_BY_ADMIN;
        }
        return false;
    }
}
