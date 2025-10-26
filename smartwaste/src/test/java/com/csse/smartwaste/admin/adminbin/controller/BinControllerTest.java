package com.csse.smartwaste.admin.adminbin.controller;

import com.csse.smartwaste.admin.adminbin.entity.Bin;
import com.csse.smartwaste.admin.adminbin.service.BinService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BinController.class)
class BinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BinService binService;

    private Bin createMockBin() {
        Bin bin = new Bin();
        bin.setId("bin1");
        bin.setStatus("Active");
        bin.setAddress("Colombo");
        bin.setCreatedAt(LocalDateTime.now());
        return bin;
    }

    @Test
    void getAllBins_ShouldReturnOk() throws Exception {
        when(binService.getAllBins()).thenReturn(List.of(createMockBin()));
        mockMvc.perform(get("/api/admin/bins/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Active"));
    }

    @Test
    void getBinById_ShouldReturnBin_WhenFound() throws Exception {
        when(binService.getBinById("bin1")).thenReturn(Optional.of(createMockBin()));
        mockMvc.perform(get("/api/admin/bins/bin1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("bin1"));
    }

    @Test
    void getBinById_ShouldReturn404_WhenNotFound() throws Exception {
        when(binService.getBinById("notfound")).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/admin/bins/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Bin not found"));
    }

    @Test
    void createBinRequest_ShouldReturnSuccessMessage() throws Exception {
        Bin mock = createMockBin();
        when(binService.createBinRequest(any(Bin.class))).thenReturn(mock);

        String json = """
                {
                  "binId": "B001",
                  "address": "Colombo",
                  "ownerId": "U100"
                }
                """;

        mockMvc.perform(post("/api/admin/bins/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bin request created successfully"));
    }

    @Test
    void updateStatus_ShouldReturnUpdatedInfo() throws Exception {
        Bin updated = createMockBin();
        updated.setId("bin1");
        updated.setStatus("Approved");
        updated.setNote("Good"); // Added to avoid NPE in Map.of()

        when(binService.updateStatus(eq("bin1"), eq("Approved"), eq("Good"))).thenReturn(updated);

        mockMvc.perform(put("/api/admin/bins/bin1/status")
                .param("status", "Approved")
                .param("note", "Good"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newStatus").value("Approved"));
    }

    @Test
    void updateStatus_ShouldReturn404_WhenServiceThrowsError() throws Exception {
        when(binService.updateStatus(eq("invalid"), eq("Approved"), eq("Note")))
                .thenThrow(new RuntimeException("Bin not found"));

        mockMvc.perform(put("/api/admin/bins/invalid/status")
                .param("status", "Approved")
                .param("note", "Note"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Bin not found"));
    }

    @Test
    void deleteBin_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(binService).deleteBin("bin1");
        mockMvc.perform(delete("/api/admin/bins/bin1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bin deleted successfully"));
    }

    @Test
    void deleteBin_ShouldReturn404_WhenNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Bin not found")).when(binService).deleteBin("missing");
        mockMvc.perform(delete("/api/admin/bins/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Bin not found or already deleted"));
    }
}
