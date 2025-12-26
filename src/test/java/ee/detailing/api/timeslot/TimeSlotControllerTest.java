package ee.detailing.api.timeslot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TimeSlotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoints

    @Test
    void getAvailableSlots_returnsEmptyForDateWithNoSlots() throws Exception {
        mockMvc.perform(get("/api/v1/time-slots")
                        .param("date", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Admin endpoints - unauthorized

    @Test
    void getSlotsByDateRange_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/time-slots")
                        .param("from", "2025-01-01")
                        .param("to", "2025-01-31"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createSlot_withoutAuth_returns403() throws Exception {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setDate(LocalDate.of(2025, 6, 15));
        dto.setStatus(TimeSlotStatus.AVAILABLE);
        dto.setTimeSlotTemplateId(1);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    // Admin endpoints - authorized

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSlotsByDateRange_withAuth_returnsSlots() throws Exception {
        mockMvc.perform(get("/api/v1/admin/time-slots")
                        .param("from", "2025-01-01")
                        .param("to", "2025-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createSlot_createsAndReturns() throws Exception {
        TimeSlotDto dto = new TimeSlotDto();
        dto.setDate(LocalDate.of(2025, 6, 15));
        dto.setStatus(TimeSlotStatus.AVAILABLE);
        dto.setTimeSlotTemplateId(1);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.date", is("2025-06-15")))
                .andExpect(jsonPath("$.status", is("AVAILABLE")))
                .andExpect(jsonPath("$.timeSlotTemplateId", is(1)))
                .andExpect(jsonPath("$.startTime", is("09:00:00")))
                .andExpect(jsonPath("$.endTime", is("11:00:00")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateSlot_updatesStatusAndReturns() throws Exception {
        // First create a slot
        TimeSlotDto createDto = new TimeSlotDto();
        createDto.setDate(LocalDate.of(2025, 7, 20));
        createDto.setStatus(TimeSlotStatus.AVAILABLE);
        createDto.setTimeSlotTemplateId(2);

        String response = mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        // Now update status to BLOCKED
        TimeSlotDto updateDto = new TimeSlotDto();
        updateDto.setDate(LocalDate.of(2025, 7, 20));
        updateDto.setStatus(TimeSlotStatus.BLOCKED);
        updateDto.setTimeSlotTemplateId(2);

        mockMvc.perform(put("/api/v1/admin/time-slots/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("BLOCKED")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteSlot_deletesSuccessfully() throws Exception {
        // First create one to delete
        TimeSlotDto dto = new TimeSlotDto();
        dto.setDate(LocalDate.of(2025, 8, 1));
        dto.setStatus(TimeSlotStatus.AVAILABLE);
        dto.setTimeSlotTemplateId(1);

        String response = mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/time-slots/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void publicEndpoint_returnsOnlyAvailableSlots() throws Exception {
        LocalDate testDate = LocalDate.of(2025, 9, 15);

        // Create AVAILABLE slot
        TimeSlotDto availableDto = new TimeSlotDto();
        availableDto.setDate(testDate);
        availableDto.setStatus(TimeSlotStatus.AVAILABLE);
        availableDto.setTimeSlotTemplateId(1);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availableDto)))
                .andExpect(status().isOk());

        // Create BLOCKED slot
        TimeSlotDto blockedDto = new TimeSlotDto();
        blockedDto.setDate(testDate);
        blockedDto.setStatus(TimeSlotStatus.BLOCKED);
        blockedDto.setTimeSlotTemplateId(2);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockedDto)))
                .andExpect(status().isOk());

        // Public endpoint should only return AVAILABLE slots
        mockMvc.perform(get("/api/v1/time-slots")
                        .param("date", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status", is("AVAILABLE")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSlotsByDateRange_returnsAllStatuses() throws Exception {
        LocalDate testDate = LocalDate.of(2025, 10, 1);

        // Create AVAILABLE slot
        TimeSlotDto availableDto = new TimeSlotDto();
        availableDto.setDate(testDate);
        availableDto.setStatus(TimeSlotStatus.AVAILABLE);
        availableDto.setTimeSlotTemplateId(1);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(availableDto)))
                .andExpect(status().isOk());

        // Create BLOCKED slot
        TimeSlotDto blockedDto = new TimeSlotDto();
        blockedDto.setDate(testDate);
        blockedDto.setStatus(TimeSlotStatus.BLOCKED);
        blockedDto.setTimeSlotTemplateId(2);

        mockMvc.perform(post("/api/v1/admin/time-slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blockedDto)))
                .andExpect(status().isOk());

        // Admin endpoint should return all slots
        mockMvc.perform(get("/api/v1/admin/time-slots")
                        .param("from", testDate.toString())
                        .param("to", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
