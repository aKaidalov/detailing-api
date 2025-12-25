package ee.detailing.api.pkg;

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
class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoints

    @Test
    void getPackagesForVehicleType_returnsPackages() throws Exception {
        // Vehicle type 1 (Car) has packages linked via vehicle_type_package
        mockMvc.perform(get("/api/v1/vehicle-types/1/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    void getPackagesForVehicleType_noPackages_returnsEmptyList() throws Exception {
        // Vehicle type 999 doesn't exist, should return empty list
        mockMvc.perform(get("/api/v1/vehicle-types/999/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Admin endpoints - unauthorized

    @Test
    void getAllPackages_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/packages"))
                .andExpect(status().isForbidden());
    }

    // Admin endpoints - authorized

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllPackages_withAuth_returnsPackages() throws Exception {
        mockMvc.perform(get("/api/v1/admin/packages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPackage_createsAndReturns() throws Exception {
        PackageDto dto = new PackageDto();
        dto.setName("Premium Wash");
        dto.setPrice(new BigDecimal("50.00"));
        dto.setDescription("Premium detailing package");
        dto.setIsActive(true);
        dto.setDisplayOrder(4);

        mockMvc.perform(post("/api/v1/admin/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Premium Wash")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePackage_updatesAndReturns() throws Exception {
        PackageDto dto = new PackageDto();
        dto.setName("Updated Package");
        dto.setPrice(new BigDecimal("35.00"));
        dto.setDescription("Updated description");
        dto.setIsActive(true);
        dto.setDisplayOrder(1);

        mockMvc.perform(put("/api/v1/admin/packages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Package")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePackage_deletesSuccessfully() throws Exception {
        // First create one to delete
        PackageDto dto = new PackageDto();
        dto.setName("ToDelete");
        dto.setPrice(new BigDecimal("10.00"));
        dto.setIsActive(true);
        dto.setDisplayOrder(99);

        String response = mockMvc.perform(post("/api/v1/admin/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/packages/" + id))
                .andExpect(status().isNoContent());
    }
}
