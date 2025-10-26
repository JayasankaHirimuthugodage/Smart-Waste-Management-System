/**
 * CollectionProgress
 * 
 * Displays real-time route progress, weight collected, and elapsed time.
 * SRP: Only responsible for rendering progress metrics - calculation done in parent
 * DIP: Uses ProgressIndicator component abstraction instead of direct implementation
 */
import React from 'react';
import { ProgressIndicator } from './CollectionComponents';

const MapPinIcon = ({ className }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 20 20">
    <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
  </svg>
);

const ScaleIcon = ({ className }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 20 20">
    <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clipRule="evenodd" />
  </svg>
);

const ClockIcon = ({ className }) => (
  <svg className={className} fill="currentColor" viewBox="0 0 20 20">
    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clipRule="evenodd" />
  </svg>
);

const CollectionProgress = ({ routeProgress, totalWeight, elapsedTime, selectedRoute }) => {
  return (
    <div className="bg-white rounded-lg shadow-sm p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold text-gray-900">
          Collection Progress
        </h2>
        {selectedRoute && (
          <div className="flex items-center space-x-2">
            <MapPinIcon className="w-4 h-4 text-green-600" />
            <span className="text-sm font-medium text-gray-700">
              {selectedRoute.name}
            </span>
            <span className="text-xs text-gray-500">
              ({selectedRoute.bins?.length || 0} bins)
            </span>
          </div>
        )}
      </div>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <ProgressIndicator
          label="Route Progress"
          current={routeProgress.collected}
          total={routeProgress.total}
          unit="bins"
          icon={MapPinIcon}
          color="green"
          showPercentage={true}
        />
        <ProgressIndicator
          label="Total Weight"
          current={totalWeight}
          total={1000}
          unit="kg"
          icon={ScaleIcon}
          color="green"
          showPercentage={false}
        />
        <ProgressIndicator
          label="Elapsed Time"
          current={elapsedTime}
          total="8h 0m"
          unit=""
          icon={ClockIcon}
          color="green"
          showPercentage={false}
        />
      </div>
    </div>
  );
};

export default CollectionProgress;
