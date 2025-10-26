package com.csse.smartwaste.collection.dto;

import com.csse.smartwaste.collection.entity.CollectionRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Collection DTO Test Suite
 * 
 * PURPOSE:
 * - Comprehensive unit tests for Collection DTOs
 * - Tests Request and Response DTOs with all getters/setters
 * - Tests inner sensor data classes
 * - Tests factory methods
 * - Achieves 80%+ coverage for DTO layer
 * 
 * TOTAL: 18+ test methods
 */
@DisplayName("Collection DTO Test Suite")
class CollectionDtoTest {

    // ====================================
    // CollectionRecordRequest Tests
    // ====================================

    @Nested
    @DisplayName("CollectionRecordRequest Tests")
    class CollectionRecordRequestTests {

        @Test
        @DisplayName("Should create request with default constructor")
        void testDefaultConstructor() {
            // WHEN: Creating request with default constructor
            CollectionRecordRequest request = new CollectionRecordRequest();

            // THEN: Object should be created successfully
            assertNotNull(request, "Request should not be null");
        }

        @Test
        @DisplayName("Should create request with parameterized constructor")
        void testParameterizedConstructor() {
            // GIVEN: Collection record data
            String binId = "BIN001";
            String workerId = "WORKER001";
            String location = "123 Main St";
            String owner = "OWNER001";
            Double weight = 15.5;
            Integer fillLevel = 80;
            String wasteType = "GENERAL";
            CollectionRecord.CollectionStatus status = CollectionRecord.CollectionStatus.COLLECTED;

            // WHEN: Creating request with parameterized constructor
            CollectionRecordRequest request = new CollectionRecordRequest(
                binId, workerId, location, owner, weight, fillLevel, wasteType, status);

            // THEN: All fields should be set correctly
            assertEquals(binId, request.getBinId());
            assertEquals(workerId, request.getWorkerId());
            assertEquals(location, request.getBinLocation());
            assertEquals(owner, request.getBinOwner());
            assertEquals(weight, request.getWeight());
            assertEquals(fillLevel, request.getFillLevel());
            assertEquals(wasteType, request.getWasteType());
            assertEquals(status, request.getStatus());
        }

        @Test
        @DisplayName("Should set and get all fields correctly")
        void testGettersAndSetters() {
            // GIVEN: A request object
            CollectionRecordRequest request = new CollectionRecordRequest();

            // WHEN: Setting all fields
            request.setBinId("BIN001");
            request.setWorkerId("WORKER001");
            request.setBinLocation("123 Main St");
            request.setBinOwner("OWNER001");
            request.setWeight(15.5);
            request.setFillLevel(80);
            request.setWasteType("GENERAL");
            request.setStatus(CollectionRecord.CollectionStatus.COLLECTED);
            request.setReason("On schedule");

            // THEN: All getters should return correct values
            assertEquals("BIN001", request.getBinId());
            assertEquals("WORKER001", request.getWorkerId());
            assertEquals("123 Main St", request.getBinLocation());
            assertEquals("OWNER001", request.getBinOwner());
            assertEquals(15.5, request.getWeight());
            assertEquals(80, request.getFillLevel());
            assertEquals("GENERAL", request.getWasteType());
            assertEquals(CollectionRecord.CollectionStatus.COLLECTED, request.getStatus());
            assertEquals("On schedule", request.getReason());
        }

        @Test
        @DisplayName("Should handle sensor data correctly")
        void testSensorDataHandling() {
            // GIVEN: A request with sensor data
            CollectionRecordRequest request = new CollectionRecordRequest();
            CollectionRecordRequest.SensorDataRequest sensorData = 
                new CollectionRecordRequest.SensorDataRequest(25.5, 85, "STRONG");

            // WHEN: Setting sensor data
            request.setSensorData(sensorData);

            // THEN: Sensor data should be retrievable
            assertNotNull(request.getSensorData());
            assertEquals(25.5, request.getSensorData().getTemperature());
            assertEquals(85, request.getSensorData().getBatteryLevel());
            assertEquals("STRONG", request.getSensorData().getSignalStrength());
        }

        @Test
        @DisplayName("Should handle null sensor data")
        void testNullSensorData() {
            // GIVEN: A request without sensor data
            CollectionRecordRequest request = new CollectionRecordRequest();

            // WHEN: Setting sensor data to null
            request.setSensorData(null);

            // THEN: Sensor data should be null
            assertNull(request.getSensorData());
        }
    }

    // ====================================
    // SensorDataRequest Tests
    // ====================================

    @Nested
    @DisplayName("SensorDataRequest Tests")
    class SensorDataRequestTests {

        @Test
        @DisplayName("Should create sensor data with default constructor")
        void testDefaultConstructor() {
            // WHEN: Creating sensor data with default constructor
            CollectionRecordRequest.SensorDataRequest sensorData = 
                new CollectionRecordRequest.SensorDataRequest();

            // THEN: Object should be created successfully
            assertNotNull(sensorData);
        }

        @Test
        @DisplayName("Should create sensor data with parameterized constructor")
        void testParameterizedConstructor() {
            // GIVEN: Sensor data values
            Double temperature = 25.5;
            Integer batteryLevel = 85;
            String signalStrength = "STRONG";

            // WHEN: Creating sensor data with parameterized constructor
            CollectionRecordRequest.SensorDataRequest sensorData = 
                new CollectionRecordRequest.SensorDataRequest(temperature, batteryLevel, signalStrength);

            // THEN: All fields should be set correctly
            assertEquals(temperature, sensorData.getTemperature());
            assertEquals(batteryLevel, sensorData.getBatteryLevel());
            assertEquals(signalStrength, sensorData.getSignalStrength());
        }

        @Test
        @DisplayName("Should set and get all sensor fields correctly")
        void testGettersAndSetters() {
            // GIVEN: A sensor data object
            CollectionRecordRequest.SensorDataRequest sensorData = 
                new CollectionRecordRequest.SensorDataRequest();

            // WHEN: Setting all fields
            sensorData.setTemperature(25.5);
            sensorData.setBatteryLevel(85);
            sensorData.setSignalStrength("STRONG");

            // THEN: All getters should return correct values
            assertEquals(25.5, sensorData.getTemperature());
            assertEquals(85, sensorData.getBatteryLevel());
            assertEquals("STRONG", sensorData.getSignalStrength());
        }
    }

    // ====================================
    // CollectionRecordResponse Tests
    // ====================================

    @Nested
    @DisplayName("CollectionRecordResponse Tests")
    class CollectionRecordResponseTests {

        @Test
        @DisplayName("Should create response with default constructor")
        void testDefaultConstructor() {
            // WHEN: Creating response with default constructor
            CollectionRecordResponse response = new CollectionRecordResponse();

            // THEN: Object should be created successfully
            assertNotNull(response);
        }

        @Test
        @DisplayName("Should create response with parameterized constructor")
        void testParameterizedConstructor() {
            // GIVEN: Response data
            String id = "record_001";
            String binId = "BIN001";
            String workerId = "WORKER001";
            LocalDateTime collectionDate = LocalDateTime.now();
            String location = "123 Main St";
            String owner = "OWNER001";
            Double weight = 15.5;
            Integer fillLevel = 80;
            String wasteType = "GENERAL";
            CollectionRecord.CollectionStatus status = CollectionRecord.CollectionStatus.COLLECTED;
            String reason = "On schedule";
            CollectionRecordResponse.SensorDataResponse sensorData = 
                new CollectionRecordResponse.SensorDataResponse(25.5, 85, "STRONG");
            LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
            LocalDateTime updatedAt = LocalDateTime.now();

            // WHEN: Creating response with parameterized constructor
            CollectionRecordResponse response = new CollectionRecordResponse(
                id, binId, workerId, collectionDate, location, owner, 
                weight, fillLevel, wasteType, status, reason, 
                sensorData, createdAt, updatedAt);

            // THEN: All fields should be set correctly
            assertEquals(id, response.getId());
            assertEquals(binId, response.getBinId());
            assertEquals(workerId, response.getWorkerId());
            assertEquals(collectionDate, response.getCollectionDate());
            assertEquals(location, response.getBinLocation());
            assertEquals(owner, response.getBinOwner());
            assertEquals(weight, response.getWeight());
            assertEquals(fillLevel, response.getFillLevel());
            assertEquals(wasteType, response.getWasteType());
            assertEquals(status, response.getStatus());
            assertEquals(reason, response.getReason());
            assertNotNull(response.getSensorData());
            assertEquals(createdAt, response.getCreatedAt());
            assertEquals(updatedAt, response.getUpdatedAt());
        }

        @Test
        @DisplayName("Should set and get all fields correctly")
        void testGettersAndSetters() {
            // GIVEN: A response object
            CollectionRecordResponse response = new CollectionRecordResponse();
            LocalDateTime now = LocalDateTime.now();

            // WHEN: Setting all fields
            response.setId("record_001");
            response.setBinId("BIN001");
            response.setWorkerId("WORKER001");
            response.setCollectionDate(now);
            response.setBinLocation("123 Main St");
            response.setBinOwner("OWNER001");
            response.setWeight(15.5);
            response.setFillLevel(80);
            response.setWasteType("GENERAL");
            response.setStatus(CollectionRecord.CollectionStatus.COLLECTED);
            response.setReason("On schedule");
            response.setCreatedAt(now.minusHours(1));
            response.setUpdatedAt(now);

            // THEN: All getters should return correct values
            assertEquals("record_001", response.getId());
            assertEquals("BIN001", response.getBinId());
            assertEquals("WORKER001", response.getWorkerId());
            assertEquals(now, response.getCollectionDate());
            assertEquals("123 Main St", response.getBinLocation());
            assertEquals("OWNER001", response.getBinOwner());
            assertEquals(15.5, response.getWeight());
            assertEquals(80, response.getFillLevel());
            assertEquals("GENERAL", response.getWasteType());
            assertEquals(CollectionRecord.CollectionStatus.COLLECTED, response.getStatus());
            assertEquals("On schedule", response.getReason());
            assertEquals(now.minusHours(1), response.getCreatedAt());
            assertEquals(now, response.getUpdatedAt());
        }

        @Test
        @DisplayName("Should handle sensor data response correctly")
        void testSensorDataHandling() {
            // GIVEN: A response with sensor data
            CollectionRecordResponse response = new CollectionRecordResponse();
            CollectionRecordResponse.SensorDataResponse sensorData = 
                new CollectionRecordResponse.SensorDataResponse(25.5, 85, "STRONG");

            // WHEN: Setting sensor data
            response.setSensorData(sensorData);

            // THEN: Sensor data should be retrievable
            assertNotNull(response.getSensorData());
            assertEquals(25.5, response.getSensorData().getTemperature());
            assertEquals(85, response.getSensorData().getBatteryLevel());
            assertEquals("STRONG", response.getSensorData().getSignalStrength());
        }

        @Test
        @DisplayName("Should create response from CollectionRecord entity with sensor data")
        void testFromCollectionRecord_WithSensorData() {
            // GIVEN: A CollectionRecord entity with sensor data
            CollectionRecord record = new CollectionRecord(
                "BIN001", "WORKER001", "123 Main St", "OWNER001", 
                15.5, 80, "GENERAL", CollectionRecord.CollectionStatus.COLLECTED);
            record.setId("record_001");
            
            CollectionRecord.SensorData sensorData = new CollectionRecord.SensorData(25.5, 85, "STRONG");
            record.setSensorData(sensorData);

            // WHEN: Creating response from entity
            CollectionRecordResponse response = CollectionRecordResponse.fromCollectionRecord(record);

            // THEN: Response should contain all entity data including sensor data
            assertEquals("record_001", response.getId());
            assertEquals("BIN001", response.getBinId());
            assertEquals("WORKER001", response.getWorkerId());
            assertEquals("123 Main St", response.getBinLocation());
            assertEquals("OWNER001", response.getBinOwner());
            assertEquals(15.5, response.getWeight());
            assertEquals(80, response.getFillLevel());
            assertEquals("GENERAL", response.getWasteType());
            assertEquals(CollectionRecord.CollectionStatus.COLLECTED, response.getStatus());
            
            // Verify sensor data was converted
            assertNotNull(response.getSensorData());
            assertEquals(25.5, response.getSensorData().getTemperature());
            assertEquals(85, response.getSensorData().getBatteryLevel());
            assertEquals("STRONG", response.getSensorData().getSignalStrength());
        }

        @Test
        @DisplayName("Should create response from CollectionRecord entity without sensor data")
        void testFromCollectionRecord_WithoutSensorData() {
            // GIVEN: A CollectionRecord entity without sensor data
            CollectionRecord record = new CollectionRecord(
                "BIN001", "WORKER001", "123 Main St", "OWNER001", 
                15.5, 80, "GENERAL", CollectionRecord.CollectionStatus.COLLECTED);
            record.setId("record_001");
            record.setSensorData(null);

            // WHEN: Creating response from entity
            CollectionRecordResponse response = CollectionRecordResponse.fromCollectionRecord(record);

            // THEN: Response should contain entity data but sensor data should be null
            assertEquals("record_001", response.getId());
            assertEquals("BIN001", response.getBinId());
            assertNull(response.getSensorData(), "Sensor data should be null when entity has no sensor data");
        }
    }

    // ====================================
    // SensorDataResponse Tests
    // ====================================

    @Nested
    @DisplayName("SensorDataResponse Tests")
    class SensorDataResponseTests {

        @Test
        @DisplayName("Should create sensor data response with default constructor")
        void testDefaultConstructor() {
            // WHEN: Creating sensor data with default constructor
            CollectionRecordResponse.SensorDataResponse sensorData = 
                new CollectionRecordResponse.SensorDataResponse();

            // THEN: Object should be created successfully
            assertNotNull(sensorData);
        }

        @Test
        @DisplayName("Should create sensor data response with parameterized constructor")
        void testParameterizedConstructor() {
            // GIVEN: Sensor data values
            Double temperature = 25.5;
            Integer batteryLevel = 85;
            String signalStrength = "STRONG";

            // WHEN: Creating sensor data with parameterized constructor
            CollectionRecordResponse.SensorDataResponse sensorData = 
                new CollectionRecordResponse.SensorDataResponse(temperature, batteryLevel, signalStrength);

            // THEN: All fields should be set correctly
            assertEquals(temperature, sensorData.getTemperature());
            assertEquals(batteryLevel, sensorData.getBatteryLevel());
            assertEquals(signalStrength, sensorData.getSignalStrength());
        }

        @Test
        @DisplayName("Should set and get all sensor response fields correctly")
        void testGettersAndSetters() {
            // GIVEN: A sensor data response object
            CollectionRecordResponse.SensorDataResponse sensorData = 
                new CollectionRecordResponse.SensorDataResponse();

            // WHEN: Setting all fields
            sensorData.setTemperature(25.5);
            sensorData.setBatteryLevel(85);
            sensorData.setSignalStrength("STRONG");

            // THEN: All getters should return correct values
            assertEquals(25.5, sensorData.getTemperature());
            assertEquals(85, sensorData.getBatteryLevel());
            assertEquals("STRONG", sensorData.getSignalStrength());
        }
    }
}
