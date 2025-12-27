package ee.detailing.api.booking;

import lombok.Data;

@Data
public class BookingStatusUpdateRequest {
    private BookingStatus status;
}
