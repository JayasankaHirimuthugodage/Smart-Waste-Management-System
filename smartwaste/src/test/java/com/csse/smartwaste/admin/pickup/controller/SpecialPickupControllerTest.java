package com.csse.smartwaste.admin.pickup.controller;

import com.csse.smartwaste.admin.pickup.entity.SpecialPickup;
import com.csse.smartwaste.admin.pickup.service.SpecialPickupService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialPickupController.class)
class SpecialPickupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialPickupService pickupService;

    private SpecialPickup createMockPickup() {
        SpecialPickup pickup = new SpecialPickup();
        pickup.setId("p1");
        pickup.setName("John Doe");
        pickup.setArea("Colombo");
        pickup.setDate("2025-11-01");
        pickup.setType("Plastic");
        pickup.setStatus("pending");
        return pickup;
    }

    @Test
    void getAll_ShouldReturnListOfPickups() throws Exception {
        when(pickupService.getAllRequests()).thenReturn(List.of(createMockPickup()));

        mockMvc.perform(get("/api/admin/pickups/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("p1"))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].status").value("pending"));
    }

    @Test
    void create_ShouldReturnCreatedPickup() throws Exception {
        SpecialPickup mockPickup = createMockPickup();
        when(pickupService.createRequest(any(SpecialPickup.class))).thenReturn(mockPickup);

        String json = """
                {
                  "name": "John Doe",
                  "area": "Colombo",
                  "date": "2025-11-01",
                  "type": "Plastic"
                }
                """;

        mockMvc.perform(post("/api/admin/pickups/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    void create_ShouldHandleMissingFields() throws Exception {
        when(pickupService.createRequest(any(SpecialPickup.class)))
                .thenThrow(new RuntimeException("Invalid request data"));

        String json = "{}";

        mockMvc.perform(post("/api/admin/pickups/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void updateStatus_ShouldReturnUpdatedPickup() throws Exception {
        SpecialPickup updated = createMockPickup();
        updated.setStatus("approved");

        when(pickupService.updateStatus(eq("p1"), eq("approved"))).thenReturn(updated);

        mockMvc.perform(put("/api/admin/pickups/p1/status")
                .param("status", "approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("approved"));
    }

    @Test
    void updateStatus_ShouldReturn500_WhenPickupNotFound() throws Exception {
        when(pickupService.updateStatus(eq("missing"), eq("approved"))).thenReturn(null);

        mockMvc.perform(put("/api/admin/pickups/missing/status")
                .param("status", "approved"))
                .andExpect(status().isNotFound());

    }
}
