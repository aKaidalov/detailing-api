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

import java.time.LocalTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TimeSlotTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTemplates_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/time-slot-templates"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTemplates_withAuth_returnsTemplates() throws Exception {
        mockMvc.perform(get("/api/v1/admin/time-slot-templates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))))
                .andExpect(jsonPath("$[0].startTime", notNullValue()))
                .andExpect(jsonPath("$[0].endTime", notNullValue()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTemplate_createsAndReturns() throws Exception {
        TimeSlotTemplateDto dto = new TimeSlotTemplateDto();
        dto.setStartTime(LocalTime.of(8, 0));
        dto.setEndTime(LocalTime.of(9, 0));
        dto.setIsActive(true);

        mockMvc.perform(post("/api/v1/admin/time-slot-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.startTime", is("08:00:00")))
                .andExpect(jsonPath("$.endTime", is("09:00:00")))
                .andExpect(jsonPath("$.isActive", is(true)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTemplate_updatesAndReturns() throws Exception {
        TimeSlotTemplateDto dto = new TimeSlotTemplateDto();
        dto.setStartTime(LocalTime.of(10, 0));
        dto.setEndTime(LocalTime.of(12, 0));
        dto.setIsActive(false);

        mockMvc.perform(put("/api/v1/admin/time-slot-templates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime", is("10:00:00")))
                .andExpect(jsonPath("$.endTime", is("12:00:00")))
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTemplate_deletesSuccessfully() throws Exception {
        TimeSlotTemplateDto dto = new TimeSlotTemplateDto();
        dto.setStartTime(LocalTime.of(20, 0));
        dto.setEndTime(LocalTime.of(22, 0));
        dto.setIsActive(true);

        String response = mockMvc.perform(post("/api/v1/admin/time-slot-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/time-slot-templates/" + id))
                .andExpect(status().isNoContent());
    }

}
