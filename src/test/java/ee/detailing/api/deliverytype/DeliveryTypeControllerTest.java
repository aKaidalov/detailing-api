package ee.detailing.api.deliverytype;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DeliveryTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoints

    @Test
    void getActiveDeliveryTypes_returnsDeliveryTypes() throws Exception {
        mockMvc.perform(get("/api/v1/delivery-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    // Admin endpoints - unauthorized

    @Test
    void getAllDeliveryTypes_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/delivery-types"))
                .andExpect(status().isForbidden());
    }

    // Admin endpoints - authorized

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllDeliveryTypes_withAuth_returnsDeliveryTypes() throws Exception {
        mockMvc.perform(get("/api/v1/admin/delivery-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createDeliveryType_createsAndReturns() throws Exception {
        DeliveryTypeDto dto = new DeliveryTypeDto();
        dto.setName("Express Pickup");
        dto.setIcon("🚀");
        dto.setPrice(new BigDecimal("25.00"));
        dto.setRequiresAddress(true);
        dto.setIsActive(true);

        mockMvc.perform(post("/api/v1/admin/delivery-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Express Pickup")))
                .andExpect(jsonPath("$.icon", is("🚀")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateDeliveryType_updatesAndReturns() throws Exception {
        DeliveryTypeDto dto = new DeliveryTypeDto();
        dto.setName("Updated Delivery");
        dto.setIcon("👤");
        dto.setPrice(new BigDecimal("20.00"));
        dto.setRequiresAddress(false);
        dto.setIsActive(true);

        mockMvc.perform(put("/api/v1/admin/delivery-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Delivery")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteDeliveryType_deletesSuccessfully() throws Exception {
        // First create one to delete
        DeliveryTypeDto dto = new DeliveryTypeDto();
        dto.setName("ToDelete");
        dto.setIcon("🗑️");
        dto.setPrice(new BigDecimal("0.00"));
        dto.setRequiresAddress(false);
        dto.setIsActive(true);

        String response = mockMvc.perform(post("/api/v1/admin/delivery-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        int id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/delivery-types/" + id))
                .andExpect(status().isNoContent());
    }
}
