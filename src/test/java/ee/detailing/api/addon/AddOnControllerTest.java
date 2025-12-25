package ee.detailing.api.addon;

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
class AddOnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Public endpoints

    @Test
    void getAddOnsForPackage_returnsAddOns() throws Exception {
        // Package 1 (Full Wash) has add-ons linked via package_add_on
        mockMvc.perform(get("/api/v1/packages/1/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name", notNullValue()));
    }

    @Test
    void getAddOnsForPackage_noAddOns_returnsEmptyList() throws Exception {
        // Package 999 doesn't exist, should return empty list
        mockMvc.perform(get("/api/v1/packages/999/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // Admin endpoints - unauthorized

    @Test
    void getAllAddOns_withoutAuth_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/add-ons"))
                .andExpect(status().isForbidden());
    }

    // Admin endpoints - authorized

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAddOns_withAuth_returnsAddOns() throws Exception {
        mockMvc.perform(get("/api/v1/admin/add-ons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAddOn_createsAndReturns() throws Exception {
        AddOnDto dto = new AddOnDto();
        dto.setName("Window Tinting");
        dto.setPrice(new BigDecimal("200.00"));
        dto.setDescription("Professional window tinting");
        dto.setIsActive(true);
        dto.setDisplayOrder(11);

        mockMvc.perform(post("/api/v1/admin/add-ons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Window Tinting")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAddOn_updatesAndReturns() throws Exception {
        AddOnDto dto = new AddOnDto();
        dto.setName("Updated Add-on");
        dto.setPrice(new BigDecimal("15.00"));
        dto.setDescription("Updated description");
        dto.setIsActive(true);
        dto.setDisplayOrder(1);

        mockMvc.perform(put("/api/v1/admin/add-ons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Add-on")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteAddOn_deletesSuccessfully() throws Exception {
        // First create one to delete
        AddOnDto dto = new AddOnDto();
        dto.setName("ToDelete");
        dto.setPrice(new BigDecimal("5.00"));
        dto.setIsActive(true);
        dto.setDisplayOrder(99);

        String response = mockMvc.perform(post("/api/v1/admin/add-ons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Integer id = objectMapper.readTree(response).get("id").asInt();

        mockMvc.perform(delete("/api/v1/admin/add-ons/" + id))
                .andExpect(status().isNoContent());
    }
}
