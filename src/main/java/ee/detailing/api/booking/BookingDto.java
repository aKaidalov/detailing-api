package ee.detailing.api.booking;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingDto {
    private Integer id;
    private String reference;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String vehicleRegNumber;
    private BigDecimal totalPrice;
    private String notes;
    private String address;
    private BookingStatus status;

    // Flattened vehicle type
    private Integer vehicleTypeId;
    private String vehicleTypeName;
    private BigDecimal vehicleTypeBasePrice;

    // Flattened package
    private Integer packageId;
    private String packageName;
    private BigDecimal packagePrice;

    // Flattened time slot
    private Integer timeSlotId;
    private LocalDate timeSlotDate;
    private LocalTime timeSlotStartTime;
    private LocalTime timeSlotEndTime;

    // Flattened delivery type
    private Integer deliveryTypeId;
    private String deliveryTypeName;
    private BigDecimal deliveryTypePrice;

    // Add-ons
    private List<BookingAddOnDto> addOns;

    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
