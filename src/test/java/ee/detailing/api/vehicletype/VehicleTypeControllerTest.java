package ee.detailing.api.vehicletype;

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
class VehicleTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoints

    @Test
    void getActiveVehicleTypes_returnsVehicleTypes() throws Exception {
        mockMvc.perform(get("/api/v1/vehicle-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    // Admin endpoints - unauthorized

    @Test
    void getAllVehicleTypes_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/vehicle-types"))
                .andExpect(status().isForbidden());
    }

    // Admin endpoints - authorized

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllVehicleTypes_withAuth_returnsVehicleTypes() throws Exception {
        mockMvc.perform(get("/api/v1/admin/vehicle-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createVehicleType_createsAndReturns() throws Exception {
        VehicleTypeDto dto = new VehicleTypeDto();
        dto.setName("SUV");
        dto.setIcon("🚙");
        dto.setBasePrice(new BigDecimal("30.00"));
        dto.setDescription("Sport utility vehicle");
        dto.setIsDeliverable(true);
        dto.setIsActive(true);
        dto.setDisplayOrder(3);

        mockMvc.perform(post("/api/v1/admin/vehicle-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("SUV")))
                .andExpect(jsonPath("$.icon", is("🚙")))
                .andExpect(jsonPath("$.basePrice", is(30.00)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateVehicleType_updatesAndReturns() throws Exception {
        VehicleTypeDto dto = new VehicleTypeDto();
        dto.setName("Updated Car");
        dto.setIcon("🚗");
        dto.setBasePrice(new BigDecimal("25.00"));
        dto.setDescription("Updated description");
        dto.setIsDeliverable(true);
        dto.setIsActive(true);
        dto.setDisplayOrder(1);

        mockMvc.perform(put("/api/v1/admin/vehicle-types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Car")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteVehicleType_deletesSuccessfully() throws Exception {
        // First create one to delete
        VehicleTypeDto dto = new VehicleTypeDto();
        dto.setName("ToDelete");
        dto.setIcon("🗑️");
        dto.setBasePrice(new BigDecimal("10.00"));
        dto.setIsDeliverable(true);
        dto.setIsActive(true);
        dto.setDisplayOrder(99);

        String response = mockMvc.perform(post("/api/v1/admin/vehicle-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/vehicle-types/" + id))
                .andExpect(status().isNoContent());
    }
}
