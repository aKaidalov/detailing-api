package ee.detailing.api.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // === Authorization Tests ===

    @Test
    void getAllNotifications_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notifications"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateNotification_withoutAuth_returns403() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setSubject("New Subject");

        mockMvc.perform(put("/api/v1/admin/notifications/BOOKING_CONFIRMATION")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    // === GET /api/v1/admin/notifications Tests ===

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllNotifications_returnsAllTemplates() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(
                        "BOOKING_CONFIRMATION",
                        "BOOKING_MODIFICATION",
                        "BOOKING_CANCELLATION")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllNotifications_containsExpectedFields() throws Exception {
        mockMvc.perform(get("/api/v1/admin/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].type", notNullValue()))
                .andExpect(jsonPath("$[0].subject", notNullValue()))
                .andExpect(jsonPath("$[0].body", notNullValue()))
                .andExpect(jsonPath("$[0].isActive", notNullValue()));
    }

    // === PUT /api/v1/admin/notifications/{type} Tests ===

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotification_updateSubject_updatesSuccessfully() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setSubject("Updated Subject - #{bookingRef}");

        mockMvc.perform(put("/api/v1/admin/notifications/BOOKING_CONFIRMATION")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is("Updated Subject - #{bookingRef}")))
                .andExpect(jsonPath("$.type", is("BOOKING_CONFIRMATION")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotification_updateBody_updatesSuccessfully() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setBody("This is the new email body for {clientName}");

        mockMvc.perform(put("/api/v1/admin/notifications/BOOKING_MODIFICATION")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body", is("This is the new email body for {clientName}")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotification_updateIsActive_updatesSuccessfully() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setIsActive(false);

        mockMvc.perform(put("/api/v1/admin/notifications/BOOKING_CANCELLATION")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotification_updateAllFields_updatesSuccessfully() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setSubject("Complete Update Subject");
        request.setBody("Complete Update Body");
        request.setIsActive(false);

        mockMvc.perform(put("/api/v1/admin/notifications/BOOKING_CONFIRMATION")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subject", is("Complete Update Subject")))
                .andExpect(jsonPath("$.body", is("Complete Update Body")))
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateNotification_invalidType_returns400() throws Exception {
        NotificationUpdateRequest request = new NotificationUpdateRequest();
        request.setSubject("New Subject");

        mockMvc.perform(put("/api/v1/admin/notifications/INVALID_TYPE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
