package com.csse.smartwaste.common.util;

import com.csse.smartwaste.collection.dto.CollectionRecordRequest;
import com.csse.smartwaste.collection.entity.CollectionRecord;

/**
 * Mock Data Factory - Generates mock data for unit testing
 * 
 * PURPOSE:
 * - Provides mock sensor data for testing (PTC-05, EXC-02)
 * - Simulates sensor failures and edge cases
 * - Creates predictable test data for assertions
 * - Separates mock data generation from test logic
 * 
 * USAGE:
 * - Used in conjunction with Mockito for mocking external dependencies
 * - Provides consistent mock data across different test cases
 */
public class MockDataFactory {

    // ====================================
    // SENSOR DATA MOCKS
    // ====================================

    /**
     * Create mock sensor data with typical values
     * Used for PTC-05: testSensorDataGenerationMocked
     * 
     * @return SensorDataRequest with realistic sensor values
     */
    public static CollectionRecordRequest.SensorDataRequest createMockSensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            25.5,      // temperature in Celsius
            85,        // battery level percentage
            "STRONG"   // signal strength
        );
    }

    /**
     * Create mock sensor data with low battery
     * Used for edge case testing
     * 
     * @return SensorDataRequest with low battery warning
     */
    public static CollectionRecordRequest.SensorDataRequest createLowBatterySensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            26.0,      // temperature
            15,        // low battery (15%)
            "WEAK"     // weak signal
        );
    }

    /**
     * Create mock sensor data with extreme temperature
     * Used for edge case testing
     * 
     * @return SensorDataRequest with high temperature
     */
    public static CollectionRecordRequest.SensorDataRequest createHighTemperatureSensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            45.0,      // extreme temperature
            90,        // battery level
            "STRONG"   // signal strength
        );
    }

    /**
     * Create mock sensor data with perfect conditions
     * Used for positive testing
     * 
     * @return SensorDataRequest with optimal values
     */
    public static CollectionRecordRequest.SensorDataRequest createOptimalSensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            24.0,      // ideal temperature
            100,       // full battery
            "EXCELLENT" // excellent signal
        );
    }

    /**
     * Simulate sensor failure by returning null
     * Used for EXC-02: testSensorFailureFallback
     * 
     * @return null to simulate sensor failure
     */
    public static CollectionRecordRequest.SensorDataRequest createFailedSensorData() {
        return null; // Simulates sensor not responding
    }

    // ====================================
    // COLLECTION RECORD SENSOR DATA MOCKS
    // ====================================

    /**
     * Create mock CollectionRecord.SensorData for entity testing
     * 
     * @return SensorData with typical values
     */
    public static CollectionRecord.SensorData createMockRecordSensorData() {
        return new CollectionRecord.SensorData(
            25.5,      // temperature
            85,        // battery level
            "STRONG"   // signal strength
        );
    }

    /**
     * Create mock sensor data with zero battery (sensor dying)
     * 
     * @return SensorData indicating sensor failure imminent
     */
    public static CollectionRecord.SensorData createDyingSensorData() {
        return new CollectionRecord.SensorData(
            0.0,       // no temperature reading
            0,         // dead battery
            "NONE"     // no signal
        );
    }

    // ====================================
    // WEIGHT AND FILL LEVEL MOCKS
    // ====================================

    /**
     * Create mock data for empty bin (ETC-02)
     * 
     * @return Array [weight, fillLevel] representing empty bin
     */
    public static Double[] createEmptyBinMockData() {
        return new Double[] { 0.0, 0.0 }; // [weight, fillLevel]
    }

    /**
     * Create mock data for full bin (ETC-02)
     * 
     * @return Array [weight, fillLevel] representing full bin
     */
    public static Double[] createFullBinMockData() {
        return new Double[] { 50.0, 100.0 }; // [weight, fillLevel]
    }

    /**
     * Create mock data for half-full bin
     * 
     * @return Array [weight, fillLevel] representing half-full bin
     */
    public static Double[] createHalfFullBinMockData() {
        return new Double[] { 25.0, 50.0 }; // [weight, fillLevel]
    }

    /**
     * Create mock data for nearly full bin (threshold testing)
     * 
     * @return Array [weight, fillLevel] at collection threshold
     */
    public static Double[] createThresholdBinMockData() {
        return new Double[] { 37.5, 75.0 }; // [weight, fillLevel] at 75% threshold
    }

    // ====================================
    // RAPID SCAN SIMULATION
    // ====================================

    /**
     * Create array of mock sensor data for rapid scan testing (ETC-04)
     * Simulates multiple bins being scanned quickly in succession
     * 
     * @param count Number of rapid scans to simulate
     * @return Array of SensorDataRequest objects
     */
    public static CollectionRecordRequest.SensorDataRequest[] createRapidScanMockData(int count) {
        CollectionRecordRequest.SensorDataRequest[] scanData = 
            new CollectionRecordRequest.SensorDataRequest[count];
        
        for (int i = 0; i < count; i++) {
            scanData[i] = new CollectionRecordRequest.SensorDataRequest(
                24.0 + (i * 0.5),  // Slightly varying temperature
                90 - (i * 2),      // Decreasing battery from multiple scans
                "STRONG"           // Consistent signal
            );
        }
        
        return scanData;
    }

    // ====================================
    // COLLECTION STATUS MOCKS
    // ====================================

    /**
     * Get mock collection status for testing
     * 
     * @param isSuccessful Whether collection was successful
     * @return Appropriate CollectionStatus
     */
    public static CollectionRecord.CollectionStatus getMockCollectionStatus(boolean isSuccessful) {
        return isSuccessful 
            ? CollectionRecord.CollectionStatus.COLLECTED 
            : CollectionRecord.CollectionStatus.MISSED;
    }

    /**
     * Get override status for manual collection
     * 
     * @return OVERRIDE status
     */
    public static CollectionRecord.CollectionStatus getMockOverrideStatus() {
        return CollectionRecord.CollectionStatus.OVERRIDE;
    }

    // ====================================
    // ERROR SIMULATION HELPERS
    // ====================================

    /**
     * Simulate corrupted sensor data
     * Used for error handling tests
     * 
     * @return SensorDataRequest with invalid/corrupted values
     */
    public static CollectionRecordRequest.SensorDataRequest createCorruptedSensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            -999.0,    // Invalid temperature
            -1,        // Invalid battery level
            "INVALID"  // Invalid signal strength
        );
    }

    /**
     * Create sensor data that would cause validation errors
     * 
     * @return SensorDataRequest with boundary-breaking values
     */
    public static CollectionRecordRequest.SensorDataRequest createInvalidSensorData() {
        return new CollectionRecordRequest.SensorDataRequest(
            999.0,     // Temperature too high
            150,       // Battery over 100%
            null       // Null signal strength
        );
    }

    // ====================================
    // UTILITY METHODS
    // ====================================

    /**
     * Check if sensor data indicates a failure condition
     * 
     * @param sensorData The sensor data to check
     * @return true if sensor data indicates failure
     */
    public static boolean isSensorDataFailed(CollectionRecordRequest.SensorDataRequest sensorData) {
        if (sensorData == null) {
            return true;
        }
        if (sensorData.getBatteryLevel() != null && sensorData.getBatteryLevel() == 0) {
            return true;
        }
        if (sensorData.getSignalStrength() != null && sensorData.getSignalStrength().equals("NONE")) {
            return true;
        }
        return false;
    }

    /**
     * Create sensor data based on fill level (realistic simulation)
     * Higher fill level = slightly higher temperature due to decomposition
     * 
     * @param fillLevel The bin fill level (0-100)
     * @return Realistic sensor data based on fill level
     */
    public static CollectionRecordRequest.SensorDataRequest createRealisticSensorData(int fillLevel) {
        double temperature = 24.0 + (fillLevel * 0.05); // Temperature increases with fill
        int battery = 100 - (fillLevel / 10); // Battery drains with sensor usage
        String signal = fillLevel < 50 ? "STRONG" : "MODERATE"; // Signal may weaken when full
        
        return new CollectionRecordRequest.SensorDataRequest(temperature, battery, signal);
    }
}
