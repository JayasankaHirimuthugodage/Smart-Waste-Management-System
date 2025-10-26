package com.csse.smartwaste.collection.controller;

import com.csse.smartwaste.collection.dto.CollectionRecordRequest;
import com.csse.smartwaste.collection.dto.CollectionRecordResponse;
import com.csse.smartwaste.collection.entity.CollectionRecord;
import com.csse.smartwaste.collection.service.CollectionService;
import com.csse.smartwaste.common.util.TestDataBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Collection Controller Test Suite
 * 
 * PURPOSE:
 * - Tests HTTP layer (Controller) for collection operations
 * - Verifies correct HTTP status codes and response formats
 * - Tests REST API endpoints for collection management
 * - Demonstrates MockMvc usage for controller testing
 * 
 * TEST CATEGORIES:
 * - Positive Test Cases (2 tests): HTTP endpoint validation
 * 
 * TOTAL: 2 test methods
 */
@WebMvcTest(CollectionController.class)
@DisplayName("Collection Controller Test Suite")
class CollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectionService collectionService;

    @Autowired
    private ObjectMapper objectMapper;

    // ====================================
    // 🟢 POSITIVE TEST CASES (HTTP Layer)
    // ====================================

    @Nested
    @DisplayName("Positive Test Cases - HTTP Endpoints")
    class PositiveTestCases {

        @Test
        @DisplayName("PTC-02: Route selection displays bins - GET /worker/{workerId} returns collection records")
        void testRouteSelectionDisplaysBins() throws Exception {
            // GIVEN: A worker ID and their collection records (route bins)
            String workerId = "WORKER001";
            
            // Create mock collection records representing bins in a route
            CollectionRecordResponse bin1 = createMockCollectionResponse(
                "BIN001", workerId, "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);
            CollectionRecordResponse bin2 = createMockCollectionResponse(
                "BIN002", workerId, "456 Oak Ave", CollectionRecord.CollectionStatus.COLLECTED);
            CollectionRecordResponse bin3 = createMockCollectionResponse(
                "BIN003", workerId, "789 Pine Rd", CollectionRecord.CollectionStatus.MISSED);
            
            List<CollectionRecordResponse> routeBins = Arrays.asList(bin1, bin2, bin3);

            // Mock service to return route bins for worker
            when(collectionService.getCollectionRecordsByWorker(workerId)).thenReturn(routeBins);

            // WHEN: Making GET request to retrieve worker's collection records (route)
            // THEN: Should return HTTP 200 OK with list of bins in the route
            mockMvc.perform(get("/api/collections/worker/{workerId}", workerId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].binId", is("BIN001")))
                    .andExpect(jsonPath("$[0].binLocation", is("123 Main St")))
                    .andExpect(jsonPath("$[0].status", is("COLLECTED")))
                    .andExpect(jsonPath("$[1].binId", is("BIN002")))
                    .andExpect(jsonPath("$[1].binLocation", is("456 Oak Ave")))
                    .andExpect(jsonPath("$[2].binId", is("BIN003")))
                    .andExpect(jsonPath("$[2].binLocation", is("789 Pine Rd")))
                    .andExpect(jsonPath("$[2].status", is("MISSED")));

            // Verify service method was called with correct worker ID
            verify(collectionService, times(1)).getCollectionRecordsByWorker(workerId);
        }

        @Test
        @DisplayName("PTC-06: Success message feedback - POST /collections returns HTTP 201 CREATED with success response")
        void testSuccessMessageFeedback() throws Exception {
            // GIVEN: A valid collection request
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            
            // Create mock success response
            CollectionRecordResponse successResponse = createMockCollectionResponse(
                "BIN001", "WORKER001", "123 Main Street, Colombo", 
                CollectionRecord.CollectionStatus.COLLECTED);

            // Mock service to return successful creation
            when(collectionService.createCollectionRecord(any(CollectionRecordRequest.class)))
                    .thenReturn(successResponse);

            // WHEN: Making POST request to create collection record
            // THEN: Should return HTTP 201 CREATED with success response body
            mockMvc.perform(post("/api/collections")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.binId", is("BIN001")))
                    .andExpect(jsonPath("$.workerId", is("WORKER001")))
                    .andExpect(jsonPath("$.binLocation", is("123 Main Street, Colombo")))
                    .andExpect(jsonPath("$.status", is("COLLECTED")))
                    .andExpect(jsonPath("$.weight", is(15.5)))
                    .andExpect(jsonPath("$.fillLevel", is(80)));

            // Verify service method was called to create collection record
            verify(collectionService, times(1)).createCollectionRecord(any(CollectionRecordRequest.class));
        }

        @Test
        @DisplayName("Additional: GET /worker/{workerId}/today returns today's collections")
        void testGetTodayCollections() throws Exception {
            // GIVEN: A worker ID and today's collection records
            String workerId = "WORKER001";
            
            CollectionRecordResponse todayBin1 = createMockCollectionResponse(
                "BIN001", workerId, "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);
            CollectionRecordResponse todayBin2 = createMockCollectionResponse(
                "BIN002", workerId, "456 Oak Ave", CollectionRecord.CollectionStatus.COLLECTED);
            
            List<CollectionRecordResponse> todayRecords = Arrays.asList(todayBin1, todayBin2);

            // Mock service to return today's records
            when(collectionService.getTodayCollectionRecordsByWorker(workerId))
                    .thenReturn(todayRecords);

            // WHEN: Making GET request for today's collections
            // THEN: Should return HTTP 200 OK with today's bins
            mockMvc.perform(get("/api/collections/worker/{workerId}/today", workerId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].binId", is("BIN001")))
                    .andExpect(jsonPath("$[1].binId", is("BIN002")));

            // Verify service method was called
            verify(collectionService, times(1)).getTodayCollectionRecordsByWorker(workerId);
        }

        @Test
        @DisplayName("Additional: GET /bin/{binId} returns collection records for bin")
        void testGetCollectionRecordsByBin() throws Exception {
            // GIVEN: A bin ID and its collection history
            String binId = "BIN001";
            
            CollectionRecordResponse record1 = createMockCollectionResponse(
                binId, "WORKER001", "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);
            CollectionRecordResponse record2 = createMockCollectionResponse(
                binId, "WORKER002", "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);
            
            List<CollectionRecordResponse> binRecords = Arrays.asList(record1, record2);

            // Mock service to return bin's collection history
            when(collectionService.getCollectionRecordsByBin(binId)).thenReturn(binRecords);

            // WHEN: Making GET request for bin's collections
            // THEN: Should return HTTP 200 OK with bin's collection history
            mockMvc.perform(get("/api/collections/bin/{binId}", binId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].binId", is(binId)))
                    .andExpect(jsonPath("$[1].binId", is(binId)));

            // Verify service method was called
            verify(collectionService, times(1)).getCollectionRecordsByBin(binId);
        }

        @Test
        @DisplayName("Additional: GET /status/{status} returns collections by status")
        void testGetCollectionRecordsByStatus() throws Exception {
            // GIVEN: Collections with COLLECTED status
            CollectionRecordResponse collected1 = createMockCollectionResponse(
                "BIN001", "WORKER001", "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);
            CollectionRecordResponse collected2 = createMockCollectionResponse(
                "BIN002", "WORKER001", "456 Oak Ave", CollectionRecord.CollectionStatus.COLLECTED);
            
            List<CollectionRecordResponse> collectedRecords = Arrays.asList(collected1, collected2);

            // Mock service to return COLLECTED status records
            when(collectionService.getCollectionRecordsByStatus(CollectionRecord.CollectionStatus.COLLECTED))
                    .thenReturn(collectedRecords);

            // WHEN: Making GET request for COLLECTED status
            // THEN: Should return HTTP 200 OK with filtered records
            mockMvc.perform(get("/api/collections/status/{status}", "COLLECTED")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].status", is("COLLECTED")))
                    .andExpect(jsonPath("$[1].status", is("COLLECTED")));

            // Verify service method was called
            verify(collectionService, times(1))
                    .getCollectionRecordsByStatus(CollectionRecord.CollectionStatus.COLLECTED);
        }

        @Test
        @DisplayName("Additional: GET /worker/{workerId}/stats returns collection statistics")
        void testGetCollectionStatsByWorker() throws Exception {
            // GIVEN: Worker statistics
            String workerId = "WORKER001";
            CollectionService.CollectionStats stats = new CollectionService.CollectionStats(
                100,  // totalCollections
                85,   // collectedCount
                10,   // overrideCount
                5,    // missedCount
                1250.5 // totalWeight
            );

            // Mock service to return statistics
            when(collectionService.getCollectionStatsByWorker(workerId)).thenReturn(stats);

            // WHEN: Making GET request for worker stats
            // THEN: Should return HTTP 200 OK with statistics
            mockMvc.perform(get("/api/collections/worker/{workerId}/stats", workerId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.totalCollections", is(100)))
                    .andExpect(jsonPath("$.collectedCount", is(85)))
                    .andExpect(jsonPath("$.overrideCount", is(10)))
                    .andExpect(jsonPath("$.missedCount", is(5)))
                    .andExpect(jsonPath("$.totalWeight", is(1250.5)));

            // Verify service method was called
            verify(collectionService, times(1)).getCollectionStatsByWorker(workerId);
        }

        @Test
        @DisplayName("Additional: GET /bin/{binId}/collected-today checks if bin collected")
        void testIsBinAlreadyCollectedToday() throws Exception {
            // GIVEN: A bin that was already collected today
            String binId = "BIN001";
            
            // Mock service to return true (already collected)
            when(collectionService.isBinAlreadyCollectedToday(binId)).thenReturn(true);

            // WHEN: Making GET request to check if bin collected today
            // THEN: Should return HTTP 200 OK with collected status
            mockMvc.perform(get("/api/collections/bin/{binId}/collected-today", binId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.binId", is(binId)))
                    .andExpect(jsonPath("$.alreadyCollected", is(true)))
                    .andExpect(jsonPath("$.date").exists());

            // Verify service method was called
            verify(collectionService, times(1)).isBinAlreadyCollectedToday(binId);
        }

        @Test
        @DisplayName("Additional: GET /bin/{binId}/latest returns most recent collection")
        void testGetLatestCollectionRecordByBin() throws Exception {
            // GIVEN: A bin with collection history
            String binId = "BIN001";
            CollectionRecordResponse latestRecord = createMockCollectionResponse(
                binId, "WORKER001", "123 Main St", CollectionRecord.CollectionStatus.COLLECTED);

            // Mock service to return latest record
            when(collectionService.getLatestCollectionRecordByBin(binId)).thenReturn(latestRecord);

            // WHEN: Making GET request for latest collection
            // THEN: Should return HTTP 200 OK with latest record
            mockMvc.perform(get("/api/collections/bin/{binId}/latest", binId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.binId", is(binId)))
                    .andExpect(jsonPath("$.workerId", is("WORKER001")))
                    .andExpect(jsonPath("$.status", is("COLLECTED")));

            // Verify service method was called
            verify(collectionService, times(1)).getLatestCollectionRecordByBin(binId);
        }

        @Test
        @DisplayName("Additional: DELETE /reset deletes collection record successfully")
        void testResetCollectionRecord() throws Exception {
            // GIVEN: A valid reset request with binId and workerId
            String binId = "BIN001";
            String workerId = "WORKER001";
            String requestBody = String.format("{\"binId\":\"%s\",\"workerId\":\"%s\"}", binId, workerId);

            // Mock service - doNothing for delete operation
            doNothing().when(collectionService).deleteCollectionRecord(binId, workerId);

            // WHEN: Making DELETE request to reset collection
            // THEN: Should return HTTP 200 OK with success message
            mockMvc.perform(delete("/api/collections/reset")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message", is("Collection record deleted successfully")))
                    .andExpect(jsonPath("$.binId", is(binId)))
                    .andExpect(jsonPath("$.workerId", is(workerId)))
                    .andExpect(jsonPath("$.timestamp").exists());

            // Verify service method was called
            verify(collectionService, times(1)).deleteCollectionRecord(binId, workerId);
        }

        @Test
        @DisplayName("Additional: DELETE /reset returns bad request for missing parameters")
        void testResetCollectionRecord_MissingParameters() throws Exception {
            // GIVEN: A request missing workerId
            String requestBody = "{\"binId\":\"BIN001\"}";

            // WHEN: Making DELETE request with incomplete data
            // THEN: Should return HTTP 400 Bad Request
            mockMvc.perform(delete("/api/collections/reset")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.error", is("binId and workerId are required")));

            // Verify service method was NOT called
            verify(collectionService, never()).deleteCollectionRecord(anyString(), anyString());
        }

        @Test
        @DisplayName("Additional: GET /health returns service health status")
        void testHealthCheck() throws Exception {
            // WHEN: Making GET request to health endpoint
            // THEN: Should return HTTP 200 OK with health status
            mockMvc.perform(get("/api/collections/health")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is("UP")))
                    .andExpect(jsonPath("$.service", is("Collection Service")))
                    .andExpect(jsonPath("$.timestamp").exists());

            // No service method verification needed for health check
        }

        @Test
        @DisplayName("Branch Coverage: DELETE /reset with only binId null")
        void testResetCollectionRecord_NullBinId() throws Exception {
            // GIVEN: Request with null binId but valid workerId
            String requestBody = "{\"binId\": null, \"workerId\": \"WORKER001\"}";

            // WHEN & THEN: Should return HTTP 400 Bad Request
            mockMvc.perform(delete("/api/collections/reset")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("binId and workerId are required")));

            verify(collectionService, never()).deleteCollectionRecord(anyString(), anyString());
        }

        @Test
        @DisplayName("Branch Coverage: DELETE /reset with only workerId null")
        void testResetCollectionRecord_NullWorkerId() throws Exception {
            // GIVEN: Request with valid binId but null workerId
            String requestBody = "{\"binId\": \"BIN001\", \"workerId\": null}";

            // WHEN & THEN: Should return HTTP 400 Bad Request
            mockMvc.perform(delete("/api/collections/reset")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("binId and workerId are required")));

            verify(collectionService, never()).deleteCollectionRecord(anyString(), anyString());
        }

        @Test
        @DisplayName("Branch Coverage: DELETE /reset with both parameters null")
        void testResetCollectionRecord_BothNull() throws Exception {
            // GIVEN: Request with both parameters null
            String requestBody = "{\"binId\": null, \"workerId\": null}";

            // WHEN & THEN: Should return HTTP 400 Bad Request
            mockMvc.perform(delete("/api/collections/reset")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error", is("binId and workerId are required")));

            verify(collectionService, never()).deleteCollectionRecord(anyString(), anyString());
        }
    }

    // ====================================
    // HELPER METHODS
    // ====================================

    /**
     * Helper method to create mock CollectionRecordResponse
     */
    private CollectionRecordResponse createMockCollectionResponse(
            String binId, String workerId, String location, CollectionRecord.CollectionStatus status) {
        
        return new CollectionRecordResponse(
            "record_" + binId,           // id
            binId,                       // binId
            workerId,                    // workerId
            LocalDateTime.now(),         // collectionDate
            location,                    // binLocation
            "OWNER001",                  // binOwner
            15.5,                        // weight
            80,                          // fillLevel
            "GENERAL",                   // wasteType
            status,                      // status
            null,                        // reason
            null,                        // sensorData
            LocalDateTime.now().minusHours(1), // createdAt
            LocalDateTime.now()          // updatedAt
        );
    }
}
