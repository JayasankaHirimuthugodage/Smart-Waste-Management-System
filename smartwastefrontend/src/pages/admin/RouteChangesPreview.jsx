import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../components/dashboard/AdminDashboardLayout";
import {
  BarChart3,
  Package,
  Truck,
  Trash2,
  LineChart,
  MapPin,
  Clock,
  Check,
  X,
  AlertCircle,
} from "lucide-react";

const RouteChangesPreview = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [activeNav, setActiveNav] = useState("routes");

  const navItems = [
    { id: "reports", label: "Generate Reports", icon: BarChart3 },
    { id: "pickups", label: "Special Pickups", icon: Package },
    { id: "routes", label: "Route Changes", icon: Truck },
    { id: "bins", label: "Bin Requests", icon: Trash2 },
    { id: "analytics", label: "Analytics", icon: LineChart },
  ];

  const handleNavClick = (id) => {
    setActiveNav(id);
    const pathMap = {
      reports: "/admin/dashboard",
      pickups: "/admin/pickups",
      routes: "/admin/routes",
      bins: "/admin/bins",
      analytics: "/admin/analytics",
    };
    if (pathMap[id]) navigate(pathMap[id]);
  };

  // Dummy route change data
  const sampleRequests = [
    {
      id: "RC001",
      route: "Route A - Downtown",
      area: "Downtown Central",
      type: "Schedule Change",
      status: "Pending",
      priority: "Medium",
      date: "2025-10-10",
      requestedBy: "John Smith",
    },
    {
      id: "RC002",
      route: "Route B - Northside",
      area: "Northside",
      type: "Route Optimization",
      status: "Approved",
      priority: "High",
      date: "2025-10-09",
      requestedBy: "Sarah Johnson",
    },
    {
      id: "RC003",
      route: "Route C - East",
      area: "Eastside",
      type: "Emergency Reroute",
      status: "Declined",
      priority: "Critical",
      date: "2025-10-08",
      requestedBy: "Emily Davis",
    },
  ];

  const getStatusStyle = (status) => {
    const map = {
      Pending: { bg: "#fef3c7", color: "#d97706" },
      Approved: { bg: "#d1fae5", color: "#059669" },
      Declined: { bg: "#fee2e2", color: "#dc2626" },
    };
    return map[status] || map.Pending;
  };

  const getPriorityColor = (priority) => {
    const map = {
      Low: "#9ca3af",
      Medium: "#f59e0b",
      High: "#ea580c",
      Critical: "#dc2626",
    };
    return map[priority] || "#6b7280";
  };

  return (
    <DashboardLayout
      navItems={navItems}
      activeNav={activeNav}
      onNavClick={handleNavClick}
      logo="Admin"
      user={user}
      onLogout={logout}
      pageTitle="Route Change Requests"
      pageSubtitle="Preview and manage route modification requests"
    >
      <div className="space-y-6">
        {/* Top Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <SummaryCard
            label="Total Requests"
            value="24"
            icon={MapPin}
            color="#4CBB17"
          />
          <SummaryCard
            label="Pending Review"
            value="8"
            icon={Clock}
            color="#f59e0b"
          />
          <SummaryCard
            label="Approved"
            value="12"
            icon={Check}
            color="#10b981"
          />
          <SummaryCard label="Declined" value="4" icon={X} color="#dc2626" />
        </div>

        {/* Table Section */}
        <div className="bg-white rounded-lg shadow-md overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h2 className="text-lg font-semibold text-gray-900">
              Route Change Overview
            </h2>
            <button
              className="px-4 py-2 text-white text-sm font-medium rounded-lg"
              style={{ backgroundColor: "#4CBB17" }}
            >
              + New Request
            </button>
          </div>

          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                {[
                  "ID",
                  "Route",
                  "Area",
                  "Type",
                  "Status",
                  "Priority",
                  "Requested By",
                  "Date",
                ].map((header) => (
                  <th
                    key={header}
                    className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider"
                  >
                    {header}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {sampleRequests.map((req) => {
                const status = getStatusStyle(req.status);
                return (
                  <tr key={req.id} className="hover:bg-gray-50">
                    <td className="py-4 px-6 text-sm text-gray-900 font-medium">
                      #{req.id}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-800">
                      {req.route}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      {req.area}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      {req.type}
                    </td>
                    <td className="py-4 px-6">
                      <span
                        className="px-3 py-1 rounded-md text-xs font-medium"
                        style={{
                          backgroundColor: status.bg,
                          color: status.color,
                        }}
                      >
                        {req.status}
                      </span>
                    </td>
                    <td className="py-4 px-6 text-sm font-medium">
                      <span
                        className="flex items-center space-x-2"
                        style={{ color: getPriorityColor(req.priority) }}
                      >
                        <AlertCircle className="w-4 h-4" />
                        <span>{req.priority}</span>
                      </span>
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      {req.requestedBy}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      {req.date}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
};

const SummaryCard = ({ label, value, icon: Icon, color }) => (
  <div className="bg-white rounded-lg shadow-md p-6">
    <div className="flex justify-between items-start">
      <div>
        <p className="text-sm text-gray-600">{label}</p>
        <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
      </div>
      <div
        className="w-10 h-10 rounded-lg flex items-center justify-center"
        style={{ backgroundColor: `${color}20` }}
      >
        <Icon className="w-5 h-5" style={{ color }} />
      </div>
    </div>
  </div>
);

export default RouteChangesPreview;
