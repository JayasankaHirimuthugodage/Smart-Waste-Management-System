package com.csse.smartwaste.admin.routechanges.controller;

import com.csse.smartwaste.admin.routechanges.model.RouteChange;
import com.csse.smartwaste.admin.routechanges.service.RouteChangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteChangeController.class)
class RouteChangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteChangeService routeService;

    private RouteChange createMockRoute() {
        RouteChange r = new RouteChange();
        r.setId("r1");
        r.setRouteName("Route A");
        r.setArea("Colombo");
        r.setStatus("PENDING");
        r.setPriority("HIGH");
        r.setRequestedBy("System");
        r.setWasteVolumePerDay(950);
        r.setDateRequested(LocalDateTime.now());
        return r;
    }

    @Test
    void getAll_ShouldReturnListOfRoutes() throws Exception {
        when(routeService.getAllRequests()).thenReturn(List.of(createMockRoute()));

        mockMvc.perform(get("/api/admin/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].routeName").value("Route A"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void create_ShouldReturnCreatedRoute() throws Exception {
        RouteChange mockRoute = createMockRoute();
        when(routeService.createRequest(any(RouteChange.class))).thenReturn(mockRoute);

        String json = """
                {
                  "routeName": "Route A",
                  "area": "Colombo",
                  "priority": "HIGH"
                }
                """;

        mockMvc.perform(post("/api/admin/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeName").value("Route A"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void updateStatus_ShouldReturnUpdatedRoute() throws Exception {
        RouteChange updated = createMockRoute();
        updated.setStatus("APPROVED");

        when(routeService.updateStatus(eq("r1"), eq("APPROVED"))).thenReturn(updated);

        mockMvc.perform(put("/api/admin/routes/r1/status")
                .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void updateStatus_ShouldReturn404_WhenNotFound() throws Exception {
        when(routeService.updateStatus(eq("missing"), eq("APPROVED")))
                .thenThrow(new RuntimeException("Route not found"));

        mockMvc.perform(put("/api/admin/routes/missing/status")
                .param("status", "APPROVED"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getHighWasteSuggestions_ShouldReturnList() throws Exception {
        when(routeService.getHighWasteAreaSuggestions()).thenReturn(List.of(createMockRoute()));

        mockMvc.perform(get("/api/admin/routes/suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("HIGH"));
    }
}
