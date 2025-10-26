package com.csse.smartwaste.common.util;

import com.csse.smartwaste.bin.dto.BinResponse;
import com.csse.smartwaste.bin.entity.Bin;
import com.csse.smartwaste.collection.dto.CollectionRecordRequest;
import com.csse.smartwaste.collection.entity.CollectionRecord;
import com.csse.smartwaste.common.model.Role;
import com.csse.smartwaste.login.entity.User;

import java.time.LocalDateTime;

/**
 * Test Data Builder - Utility class to build test objects
 * 
 * PURPOSE:
 * - Provides reusable methods to create test data objects
 * - Reduces code duplication in test classes
 * - Makes tests more readable and maintainable
 * - Follows the Builder pattern for test data creation
 * 
 * USAGE:
 * - Use static methods to create pre-configured test objects
 * - Customize objects after creation if needed
 */
public class TestDataBuilder {

    // ====================================
    // COLLECTION RECORD BUILDERS
    // ====================================

    /**
     * Build a valid collection record request for testing
     * Represents a typical successful collection scenario
     */
    public static CollectionRecordRequest buildValidCollectionRequest() {
        CollectionRecordRequest request = new CollectionRecordRequest();
        request.setBinId("BIN001");
        request.setWorkerId("WORKER001");
        request.setBinLocation("123 Main Street, Colombo");
        request.setBinOwner("OWNER001");
        request.setWeight(15.5);
        request.setFillLevel(80);
        request.setWasteType("GENERAL");
        request.setStatus(CollectionRecord.CollectionStatus.COLLECTED);
        return request;
    }

    /**
     * Build a collection request with sensor data
     * Used for testing sensor data integration (PTC-05)
     */
    public static CollectionRecordRequest buildRequestWithSensorData() {
        CollectionRecordRequest request = buildValidCollectionRequest();
        CollectionRecordRequest.SensorDataRequest sensorData = 
            new CollectionRecordRequest.SensorDataRequest(25.5, 85, "STRONG");
        request.setSensorData(sensorData);
        return request;
    }

    /**
     * Build a collection request with invalid bin ID
     * Used for negative testing (NTC-03)
     */
    public static CollectionRecordRequest buildInvalidBinRequest() {
        CollectionRecordRequest request = buildValidCollectionRequest();
        request.setBinId("INVALID_BIN_999");
        return request;
    }

    /**
     * Build a collection request with zero fill level
     * Used for edge case testing (ETC-02)
     */
    public static CollectionRecordRequest buildZeroFillLevelRequest() {
        CollectionRecordRequest request = buildValidCollectionRequest();
        request.setFillLevel(0);
        request.setWeight(0.0);
        return request;
    }

    /**
     * Build a collection request with full (100%) fill level
     * Used for edge case testing (ETC-02)
     */
    public static CollectionRecordRequest buildFullFillLevelRequest() {
        CollectionRecordRequest request = buildValidCollectionRequest();
        request.setFillLevel(100);
        request.setWeight(50.0);
        return request;
    }

    /**
     * Build a collection request without sensor data (simulating sensor failure)
     * Used for exception testing (EXC-02)
     */
    public static CollectionRecordRequest buildRequestWithoutSensorData() {
        CollectionRecordRequest request = buildValidCollectionRequest();
        request.setSensorData(null);
        return request;
    }

    // ====================================
    // BIN ENTITY BUILDERS
    // ====================================

    /**
     * Build an active bin entity for testing
     * Represents a bin ready for collection
     */
    public static Bin buildActiveBin(String binId) {
        Bin bin = new Bin();
        bin.setBinId(binId);
        bin.setOwnerId("OWNER001");
        bin.setStatus(Bin.BinStatus.ACTIVE);
        bin.setTag(new Bin.BinTag("RFID", "TAG_" + binId));
        bin.setLatitude(6.9271);
        bin.setLongitude(79.8612);
        bin.setAddress("123 Main Street, Colombo");
        bin.setCreatedAt(LocalDateTime.now().minusDays(30));
        bin.setUpdatedAt(LocalDateTime.now());
        return bin;
    }

    /**
     * Build a collected bin entity
     * Represents a bin that was already collected
     */
    public static Bin buildCollectedBin(String binId) {
        Bin bin = buildActiveBin(binId);
        bin.setStatus(Bin.BinStatus.COLLECTED);
        return bin;
    }

    /**
     * Build a damaged bin entity
     * Used for negative testing (bin not in ACTIVE status)
     */
    public static Bin buildDamagedBin(String binId) {
        Bin bin = buildActiveBin(binId);
        bin.setStatus(Bin.BinStatus.DAMAGED);
        return bin;
    }

    // ====================================
    // BIN RESPONSE DTO BUILDERS
    // ====================================

    /**
     * Build a bin response DTO for mocking service responses
     */
    public static BinResponse buildBinResponse(String binId, Bin.BinStatus status) {
        BinResponse response = new BinResponse();
        response.setBinId(binId);
        response.setOwnerId("OWNER001");
        response.setStatus(status);
        response.setLatitude(6.9271);
        response.setLongitude(79.8612);
        response.setAddress("123 Main Street, Colombo");
        response.setCreatedAt(LocalDateTime.now().minusDays(30));
        response.setUpdatedAt(LocalDateTime.now());
        
        BinResponse.BinTagResponse tag = new BinResponse.BinTagResponse("RFID", "TAG_" + binId);
        response.setTag(tag);
        
        return response;
    }

    /**
     * Build an active bin response
     */
    public static BinResponse buildActiveBinResponse(String binId) {
        return buildBinResponse(binId, Bin.BinStatus.ACTIVE);
    }

    // ====================================
    // COLLECTION RECORD ENTITY BUILDERS
    // ====================================

    /**
     * Build a collection record entity
     * Used for testing repository operations
     */
    public static CollectionRecord buildCollectionRecord(String binId, String workerId, 
                                                         CollectionRecord.CollectionStatus status) {
        CollectionRecord record = new CollectionRecord();
        record.setBinId(binId);
        record.setWorkerId(workerId);
        record.setBinLocation("123 Main Street, Colombo");
        record.setBinOwner("OWNER001");
        record.setWeight(15.5);
        record.setFillLevel(80);
        record.setWasteType("GENERAL");
        record.setStatus(status);
        record.setCollectionDate(LocalDateTime.now());
        
        // Add sensor data
        CollectionRecord.SensorData sensorData = new CollectionRecord.SensorData(25.5, 85, "STRONG");
        record.setSensorData(sensorData);
        
        return record;
    }

    /**
     * Build a collected record
     */
    public static CollectionRecord buildCollectedRecord(String binId, String workerId) {
        return buildCollectionRecord(binId, workerId, CollectionRecord.CollectionStatus.COLLECTED);
    }

    /**
     * Build a missed collection record
     */
    public static CollectionRecord buildMissedRecord(String binId, String workerId) {
        CollectionRecord record = buildCollectionRecord(binId, workerId, CollectionRecord.CollectionStatus.MISSED);
        record.setReason("Bin not accessible");
        return record;
    }

    // ====================================
    // USER ENTITY BUILDERS
    // ====================================

    /**
     * Build a worker user for testing
     */
    public static User buildWorkerUser(String userId, String name) {
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setEmail(name.toLowerCase().replace(" ", ".") + "@smartwaste.com");
        user.setRole(Role.Worker);
        user.setPhone("+94771234567");
        user.setStatus(User.UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now().minusMonths(6));
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    /**
     * Build a resident user for testing
     */
    public static User buildResidentUser(String userId, String name) {
        User user = buildWorkerUser(userId, name);
        user.setRole(Role.Resident);
        return user;
    }

    /**
     * Build an admin user for testing
     */
    public static User buildAdminUser(String userId, String name) {
        User user = buildWorkerUser(userId, name);
        user.setRole(Role.Admin);
        return user;
    }

    // ====================================
    // UTILITY METHODS
    // ====================================

    /**
     * Build a list of collection records for a worker
     * Used for testing statistics and progress calculations (PTC-07, ETC-01)
     */
    public static CollectionRecord[] buildMultipleCollectionRecords(String workerId, int totalBins, int collectedCount) {
        CollectionRecord[] records = new CollectionRecord[totalBins];
        
        for (int i = 0; i < totalBins; i++) {
            String binId = "BIN" + String.format("%03d", i + 1);
            CollectionRecord.CollectionStatus status = i < collectedCount 
                ? CollectionRecord.CollectionStatus.COLLECTED 
                : CollectionRecord.CollectionStatus.MISSED;
            records[i] = buildCollectionRecord(binId, workerId, status);
        }
        
        return records;
    }

    /**
     * Build collection records for route completion testing (ETC-01)
     */
    public static CollectionRecord[] buildRouteCollectionRecords(String workerId, int totalBins) {
        return buildMultipleCollectionRecords(workerId, totalBins, totalBins);
    }
}
