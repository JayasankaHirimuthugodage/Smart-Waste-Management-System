/**
 * Route Changes Data Constants
 * Frontend Only - No Backend Integration
 */

export const ROUTE_CHANGE_TYPES = [
  { value: 'all', label: 'All Types' },
  { value: 'schedule-change', label: 'Schedule Change' },
  { value: 'route-optimization', label: 'Route Optimization' },
  { value: 'area-reassignment', label: 'Area Reassignment' },
  { value: 'emergency-reroute', label: 'Emergency Reroute' },
  { value: 'vehicle-change', label: 'Vehicle Change' }
];

export const ROUTE_STATUSES = [
  { value: 'all', label: 'All Statuses' },
  { value: 'pending', label: 'Pending' },
  { value: 'approved', label: 'Approved' },
  { value: 'declined', label: 'Declined' },
  { value: 'implemented', label: 'Implemented' }
];

export const PRIORITY_LEVELS = [
  { value: 'all', label: 'All Priorities' },
  { value: 'low', label: 'Low' },
  { value: 'medium', label: 'Medium' },
  { value: 'high', label: 'High' },
  { value: 'critical', label: 'Critical' }
];

// Mock data for UI display
export const ROUTE_CHANGE_REQUESTS = [
  {
    id: 'RC001',
    requestedBy: 'John Anderson',
    role: 'Route Manager',
    route: 'Route A-12',
    area: 'Downtown',
    type: 'Schedule Change',
    currentSchedule: 'Mon, Wed, Fri - 8:00 AM',
    proposedSchedule: 'Tue, Thu, Sat - 9:00 AM',
    reason: 'Avoid traffic congestion during peak hours and improve collection efficiency',
    requestDate: '2024-01-15',
    status: 'pending',
    priority: 'medium',
    estimatedImpact: 'Reduced fuel consumption by 10%, Better accessibility',
    affectedHouseholds: 245
  },
  {
    id: 'RC002',
    requestedBy: 'Sarah Martinez',
    role: 'Operations Supervisor',
    route: 'Route B-7',
    area: 'Northside',
    type: 'Route Optimization',
    currentSchedule: 'Daily - 7:00 AM',
    proposedSchedule: 'Daily - 7:30 AM',
    reason: 'Optimize route path to reduce distance by 3km and fuel consumption by 15%',
    requestDate: '2024-01-14',
    status: 'approved',
    priority: 'high',
    estimatedImpact: '15% fuel savings, 20 minutes time reduction',
    affectedHouseholds: 189
  },
  {
    id: 'RC003',
    requestedBy: 'Mike Thompson',
    role: 'Area Coordinator',
    route: 'Route C-3',
    area: 'Eastside',
    type: 'Area Reassignment',
    currentSchedule: 'Mon, Wed, Fri - 10:00 AM',
    proposedSchedule: 'Tue, Thu - 10:00 AM',
    reason: 'Area expansion requires route split to maintain service quality',
    requestDate: '2024-01-13',
    status: 'declined',
    priority: 'low',
    estimatedImpact: 'Requires additional vehicle and crew',
    affectedHouseholds: 156
  },
  {
    id: 'RC004',
    requestedBy: 'Emily Davis',
    role: 'Emergency Coordinator',
    route: 'Route D-9',
    area: 'Westside',
    type: 'Emergency Reroute',
    currentSchedule: 'Daily - 6:00 AM',
    proposedSchedule: 'Daily - 6:00 AM (Alt Route)',
    reason: 'Road construction on main street - alternative route required immediately',
    requestDate: '2024-01-12',
    status: 'implemented',
    priority: 'critical',
    estimatedImpact: 'Temporary route, +15 min collection time',
    affectedHouseholds: 312
  },
  {
    id: 'RC005',
    requestedBy: 'Robert Wilson',
    role: 'Route Manager',
    route: 'Route E-15',
    area: 'Southside',
    type: 'Schedule Change',
    currentSchedule: 'Mon, Wed, Fri - 9:00 AM',
    proposedSchedule: 'Mon, Wed, Fri - 11:00 AM',
    reason: 'Multiple resident requests for later pickup time to reduce morning traffic disruption',
    requestDate: '2024-01-11',
    status: 'pending',
    priority: 'medium',
    estimatedImpact: 'Better resident satisfaction',
    affectedHouseholds: 278
  },
  {
    id: 'RC006',
    requestedBy: 'Lisa Chen',
    role: 'Safety Officer',
    route: 'Route F-22',
    area: 'Industrial Zone',
    type: 'Route Optimization',
    currentSchedule: 'Tue, Thu - 5:00 AM',
    proposedSchedule: 'Tue, Thu - 6:00 AM',
    reason: 'Worker safety concerns - improve visibility and reduce early morning risks',
    requestDate: '2024-01-10',
    status: 'approved',
    priority: 'high',
    estimatedImpact: 'Enhanced worker safety',
    affectedHouseholds: 98
  },
  {
    id: 'RC007',
    requestedBy: 'David Brown',
    role: 'Fleet Manager',
    route: 'Route G-8',
    area: 'Residential North',
    type: 'Vehicle Change',
    currentSchedule: 'Daily - 8:00 AM',
    proposedSchedule: 'Daily - 8:00 AM',
    reason: 'Assign larger capacity truck to handle increased waste volume',
    requestDate: '2024-01-09',
    status: 'pending',
    priority: 'medium',
    estimatedImpact: 'Better waste capacity management',
    affectedHouseholds: 234
  },
  {
    id: 'RC008',
    requestedBy: 'Jessica Lee',
    role: 'Route Manager',
    route: 'Route H-11',
    area: 'Downtown',
    type: 'Schedule Change',
    currentSchedule: 'Mon, Wed, Fri - 7:00 AM',
    proposedSchedule: 'Tue, Thu, Sat - 7:00 AM',
    reason: 'Balance workload across the week and reduce crew overtime',
    requestDate: '2024-01-08',
    status: 'implemented',
    priority: 'medium',
    estimatedImpact: 'Reduced overtime costs by 12%',
    affectedHouseholds: 201
  }
];

export const ROUTE_STATUS_STYLES = {
  pending: { bg: '#fef3c7', color: '#d97706', text: 'Pending' },
  approved: { bg: '#d1fae5', color: '#059669', text: 'Approved' },
  declined: { bg: '#fee2e2', color: '#dc2626', text: 'Declined' },
  implemented: { bg: '#dbeafe', color: '#2563eb', text: 'Implemented' }
};

export const PRIORITY_STYLES = {
  low: { bg: '#f3f4f6', color: '#6b7280', text: 'Low' },
  medium: { bg: '#fef3c7', color: '#d97706', text: 'Medium' },
  high: { bg: '#fed7aa', color: '#ea580c', text: 'High' },
  critical: { bg: '#fee2e2', color: '#dc2626', text: 'Critical' }
};