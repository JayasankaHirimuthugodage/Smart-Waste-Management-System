import React from 'react';

/**
 * ActivityItem Component
 * Simple component for displaying individual activity items in dashboards
 * Takes title, subtitle, and status props
 */
const ActivityItem = ({ title, subtitle, status }) => {
  const getStatusColor = (status) => {
    const colors = {
      pending: 'bg-yellow-100 text-yellow-800',
      in_progress: 'bg-blue-100 text-blue-800',
      completed: 'bg-green-100 text-green-800',
      resolved: 'bg-green-100 text-green-800',
      cancelled: 'bg-red-100 text-red-800',
      failed: 'bg-red-100 text-red-800',
    };
    return colors[status] || 'bg-gray-100 text-gray-800';
  };

  const getStatusIcon = (status) => {
    const icons = {
      pending: '⏳',
      in_progress: '🔄',
      completed: '✅',
      resolved: '✅',
      cancelled: '❌',
      failed: '❌',
    };
    return icons[status] || '📋';
  };

  return (
    <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
      <div className="flex items-center space-x-3">
        <span className="text-lg">{getStatusIcon(status)}</span>
        <div>
          <p className="font-medium text-gray-900">{title}</p>
          <p className="text-sm text-gray-600">{subtitle}</p>
        </div>
      </div>
      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(status)}`}>
        {status}
      </span>
    </div>
  );
};

export default ActivityItem;