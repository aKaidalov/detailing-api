package ee.detailing.api.notification;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class EmailContext {
    private String clientName;
    private String bookingRef;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String services;
    private String subServices;
    private String deliveryOption;
    private BigDecimal totalPrice;
    private String businessName;
    private String businessPhone;
    private String businessAddress;
    private String cancellationLink;
    private String rebookingLink;
}
