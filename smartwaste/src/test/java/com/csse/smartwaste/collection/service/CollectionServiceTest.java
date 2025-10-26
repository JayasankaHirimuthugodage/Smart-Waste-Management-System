package com.csse.smartwaste.collection.service;

import com.csse.smartwaste.bin.dto.BinResponse;
import com.csse.smartwaste.bin.entity.Bin;
import com.csse.smartwaste.bin.service.BinService;
import com.csse.smartwaste.collection.dto.CollectionRecordRequest;
import com.csse.smartwaste.collection.dto.CollectionRecordResponse;
import com.csse.smartwaste.collection.entity.CollectionRecord;
import com.csse.smartwaste.collection.repository.CollectionRecordRepository;
import com.csse.smartwaste.common.exception.DuplicateResourceException;
import com.csse.smartwaste.common.exception.ResourceNotFoundException;
import com.csse.smartwaste.common.util.MockDataFactory;
import com.csse.smartwaste.common.util.TestDataBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Collection Service Test Suite
 * 
 * PURPOSE:
 * - Comprehensive unit tests for CollectionService
 * - Covers all 4 test categories: Positive, Negative, Edge, Exception
 * - Achieves >80% code coverage for CollectionService
 * - Demonstrates proper mocking with Mockito
 * - Uses nested test classes for organization (Pattern 2)
 * 
 * TEST CATEGORIES:
 * - Positive Test Cases (5 tests): Main flow scenarios
 * - Negative Test Cases (2 tests): Invalid input handling
 * - Edge Cases (3 tests): Boundary conditions
 * - Exception/Error Cases (2 tests): Error handling
 * 
 * TOTAL: 12 comprehensive test methods
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Collection Service Test Suite")
class CollectionServiceTest {

    @Mock
    private CollectionRecordRepository collectionRecordRepository;

    @Mock
    private BinService binService;

    @InjectMocks
    private CollectionService collectionService;

    // ====================================
    // 🟢 POSITIVE TEST CASES (Main Flow)
    // ====================================

    @Nested
    @DisplayName("Positive Test Cases - Main Flow")
    class PositiveTestCases {

        @BeforeEach
        void setUpPositiveTests() {
            // Default mock for bin service - return a valid bin response
            lenient().when(binService.getBinByBinId(anyString()))
                .thenReturn(TestDataBuilder.buildActiveBinResponse("BIN001"));
            
            // Default mock for duplicate check - no duplicates
            lenient().when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(false);
        }

        @Test
        @DisplayName("PTC-03: Location tracking after selection - Location data is saved correctly")
        void testLocationTrackingAfterSelection() {
            // GIVEN: A collection request with location data
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            String expectedLocation = "123 Main Street, Colombo";
            request.setBinLocation(expectedLocation);

            // Mock bin service to return active bin
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);

            // Mock repository to return false (bin not collected today)
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // Mock repository save - capture and return what was saved
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                CollectionRecord record = invocation.getArgument(0);
                record.setId("record_123"); // Set an ID as would happen in real save
                return record;
            });

            // WHEN: Creating collection record
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Location data should be correctly saved
            assertNotNull(response, "Response should not be null");
            assertEquals(expectedLocation, response.getBinLocation(), 
                "Location should be saved correctly");
            
            // Verify bin service was called to validate bin
            verify(binService, times(1)).getBinByBinId("BIN001");
            
            // Verify repository save was called
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("PTC-04: Valid bin scan validation - Bin exists in database and validation passes")
        void testValidBinScanValidation() {
            // GIVEN: A valid bin ID that exists in the database
            String validBinId = "BIN001";
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setBinId(validBinId);

            // Mock bin service to return active bin (validation passes)
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse(validBinId);
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);

            // Mock duplicate check (not collected today)
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // Mock repository save - return saved record
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                CollectionRecord record = invocation.getArgument(0);
                record.setId("record_123");
                return record;
            });

            // WHEN: Creating collection record with valid bin
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Validation should pass and record should be created
            assertNotNull(response, "Response should not be null");
            assertEquals(validBinId, response.getBinId(), "Bin ID should match");
            assertEquals(CollectionRecord.CollectionStatus.COLLECTED, response.getStatus(), 
                "Status should be COLLECTED");

            // Verify bin validation was called
            verify(binService, times(1)).getBinByBinId(validBinId);
            
            // Verify bin status was updated after successful collection
            verify(binService, times(1)).updateBinStatus(validBinId, Bin.BinStatus.COLLECTED);
        }

        @Test
        @DisplayName("PTC-05: Sensor data generation (Mocked) - Mock sensor returns weight, fill level, and location")
        void testSensorDataGenerationMocked() {
            // GIVEN: A collection request with mocked sensor data
            CollectionRecordRequest request = TestDataBuilder.buildRequestWithSensorData();
            CollectionRecordRequest.SensorDataRequest mockSensorData = MockDataFactory.createMockSensorData();
            request.setSensorData(mockSensorData);

            // Mock bin service
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);

            // Mock duplicate check
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // Mock repository save with sensor data
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                CollectionRecord record = invocation.getArgument(0);
                record.setId("record_123");
                return record;
            });

            // WHEN: Creating collection record with mocked sensor data
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Sensor data should be properly saved
            assertNotNull(response, "Response should not be null");
            assertNotNull(response.getSensorData(), "Sensor data should not be null");
            assertEquals(25.5, response.getSensorData().getTemperature(), 
                "Temperature should match mock data");
            assertEquals(85, response.getSensorData().getBatteryLevel(), 
                "Battery level should match mock data");
            assertEquals("STRONG", response.getSensorData().getSignalStrength(), 
                "Signal strength should match mock data");

            // Verify weight and fill level from request
            assertEquals(15.5, response.getWeight(), "Weight should match");
            assertEquals(80, response.getFillLevel(), "Fill level should match");

            // Verify repository save was called
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("PTC-07: Route progress update - Route progress and percentage calculated correctly")
        void testRouteProgressUpdate() {
            // GIVEN: A worker with multiple collection records (route in progress)
            String workerId = "WORKER001";
            int totalBins = 10;
            int collectedCount = 7;
            
            // Create mock collection records: 7 collected, 3 missed
            CollectionRecord[] records = TestDataBuilder.buildMultipleCollectionRecords(
                workerId, totalBins, collectedCount);
            List<CollectionRecord> recordList = Arrays.asList(records);

            // Mock repository to return worker's collection records
            when(collectionRecordRepository.findByWorkerId(anyString())).thenReturn(recordList);

            // WHEN: Getting collection statistics (route progress)
            CollectionService.CollectionStats stats = collectionService.getCollectionStatsByWorker(workerId);

            // THEN: Progress should be calculated correctly
            assertNotNull(stats, "Stats should not be null");
            assertEquals(totalBins, stats.getTotalCollections(), 
                "Total collections should match");
            assertEquals(collectedCount, stats.getCollectedCount(), 
                "Collected count should be " + collectedCount);
            assertEquals(totalBins - collectedCount, stats.getMissedCount(), 
                "Missed count should be " + (totalBins - collectedCount));

            // Calculate and verify percentage
            double expectedPercentage = (double) collectedCount / totalBins * 100;
            double actualPercentage = (double) stats.getCollectedCount() / stats.getTotalCollections() * 100;
            assertEquals(expectedPercentage, actualPercentage, 0.01, 
                "Route progress percentage should be 70%");

            // Verify repository was called
            verify(collectionRecordRepository, times(1)).findByWorkerId(workerId);
        }

        @Test
        @DisplayName("PTC-08: End of shift summary generation - Summary generated correctly at shift end")
        void testEndOfShiftSummaryGeneration() {
            // GIVEN: A worker finishing their shift with collection data
            String workerId = "WORKER001";
            
            // Create shift data: 8 collected, 1 override, 1 missed
            CollectionRecord[] records = new CollectionRecord[10];
            for (int i = 0; i < 8; i++) {
                records[i] = TestDataBuilder.buildCollectedRecord("BIN" + (i + 1), workerId);
                records[i].setWeight(15.0 + i); // Varying weights
            }
            records[8] = TestDataBuilder.buildCollectionRecord("BIN009", workerId, 
                CollectionRecord.CollectionStatus.OVERRIDE);
            records[9] = TestDataBuilder.buildMissedRecord("BIN010", workerId);
            
            List<CollectionRecord> recordList = Arrays.asList(records);

            // Mock repository
            when(collectionRecordRepository.findByWorkerId(anyString())).thenReturn(recordList);

            // WHEN: Generating end-of-shift summary
            CollectionService.CollectionStats summary = collectionService.getCollectionStatsByWorker(workerId);

            // THEN: Summary should contain all shift statistics
            assertNotNull(summary, "Summary should not be null");
            assertEquals(10, summary.getTotalCollections(), "Total collections in shift");
            assertEquals(8, summary.getCollectedCount(), "Successfully collected bins");
            assertEquals(1, summary.getOverrideCount(), "Override collections");
            assertEquals(1, summary.getMissedCount(), "Missed collections");
            assertTrue(summary.getTotalWeight() > 0, "Total weight should be calculated");

            // Verify shift completion rate
            double completionRate = (double) (summary.getCollectedCount() + summary.getOverrideCount()) 
                / summary.getTotalCollections() * 100;
            assertEquals(90.0, completionRate, 0.01, "Shift completion rate should be 90%");

            // Verify repository was called
            verify(collectionRecordRepository, times(1)).findByWorkerId(workerId);
        }
    }

    // ====================================
    // ❌ NEGATIVE TEST CASES (Invalid Input)
    // ====================================

    @Nested
    @DisplayName("Negative Test Cases - Invalid Input")
    class NegativeTestCases {

        @Test
        @DisplayName("NTC-03: Unregistered bin tag scan - Invalid bin ID should be rejected")
        void testUnregisteredBinTagScan() {
            // GIVEN: A collection request with invalid/unregistered bin ID
            String invalidBinId = "INVALID_BIN_999";
            CollectionRecordRequest request = TestDataBuilder.buildInvalidBinRequest();
            request.setBinId(invalidBinId);

            // Mock bin service to throw ResourceNotFoundException (bin doesn't exist)
            when(binService.getBinByBinId(anyString()))
                    .thenThrow(new ResourceNotFoundException("Bin", "binId", invalidBinId));

            // WHEN & THEN: Creating collection record should throw ResourceNotFoundException
            ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> collectionService.createCollectionRecord(request),
                "Should throw ResourceNotFoundException for invalid bin ID"
            );

            // Verify exception message
            assertTrue(exception.getMessage().contains(invalidBinId), 
                "Exception message should contain the invalid bin ID");

            // Verify bin service was called for validation
            verify(binService, times(1)).getBinByBinId(invalidBinId);

            // Verify repository save was never called (validation failed)
            verify(collectionRecordRepository, never()).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("NTC-04: Duplicate scan warning - Duplicate scan should return warning and not double record")
        void testDuplicateScanWarning() {
            // GIVEN: A bin that was already collected today
            String binId = "BIN001";
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setBinId(binId);

            // Mock repository to return true (bin already collected today)
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(true);

            // WHEN & THEN: Attempting duplicate collection should throw DuplicateResourceException
            DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> collectionService.createCollectionRecord(request),
                "Should throw DuplicateResourceException for duplicate scan"
            );

            // Verify exception message
            assertTrue(exception.getMessage().contains("already collected today"), 
                "Exception message should mention duplicate collection");
            assertTrue(exception.getMessage().contains(binId), 
                "Exception message should contain the bin ID");

            // Verify duplicate check was performed
            verify(collectionRecordRepository, times(1))
                .existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class));

            // Verify repository save was never called (duplicate prevented)
            verify(collectionRecordRepository, never()).save(any(CollectionRecord.class));
            
            // Verify bin status was never updated (no duplicate)
            verify(binService, never()).updateBinStatus(anyString(), any(Bin.BinStatus.class));
        }
    }

    // ====================================
    // 🪜 EDGE CASES (Boundary Conditions)
    // ====================================

    @Nested
    @DisplayName("Edge Cases - Boundary Conditions")
    class EdgeCases {

        @Test
        @DisplayName("ETC-01: Final bin scan completes route - Last bin scan should mark route as 100%")
        void testFinalBinScanCompletesRoute() {
            // GIVEN: A worker collecting the last bin in their route
            String workerId = "WORKER001";
            int totalBins = 10;
            
            // Create route where all bins are collected (100% completion)
            CollectionRecord[] allCollectedRecords = TestDataBuilder.buildRouteCollectionRecords(
                workerId, totalBins);
            List<CollectionRecord> recordList = Arrays.asList(allCollectedRecords);

            // Mock repository
            when(collectionRecordRepository.findByWorkerId(anyString())).thenReturn(recordList);

            // WHEN: Getting statistics after final bin collection
            CollectionService.CollectionStats stats = collectionService.getCollectionStatsByWorker(workerId);

            // THEN: Route should be 100% complete
            assertNotNull(stats, "Stats should not be null");
            assertEquals(totalBins, stats.getTotalCollections(), "All bins in route");
            assertEquals(totalBins, stats.getCollectedCount(), "All bins collected");
            assertEquals(0, stats.getMissedCount(), "No missed collections");

            // Calculate completion percentage
            double completionPercentage = (double) stats.getCollectedCount() / stats.getTotalCollections() * 100;
            assertEquals(100.0, completionPercentage, 0.01, 
                "Route completion should be exactly 100%");

            // Verify repository was called
            verify(collectionRecordRepository, times(1)).findByWorkerId(workerId);
        }

        @Test
        @DisplayName("ETC-02: Fill level zero or full - Edge fill levels (0% and 100%) still process correctly")
        void testFillLevelZeroOrFull() {
            // GIVEN: Two requests with edge fill levels (0% and 100%)
            
            // Test Case 1: Empty bin (0% fill level)
            CollectionRecordRequest emptyBinRequest = TestDataBuilder.buildZeroFillLevelRequest();
            
            // Mock bin service for empty bin
            BinResponse binResponse1 = TestDataBuilder.buildActiveBinResponse("BIN001");
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse1);
            
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                CollectionRecord record = invocation.getArgument(0);
                record.setId("record_empty");
                return record;
            });

            // WHEN: Creating collection for empty bin
            CollectionRecordResponse emptyResponse = collectionService.createCollectionRecord(emptyBinRequest);

            // THEN: Empty bin should be processed successfully
            assertNotNull(emptyResponse, "Empty bin response should not be null");
            assertEquals(0, emptyResponse.getFillLevel(), "Fill level should be 0");
            assertEquals(0.0, emptyResponse.getWeight(), "Weight should be 0.0");

            // Test Case 2: Full bin (100% fill level)
            CollectionRecordRequest fullBinRequest = TestDataBuilder.buildFullFillLevelRequest();
            fullBinRequest.setBinId("BIN002");
            
            // Mock bin service for full bin
            BinResponse binResponse2 = TestDataBuilder.buildActiveBinResponse("BIN002");
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse2);
            
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // WHEN: Creating collection for full bin
            CollectionRecordResponse fullResponse = collectionService.createCollectionRecord(fullBinRequest);

            // THEN: Full bin should be processed successfully
            assertNotNull(fullResponse, "Full bin response should not be null");
            assertEquals(100, fullResponse.getFillLevel(), "Fill level should be 100");
            assertEquals(50.0, fullResponse.getWeight(), "Weight should be 50.0");

            // Verify both edge cases were processed
            verify(collectionRecordRepository, times(2)).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("ETC-04: Rapid multiple bin scans - Rapid consecutive scans handled without failure")
        void testRapidMultipleBinScans() {
            // GIVEN: Multiple rapid consecutive bin scans (simulating fast worker)
            int rapidScanCount = 5;
            
            for (int i = 0; i < rapidScanCount; i++) {
                String binId = "BIN" + String.format("%03d", i + 1);
                CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
                request.setBinId(binId);
                request.setWorkerId("WORKER001");

                // Mock bin service for each bin
                BinResponse binResponse = TestDataBuilder.buildActiveBinResponse(binId);
                when(binService.getBinByBinId(anyString())).thenReturn(binResponse);

                // Mock duplicate check (not collected)
                when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                        anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                        .thenReturn(false);

                // Mock repository save
                when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                    CollectionRecord record = invocation.getArgument(0);
                    record.setId("record_" + binId);
                    return record;
                });

                // WHEN: Creating rapid collection records
                CollectionRecordResponse response = collectionService.createCollectionRecord(request);

                // THEN: Each rapid scan should be processed successfully
                assertNotNull(response, "Response for rapid scan " + (i + 1) + " should not be null");
                assertEquals(binId, response.getBinId(), "Bin ID should match for scan " + (i + 1));
            }

            // Verify all rapid scans were saved
            verify(collectionRecordRepository, times(rapidScanCount)).save(any(CollectionRecord.class));
            
            // Verify bin validation was called for each scan
            verify(binService, atLeast(rapidScanCount)).getBinByBinId(anyString());
            
            // Verify no exceptions were thrown during rapid scanning
            // (test passed without failures)
        }
    }

    // ====================================
    // ⚠️ EXCEPTION / ERROR HANDLING CASES
    // ====================================

    @Nested
    @DisplayName("Exception/Error Handling Cases")
    class ExceptionHandlingCases {

        @Test
        @DisplayName("EXC-01: Invalid bin tag error handling - Invalid tag should throw and be caught with proper message")
        void testInvalidBinTagErrorHandling() {
            // GIVEN: A collection request with invalid bin tag
            String invalidBinId = "INVALID_TAG_XYZ";
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setBinId(invalidBinId);

            // Mock bin service to throw ResourceNotFoundException with specific message
            ResourceNotFoundException expectedException = new ResourceNotFoundException(
                "Bin", "binId", invalidBinId);
            when(binService.getBinByBinId(anyString())).thenThrow(expectedException);

            // WHEN & THEN: Invalid bin tag should throw ResourceNotFoundException
            ResourceNotFoundException thrownException = assertThrows(
                ResourceNotFoundException.class,
                () -> collectionService.createCollectionRecord(request),
                "Should throw ResourceNotFoundException for invalid bin tag"
            );

            // Verify exception has proper error message
            assertNotNull(thrownException.getMessage(), "Exception should have error message");
            assertTrue(thrownException.getMessage().contains("Bin"), 
                "Error message should mention 'Bin'");
            assertTrue(thrownException.getMessage().contains(invalidBinId), 
                "Error message should contain the invalid bin ID");

            // Verify error handling prevented further processing
            verify(binService, times(1)).getBinByBinId(invalidBinId);
            verify(collectionRecordRepository, never()).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("EXC-02: Sensor failure fallback - Simulate sensor failure and verify fallback/manual handling")
        void testSensorFailureFallback() {
            // GIVEN: A collection request without sensor data (sensor failed)
            CollectionRecordRequest request = TestDataBuilder.buildRequestWithoutSensorData();
            request.setSensorData(null); // Simulating sensor failure
            
            // Set manual data as fallback
            request.setWeight(12.0); // Manual weight measurement
            request.setFillLevel(75); // Manual fill level estimate

            // Mock bin service
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);

            // Mock duplicate check
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // Mock repository save (without sensor data)
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenAnswer(invocation -> {
                CollectionRecord record = invocation.getArgument(0);
                record.setId("record_manual");
                return record;
            });

            // WHEN: Creating collection record with sensor failure
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Collection should succeed with manual fallback data
            assertNotNull(response, "Response should not be null");
            assertNull(response.getSensorData(), 
                "Sensor data should be null (sensor failed)");
            
            // Verify manual fallback data was used
            assertEquals(12.0, response.getWeight(), 
                "Manual weight should be saved as fallback");
            assertEquals(75, response.getFillLevel(), 
                "Manual fill level should be saved as fallback");
            
            // Verify collection status is still COLLECTED
            assertEquals(CollectionRecord.CollectionStatus.COLLECTED, response.getStatus(), 
                "Collection should succeed despite sensor failure");

            // Verify repository save was called (fallback succeeded)
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
            
            // Verify bin status was updated (collection completed)
            verify(binService, times(1)).updateBinStatus("BIN001", Bin.BinStatus.COLLECTED);
        }
    }

    // =====================================================
    // 🔵 ADDITIONAL COVERAGE TESTS (Boost to 80%+)
    // =====================================================

    @Nested
    @DisplayName("Additional Coverage Tests - Query Methods")
    class AdditionalCoverageTests {

        @Test
        @DisplayName("ACT-01: Get collection records by worker ID - Should return all records for worker")
        void testGetCollectionRecordsByWorker() {
            // GIVEN: Multiple collection records for a worker
            String workerId = "WORKER001";
            List<CollectionRecord> mockRecords = Arrays.asList(
                TestDataBuilder.buildCollectedRecord("BIN001", workerId),
                TestDataBuilder.buildCollectedRecord("BIN002", workerId),
                TestDataBuilder.buildCollectedRecord("BIN003", workerId)
            );

            when(collectionRecordRepository.findByWorkerId(workerId)).thenReturn(mockRecords);

            // WHEN: Getting records by worker ID
            List<CollectionRecordResponse> responses = collectionService.getCollectionRecordsByWorker(workerId);

            // THEN: Should return all 3 records
            assertNotNull(responses, "Response list should not be null");
            assertEquals(3, responses.size(), "Should return 3 collection records");
            
            // Verify repository was called correctly
            verify(collectionRecordRepository, times(1)).findByWorkerId(workerId);
        }

        @Test
        @DisplayName("ACT-02: Get today's collection records by worker - Should return only today's records")
        void testGetTodayCollectionRecordsByWorker() {
            // GIVEN: Collection records for today
            String workerId = "WORKER001";
            List<CollectionRecord> mockTodayRecords = Arrays.asList(
                TestDataBuilder.buildCollectedRecord("BIN001", workerId),
                TestDataBuilder.buildCollectedRecord("BIN002", workerId)
            );

            when(collectionRecordRepository.findTodayCollectionsByWorker(
                    eq(workerId), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(mockTodayRecords);

            // WHEN: Getting today's records
            List<CollectionRecordResponse> responses = collectionService.getTodayCollectionRecordsByWorker(workerId);

            // THEN: Should return today's records only
            assertNotNull(responses, "Response list should not be null");
            assertEquals(2, responses.size(), "Should return 2 today's records");
            
            // Verify repository was called with correct date range
            verify(collectionRecordRepository, times(1)).findTodayCollectionsByWorker(
                    eq(workerId), any(LocalDateTime.class), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("ACT-03: Get collection records by bin ID - Should return all records for a bin")
        void testGetCollectionRecordsByBin() {
            // GIVEN: Multiple collection records for a bin
            String binId = "BIN001";
            List<CollectionRecord> mockRecords = Arrays.asList(
                TestDataBuilder.buildCollectedRecord(binId, "WORKER001"),
                TestDataBuilder.buildCollectedRecord(binId, "WORKER002")
            );

            when(collectionRecordRepository.findByBinId(binId)).thenReturn(mockRecords);

            // WHEN: Getting records by bin ID
            List<CollectionRecordResponse> responses = collectionService.getCollectionRecordsByBin(binId);

            // THEN: Should return all records for this bin
            assertNotNull(responses, "Response list should not be null");
            assertEquals(2, responses.size(), "Should return 2 collection records");
            
            // Verify all responses have the correct bin ID
            responses.forEach(response -> 
                assertEquals(binId, response.getBinId(), "All records should be for BIN001"));
            
            // Verify repository was called correctly
            verify(collectionRecordRepository, times(1)).findByBinId(binId);
        }

        @Test
        @DisplayName("ACT-04: Get collection records by status - Should filter by COLLECTED status")
        void testGetCollectionRecordsByStatus() {
            // GIVEN: Collection records with COLLECTED status
            List<CollectionRecord> mockCollectedRecords = Arrays.asList(
                TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001"),
                TestDataBuilder.buildCollectedRecord("BIN002", "WORKER001")
            );

            when(collectionRecordRepository.findByStatus(CollectionRecord.CollectionStatus.COLLECTED))
                    .thenReturn(mockCollectedRecords);

            // WHEN: Getting records by COLLECTED status
            List<CollectionRecordResponse> responses = collectionService.getCollectionRecordsByStatus(
                    CollectionRecord.CollectionStatus.COLLECTED);

            // THEN: Should return only COLLECTED records
            assertNotNull(responses, "Response list should not be null");
            assertEquals(2, responses.size(), "Should return 2 COLLECTED records");
            
            // Verify all records have COLLECTED status
            responses.forEach(response -> 
                assertEquals(CollectionRecord.CollectionStatus.COLLECTED, response.getStatus(),
                    "All records should have COLLECTED status"));
            
            // Verify repository was called correctly
            verify(collectionRecordRepository, times(1))
                    .findByStatus(CollectionRecord.CollectionStatus.COLLECTED);
        }

        @Test
        @DisplayName("ACT-05: Check if bin already collected today - Should return true for collected bin")
        void testIsBinAlreadyCollectedToday() {
            // GIVEN: Bin was already collected today
            String binId = "BIN001";
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    eq(binId), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(true);

            // WHEN: Checking if bin was collected today
            boolean isCollected = collectionService.isBinAlreadyCollectedToday(binId);

            // THEN: Should return true
            assertTrue(isCollected, "Should return true for already collected bin");
            
            // Verify repository was called correctly
            verify(collectionRecordRepository, times(1))
                    .existsByBinIdAndCollectionDateBetweenAndStatusIn(
                            eq(binId), any(LocalDateTime.class), any(LocalDateTime.class));
        }

        @Test
        @DisplayName("ACT-06: Get latest collection record by bin - Should return most recent record")
        void testGetLatestCollectionRecordByBin() {
            // GIVEN: Latest collection record exists for bin
            String binId = "BIN001";
            CollectionRecord latestRecord = TestDataBuilder.buildCollectedRecord(binId, "WORKER001");
            
            when(collectionRecordRepository.findFirstByBinIdOrderByCollectionDateDesc(binId))
                    .thenReturn(java.util.Optional.of(latestRecord));

            // WHEN: Getting latest record
            CollectionRecordResponse response = collectionService.getLatestCollectionRecordByBin(binId);

            // THEN: Should return the latest record
            assertNotNull(response, "Response should not be null");
            assertEquals(binId, response.getBinId(), "Bin ID should match");
            
            // Verify repository was called correctly
            verify(collectionRecordRepository, times(1))
                    .findFirstByBinIdOrderByCollectionDateDesc(binId);
        }

        @Test
        @DisplayName("ACT-07: Get latest record throws exception - Should throw when no records found")
        void testGetLatestCollectionRecordByBin_NotFound() {
            // GIVEN: No records exist for bin
            String binId = "NONEXISTENT_BIN";
            when(collectionRecordRepository.findFirstByBinIdOrderByCollectionDateDesc(binId))
                    .thenReturn(java.util.Optional.empty());

            // WHEN & THEN: Should throw ResourceNotFoundException
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> collectionService.getLatestCollectionRecordByBin(binId),
                    "Should throw ResourceNotFoundException when no records found");
            
            // Verify exception message
            assertTrue(exception.getMessage().contains(binId),
                    "Exception message should contain the bin ID");
            
            // Verify repository was called
            verify(collectionRecordRepository, times(1))
                    .findFirstByBinIdOrderByCollectionDateDesc(binId);
        }

        @Test
        @DisplayName("ACT-08: Delete collection record - Should delete record and update bin status")
        void testDeleteCollectionRecord() {
            // GIVEN: Collection record exists for bin and worker
            String binId = "BIN001";
            String workerId = "WORKER001";
            List<CollectionRecord> existingRecords = Arrays.asList(
                TestDataBuilder.buildCollectedRecord(binId, workerId)
            );

            when(collectionRecordRepository.findByBinId(binId)).thenReturn(existingRecords);
            doNothing().when(collectionRecordRepository).deleteByBinIdAndWorkerId(binId, workerId);
            when(binService.updateBinStatus(binId, Bin.BinStatus.ACTIVE))
                    .thenReturn(TestDataBuilder.buildBinResponse(binId, Bin.BinStatus.ACTIVE));

            // WHEN: Deleting collection record
            assertDoesNotThrow(() -> collectionService.deleteCollectionRecord(binId, workerId),
                    "Should delete record without throwing exception");

            // THEN: Verify deletion and status update
            verify(collectionRecordRepository, times(1)).findByBinId(binId);
            verify(collectionRecordRepository, times(1)).deleteByBinIdAndWorkerId(binId, workerId);
            verify(binService, times(1)).updateBinStatus(binId, Bin.BinStatus.ACTIVE);
        }

        @Test
        @DisplayName("ACT-09: Delete non-existent record - Should throw ResourceNotFoundException")
        void testDeleteCollectionRecord_NotFound() {
            // GIVEN: No records exist for bin
            String binId = "NONEXISTENT_BIN";
            String workerId = "WORKER001";
            
            when(collectionRecordRepository.findByBinId(binId)).thenReturn(Arrays.asList());

            // WHEN & THEN: Should throw ResourceNotFoundException
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> collectionService.deleteCollectionRecord(binId, workerId),
                    "Should throw ResourceNotFoundException when no records found");
            
            // Verify exception message
            assertTrue(exception.getMessage().contains(binId),
                    "Exception message should contain the bin ID");
            
            // Verify only findByBinId was called, not delete operations
            verify(collectionRecordRepository, times(1)).findByBinId(binId);
            verify(collectionRecordRepository, never()).deleteByBinIdAndWorkerId(anyString(), anyString());
        }
    }

    // ====================================
    // 🎯 BRANCH COVERAGE TESTS
    // ====================================

    @Nested
    @DisplayName("Branch Coverage Tests - Null Handling & Edge Cases")
    class BranchCoverageTests {

        @Test
        @DisplayName("BCT-01: Create record with null reason - Should handle null reason branch")
        void testCreateCollectionRecord_WithNullReason() {
            // GIVEN: A collection request with null reason
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setReason(null); // Explicitly set reason to null
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock dependencies
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class))).thenReturn(binResponse);

            // WHEN: Creating collection record
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Record should be created successfully with null reason
            assertNotNull(response, "Response should not be null");
            
            // Verify the saved record had null reason (testing the if (request.getReason() != null) branch)
            verify(collectionRecordRepository, times(1)).save(argThat(record -> 
                record.getReason() == null // This tests the false branch of the null check
            ));
        }

        @Test
        @DisplayName("BCT-02: Create record with null sensor data - Should handle null sensor data branch")
        void testCreateCollectionRecord_WithNullSensorData() {
            // GIVEN: A collection request with null sensor data
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setSensorData(null); // Explicitly set sensor data to null
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock dependencies
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class))).thenReturn(binResponse);

            // WHEN: Creating collection record
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Record should be created successfully with null sensor data
            assertNotNull(response, "Response should not be null");
            
            // Verify the saved record had null sensor data (testing the if (request.getSensorData() != null) branch)
            verify(collectionRecordRepository, times(1)).save(argThat(record -> 
                record.getSensorData() == null // This tests the false branch of the null check
            ));
        }

        @Test
        @DisplayName("BCT-03: Worker with no records - Should handle empty records list")
        void testGetCollectionStatsByWorker_EmptyRecords() {
            // GIVEN: A worker with no collection records
            String workerId = "WORKER_NO_RECORDS";
            
            when(collectionRecordRepository.findByWorkerId(workerId))
                    .thenReturn(Arrays.asList()); // Empty list

            // WHEN: Getting worker collection stats
            CollectionService.CollectionStats stats = collectionService.getCollectionStatsByWorker(workerId);

            // THEN: Should return stats with zero counts (testing the if (records.isEmpty()) branch)
            assertNotNull(stats, "Stats should not be null");
            assertEquals(0, stats.getTotalCollections(), "Total collections should be 0");
            assertEquals(0, stats.getCollectedCount(), "Collected count should be 0");
            assertEquals(0, stats.getOverrideCount(), "Override count should be 0");
            assertEquals(0, stats.getMissedCount(), "Missed count should be 0");
            assertEquals(0.0, stats.getTotalWeight(), "Total weight should be 0.0");

            // Verify repository was called
            verify(collectionRecordRepository, times(1)).findByWorkerId(workerId);
        }

        @Test
        @DisplayName("BCT-04: Create record with both null reason and null sensor data")
        void testCreateCollectionRecord_WithBothNulls() {
            // GIVEN: A collection request with both null reason and null sensor data
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setReason(null);
            request.setSensorData(null);
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock dependencies
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class))).thenReturn(binResponse);

            // WHEN: Creating collection record
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Record should be created successfully with both nulls
            assertNotNull(response, "Response should not be null");
            
            // Verify both null branches were tested
            verify(collectionRecordRepository, times(1)).save(argThat(record -> 
                record.getReason() == null && record.getSensorData() == null
            ));
        }

        @Test
        @DisplayName("BCT-05: Bin not in ACTIVE status - Should throw IllegalStateException")
        void testCreateCollectionRecord_BinNotActive() {
            // GIVEN: A collection request for COLLECTED status but bin is DAMAGED (not ACTIVE)
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setStatus(CollectionRecord.CollectionStatus.COLLECTED);
            
            // Mock bin service to return a DAMAGED bin (not ACTIVE)
            BinResponse binResponse = TestDataBuilder.buildBinResponse("BIN001", Bin.BinStatus.DAMAGED);
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);

            // WHEN & THEN: Should throw IllegalStateException (testing the bin status validation branch)
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> collectionService.createCollectionRecord(request),
                    "Should throw IllegalStateException when bin is not ACTIVE");
            
            // Verify exception message
            assertTrue(exception.getMessage().contains("is not in ACTIVE status"),
                    "Exception message should indicate bin status issue");
            assertTrue(exception.getMessage().contains("DAMAGED"),
                    "Exception message should show current status");
            
            // Verify bin was checked but record was NOT saved
            verify(binService, times(1)).getBinByBinId("BIN001");
            verify(collectionRecordRepository, never()).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("BCT-06: Bin status update fails - Should continue with record creation")
        void testCreateCollectionRecord_BinStatusUpdateFails() {
            // GIVEN: A valid collection request
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setStatus(CollectionRecord.CollectionStatus.COLLECTED);
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock dependencies
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            
            // Mock bin status update to throw exception (testing the catch block at line 100)
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class)))
                    .thenThrow(new RuntimeException("Network error updating bin status"));

            // WHEN: Creating collection record
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Record should still be created successfully despite bin status update failure
            assertNotNull(response, "Response should not be null even if bin status update fails");
            assertEquals("BIN001", response.getBinId());
            
            // Verify record was saved even though bin status update failed
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
            verify(binService, times(1)).updateBinStatus("BIN001", Bin.BinStatus.COLLECTED);
        }

        @Test
        @DisplayName("BCT-07: Delete record with bin status update failure - Should continue with deletion")
        void testDeleteCollectionRecord_BinStatusUpdateFails() {
            // GIVEN: A valid bin and worker
            String binId = "BIN001";
            String workerId = "WORKER001";
            
            CollectionRecord record = TestDataBuilder.buildCollectedRecord(binId, workerId);
            when(collectionRecordRepository.findByBinId(binId)).thenReturn(Arrays.asList(record));
            
            // Mock bin status update to throw exception (testing the catch block at line 252)
            doThrow(new RuntimeException("Database connection error"))
                    .when(binService).updateBinStatus(binId, Bin.BinStatus.ACTIVE);

            // WHEN: Deleting collection record (should NOT throw exception)
            assertDoesNotThrow(() -> collectionService.deleteCollectionRecord(binId, workerId),
                    "Should not throw exception even if bin status update fails");

            // THEN: Record should still be deleted despite bin status update failure
            verify(collectionRecordRepository, times(1)).findByBinId(binId);
            verify(collectionRecordRepository, times(1)).deleteByBinIdAndWorkerId(binId, workerId);
            verify(binService, times(1)).updateBinStatus(binId, Bin.BinStatus.ACTIVE);
        }

        @Test
        @DisplayName("BCT-08: Create OVERRIDE record - Should update bin status like COLLECTED")
        void testCreateCollectionRecord_OverrideStatus() {
            // GIVEN: A collection request with OVERRIDE status
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setStatus(CollectionRecord.CollectionStatus.OVERRIDE);
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock dependencies
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(false);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class))).thenReturn(binResponse);

            // WHEN: Creating collection record with OVERRIDE status
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Record should be created and bin status updated (testing OR branch: COLLECTED || OVERRIDE)
            assertNotNull(response, "Response should not be null");
            
            // Verify bin status was updated to COLLECTED (same as COLLECTED status)
            verify(binService, times(1)).updateBinStatus("BIN001", Bin.BinStatus.COLLECTED);
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
        }

        @Test
        @DisplayName("BCT-09: Bin already collected but with OVERRIDE status - Should allow creation")
        void testCreateCollectionRecord_AlreadyCollectedButOverride() {
            // GIVEN: Bin was already collected today, but request has OVERRIDE status
            CollectionRecordRequest request = TestDataBuilder.buildValidCollectionRequest();
            request.setStatus(CollectionRecord.CollectionStatus.OVERRIDE);
            
            BinResponse binResponse = TestDataBuilder.buildActiveBinResponse("BIN001");
            CollectionRecord savedRecord = TestDataBuilder.buildCollectedRecord("BIN001", "WORKER001");

            // Mock that bin was already collected today
            when(collectionRecordRepository.existsByBinIdAndCollectionDateBetweenAndStatusIn(
                    anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(true);  // Already collected!
            
            when(binService.getBinByBinId(anyString())).thenReturn(binResponse);
            when(collectionRecordRepository.save(any(CollectionRecord.class))).thenReturn(savedRecord);
            when(binService.updateBinStatus(anyString(), any(Bin.BinStatus.class))).thenReturn(binResponse);

            // WHEN: Creating override record (testing AND branch: alreadyCollected && status != COLLECTED)
            CollectionRecordResponse response = collectionService.createCollectionRecord(request);

            // THEN: Should succeed because OVERRIDE status bypasses duplicate check
            assertNotNull(response, "Response should not be null");
            
            // Verify record was created despite bin being already collected
            verify(collectionRecordRepository, times(1)).save(any(CollectionRecord.class));
            verify(binService, times(1)).updateBinStatus("BIN001", Bin.BinStatus.COLLECTED);
        }
    }
}
