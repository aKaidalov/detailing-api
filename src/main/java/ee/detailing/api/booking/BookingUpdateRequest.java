package ee.detailing.api.booking;

import lombok.Data;

import java.util.List;

@Data
public class BookingUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String vehicleRegNumber;
    private Integer vehicleTypeId;
    private Integer packageId;
    private Integer timeSlotId;
    private Integer deliveryTypeId;
    private String address;
    private String notes;
    private List<Integer> addOnIds;
}
