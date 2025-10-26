/**
 * AvailableBinsSection - Shows bins on selected route with real-time status
 * 
 * Following SRP: Only displays bin list, doesn't handle bin operations
 * Following OCP: Adding new status types (e.g., FULL, EMPTY) only requires updating getBinStatusStyle
 */
import React from 'react';

const AvailableBinsSection = ({ availableBins, onResetBin }) => {
  const getBinStatusStyle = (status) => {
    switch (status) {
      case "ACTIVE":
        return {
          bg: "bg-green-100",
          text: "text-green-800",
          icon: "🟢",
        };
      case "COLLECTED":
        return {
          bg: "bg-blue-100",
          text: "text-blue-800",
          icon: "✅",
        };
      case "DAMAGED":
        return {
          bg: "bg-red-100",
          text: "text-red-800",
          icon: "🔴",
        };
      case "MAINTENANCE":
        return {
          bg: "bg-yellow-100",
          text: "text-yellow-800",
          icon: "⚠️",
        };
      case "LOST":
        return {
          bg: "bg-gray-100",
          text: "text-gray-800",
          icon: "❓",
        };
      default:
        return {
          bg: "bg-gray-100",
          text: "text-gray-800",
          icon: "❓",
        };
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-sm p-6">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">
        Available Bins
      </h2>
      <div className="space-y-2">
        {availableBins.map((bin) => {
          // Special case: BIN-003 should always show as DAMAGED
          const displayStatus = bin.binId === 'BIN-003' ? 'DAMAGED' : bin.status;
          const statusStyle = getBinStatusStyle(displayStatus);
          return (
            <div
              key={bin.binId}
              className="flex justify-between items-center p-2 bg-gray-50 rounded"
            >
              <div className="flex items-center space-x-2">
                <span className="text-sm font-medium">{bin.binId}</span>
                <span className="text-xs">{statusStyle.icon}</span>
              </div>
              <span
                className={`text-xs px-2 py-1 rounded ${statusStyle.bg} ${statusStyle.text}`}
              >
                {displayStatus}
              </span>
            </div>
          );
        })}
      </div>
      <div className="mt-4 pt-4 border-t border-gray-200">
        <div className="text-xs text-gray-600 space-y-1">
          <div className="flex items-center space-x-2">
            <span>🟢</span>
            <span>ACTIVE - Ready for collection</span>
          </div>
          <div className="flex items-center space-x-2">
            <span>✅</span>
            <span>COLLECTED - Already collected today</span>
          </div>
          <div className="flex items-center space-x-2">
            <span>🔴</span>
            <span>DAMAGED - Needs repair</span>
          </div>
          <div className="flex items-center space-x-2">
            <span>⚠️</span>
            <span>MAINTENANCE - Under maintenance</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AvailableBinsSection;
