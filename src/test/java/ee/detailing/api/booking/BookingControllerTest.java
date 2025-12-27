package ee.detailing.api.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.detailing.api.timeslot.TimeSlotDto;
import ee.detailing.api.timeslot.TimeSlotStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper method to create a time slot for testing
    private Integer createTimeSlot(LocalDate date, Integer templateId) throws Exception {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setDate(date);
        dto.setStatus(TimeSlotStatus.AVAILABLE);
        dto.setTimeSlotTemplateId(templateId);

        String response = mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asInt();
    }

    // Helper method to create a booking
    private BookingCreateRequest createValidBookingRequest(Integer timeSlotId) {
        BookingCreateRequest request = new BookingCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhone("+372 5555 1234");
        request.setVehicleRegNumber("123ABC");
        request.setVehicleTypeId(1); // Car
        request.setPackageId(1); // Full Wash
        request.setTimeSlotId(timeSlotId);
        request.setDeliveryTypeId(1); // I bring it myself
        return request;
    }

    // === Public Endpoint Tests ===

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBooking_validRequest_createsAndReturnsWithReference() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 15), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        // Price = Car base (20) + Full Wash (30) + Self-delivery (0) = 50
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.reference", matchesPattern("BK-\\d{6}-[A-Z0-9]{4}")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.totalPrice", is(50.00)))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.vehicleTypeName", is("Car")))
                .andExpect(jsonPath("$.packageName", is("Full Wash")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBooking_withAddOns_calculatesCorrectPrice() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 16), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);
        request.setAddOnIds(List.of(1, 2)); // Rust spot removal (10) + Quick ceramic (15)

        // Price = Car (20) + Full Wash (30) + Self (0) + AddOns (10 + 15) = 75
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(75.00)))
                .andExpect(jsonPath("$.addOns", hasSize(2)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBooking_withPickupDeliveryAndAddress_createsSuccessfully() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 17), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);
        request.setDeliveryTypeId(2); // We pick up the car (15, requires address)
        request.setAddress("Pärnu mnt 100, Tallinn");

        // Price = Car (20) + Full Wash (30) + Pickup (15) = 65
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(65.00)))
                .andExpect(jsonPath("$.address", is("Pärnu mnt 100, Tallinn")))
                .andExpect(jsonPath("$.deliveryTypeName", is("We pick up the car")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBooking_timeSlotNotAvailable_returns400() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 18), 1);

        // First booking takes the slot
        BookingCreateRequest request1 = createValidBookingRequest(timeSlotId);
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        // Second booking tries same slot
        BookingCreateRequest request2 = createValidBookingRequest(timeSlotId);
        request2.setEmail("another@example.com");

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBooking_pickupDeliveryWithoutAddress_returns400() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 19), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);
        request.setDeliveryTypeId(2); // Pickup requires address
        request.setAddress(null);

        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getBookingByReference_exists_returnsBooking() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 20), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String reference = objectMapper.readTree(createResponse).get("reference").asText();

        mockMvc.perform(get("/api/v1/bookings/" + reference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference", is(reference)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    void getBookingByReference_notExists_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/bookings/BK-000000-XXXX"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void cancelBooking_validPending_cancelsAndReleasesSlot() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 21), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String reference = objectMapper.readTree(createResponse).get("reference").asText();

        // Cancel the booking
        mockMvc.perform(delete("/api/v1/bookings/" + reference))
                .andExpect(status().isNoContent());

        // Verify booking is cancelled
        mockMvc.perform(get("/api/v1/bookings/" + reference))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELLED_BY_CUSTOMER")));

        // Time slot should be available again - visible in public endpoint
        mockMvc.perform(get("/api/v1/time-slots")
                        .param("date", "2025-06-21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // === Admin Endpoint Tests - Authorization ===

    @Test
    void getAllBookings_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/bookings"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBookings_withAuth_returnsBookings() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 22), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);
        mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/admin/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getBookingById_exists_returnsBooking() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 23), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(createResponse).get("id").asInt();

        mockMvc.perform(get("/api/v1/admin/bookings/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBookingStatus_validTransition_updatesStatus() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 24), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(createResponse).get("id").asInt();

        // Transition PENDING -> CONFIRMED
        BookingStatusUpdateRequest statusRequest = new BookingStatusUpdateRequest();
        statusRequest.setStatus(BookingStatus.CONFIRMED);

        mockMvc.perform(put("/api/v1/admin/bookings/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));

        // Transition CONFIRMED -> COMPLETED
        statusRequest.setStatus(BookingStatus.COMPLETED);

        mockMvc.perform(put("/api/v1/admin/bookings/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBookingStatus_invalidTransition_returns400() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 25), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(createResponse).get("id").asInt();

        // Invalid transition PENDING -> COMPLETED (should go through CONFIRMED first)
        BookingStatusUpdateRequest statusRequest = new BookingStatusUpdateRequest();
        statusRequest.setStatus(BookingStatus.COMPLETED);

        mockMvc.perform(put("/api/v1/admin/bookings/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBooking_exists_deletesSuccessfully() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 26), 1);

        BookingCreateRequest request = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(createResponse).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/bookings/" + id))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/admin/bookings/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBooking_changesFields_recalculatesPrice() throws Exception {
        Integer timeSlotId = createTimeSlot(LocalDate.of(2025, 6, 27), 1);

        BookingCreateRequest createRequest = createValidBookingRequest(timeSlotId);

        String createResponse = mockMvc.perform(post("/api/v1/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(50.00))) // Car + Full Wash
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(createResponse).get("id").asInt();

        // Update to Van (base price 40 instead of 20)
        BookingUpdateRequest updateRequest = new BookingUpdateRequest();
        updateRequest.setVehicleTypeId(2); // Van

        mockMvc.perform(put("/api/v1/admin/bookings/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(70.00))) // Van (40) + Full Wash (30)
                .andExpect(jsonPath("$.vehicleTypeName", is("Van")));
    }
}
