import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../components/dashboard/AdminDashboardLayout";
import {
  getAllRoutes,
  updateRouteStatus,
  getRouteSuggestions,
} from "../../services/routeChangeService";
import {
  Check,
  X,
  Eye,
  BarChart3,
  Package,
  Truck,
  Trash2,
  LineChart,
  Clock,
  CheckCircle,
  XCircle,
  MapPin,
} from "lucide-react";

const RouteChangesPreview = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  const [activeNav, setActiveNav] = useState("routes");
  const [routes, setRoutes] = useState([]);
  const [selected, setSelected] = useState(null);
  const [suggestions, setSuggestions] = useState([]);

  // Sync sidebar highlight with URL path
  useEffect(() => {
    if (location.pathname.includes("/dashboard")) setActiveNav("reports");
    else if (location.pathname.includes("/pickups")) setActiveNav("pickups");
    else if (location.pathname.includes("/bins")) setActiveNav("bins");
    else if (location.pathname.includes("/analytics"))
      setActiveNav("analytics");
    else if (location.pathname.includes("/routes")) setActiveNav("routes");
  }, [location.pathname]);

  // Fetch all data
  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [all, sugg] = await Promise.all([
        getAllRoutes(),
        getRouteSuggestions(),
      ]);
      setRoutes(all);
      setSuggestions(sugg);
    } catch (error) {
      console.error("Failed to load routes:", error);
    }
  };

  const handleStatus = async (id, status) => {
    try {
      await updateRouteStatus(id, status);
      loadData();
    } catch (error) {
      console.error("Failed to update route status:", error);
    }
  };

  const navItems = [
    { id: "reports", label: "Generate Reports", icon: BarChart3 },
    { id: "pickups", label: "Special Pickups", icon: Package },
    { id: "routes", label: "Route Changes", icon: Truck },
    { id: "bins", label: "Bin Requests", icon: Trash2 },
    { id: "analytics", label: "Analytics", icon: LineChart },
  ];

  // Page navigation logic
  const handleNavClick = (navId) => {
    setActiveNav(navId);
    const paths = {
      reports: "/admin/dashboard",
      pickups: "/admin/pickups",
      routes: "/admin/routes",
      bins: "/admin/bins",
      analytics: "/admin/analytics",
    };
    if (paths[navId]) navigate(paths[navId]);
  };

  // Calculate statistics
  const stats = {
    total: routes.length,
    pending: routes.filter((r) => r.status === "PENDING").length,
    approved: routes.filter((r) => r.status === "APPROVED").length,
    declined: routes.filter((r) => r.status === "DECLINED").length,
  };

  // Get status style
  const getStatusStyle = (status) => {
    const styles = {
      PENDING: { bg: "#fef3c7", color: "#d97706", text: "Pending" },
      APPROVED: { bg: "#d1fae5", color: "#059669", text: "Approved" },
      DECLINED: { bg: "#fee2e2", color: "#dc2626", text: "Declined" },
    };
    return styles[status] || styles.PENDING;
  };

  return (
    <DashboardLayout
      navItems={navItems}
      activeNav={activeNav}
      onNavClick={handleNavClick}
      logo="Admin"
      user={user}
      onLogout={logout}
      pageTitle="Route Optimization Requests"
      pageSubtitle="Approve or decline data-driven route optimization proposals"
    >
      {/* Statistics Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm text-gray-600">Total Routes</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">
                {stats.total}
              </p>
            </div>
            <div
              className="w-10 h-10 rounded-lg flex items-center justify-center"
              style={{ backgroundColor: "#4CBB1715" }}
            >
              <MapPin className="w-5 h-5" style={{ color: "#4CBB17" }} />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm text-gray-600">Pending Review</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">
                {stats.pending}
              </p>
            </div>
            <div
              className="w-10 h-10 rounded-lg flex items-center justify-center"
              style={{ backgroundColor: "#f59e0b15" }}
            >
              <Clock className="w-5 h-5" style={{ color: "#f59e0b" }} />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm text-gray-600">Approved</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">
                {stats.approved}
              </p>
            </div>
            <div
              className="w-10 h-10 rounded-lg flex items-center justify-center"
              style={{ backgroundColor: "#10b98115" }}
            >
              <CheckCircle className="w-5 h-5" style={{ color: "#10b981" }} />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex justify-between items-start">
            <div>
              <p className="text-sm text-gray-600">Declined</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">
                {stats.declined}
              </p>
            </div>
            <div
              className="w-10 h-10 rounded-lg flex items-center justify-center"
              style={{ backgroundColor: "#ef444415" }}
            >
              <XCircle className="w-5 h-5" style={{ color: "#ef4444" }} />
            </div>
          </div>
        </div>
      </div>

      {/* High Waste Area Suggestions */}
      {suggestions.length > 0 && (
        <div className="bg-yellow-50 border-l-4 border-yellow-500 p-4 mb-6 rounded-r-lg shadow-sm">
          <h3 className="font-semibold text-yellow-700 mb-2 flex items-center">
            <svg
              className="w-5 h-5 mr-2"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                fillRule="evenodd"
                d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                clipRule="evenodd"
              />
            </svg>
            High Waste Area Suggestions
          </h3>
          <div className="space-y-2">
            {suggestions.map((s, i) => (
              <p key={i} className="text-sm text-gray-700">
                ⚠️ <span className="font-semibold">{s.area}</span> producing{" "}
                <span className="font-semibold">
                  {s.wasteVolumePerDay}kg/day
                </span>{" "}
                — consider route update.
              </p>
            ))}
          </div>
        </div>
      )}

      {/* Route Table */}
      <div className="bg-white rounded-lg shadow-sm">
        <div className="px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">
            Route Optimization Requests
          </h2>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Route
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Area
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Current Stops
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Proposed Stops
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Distance (km)
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Reason
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Status
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {routes.map((r) => {
                const statusStyle = getStatusStyle(r.status);
                return (
                  <tr key={r.id} className="hover:bg-gray-50 transition-colors">
                    <td className="py-4 px-6 text-sm font-medium text-gray-900">
                      {r.routeName}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      {r.area}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-600">
                      {r.currentStops?.join(" → ")}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-600">
                      {r.proposedStops?.join(" → ")}
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-700">
                      <div className="flex items-center space-x-2">
                        <span className="text-gray-500">
                          {r.currentDistanceKm}
                        </span>
                        <span>→</span>
                        <span className="font-semibold text-green-600">
                          {r.proposedDistanceKm}
                        </span>
                      </div>
                    </td>
                    <td className="py-4 px-6 text-sm text-gray-600">
                      {r.reason}
                    </td>
                    <td className="py-4 px-6">
                      <span
                        className="inline-flex items-center px-3 py-1 rounded-md text-xs font-medium"
                        style={{
                          backgroundColor: statusStyle.bg,
                          color: statusStyle.color,
                        }}
                      >
                        {statusStyle.text}
                      </span>
                    </td>
                    <td className="py-4 px-6">
                      <div className="flex items-center space-x-2">
                        <button
                          onClick={() => setSelected(r)}
                          className="p-1.5 rounded hover:bg-blue-50 text-blue-600 transition-colors"
                          title="View Details"
                        >
                          <Eye className="w-4 h-4" />
                        </button>
                        {r.status === "PENDING" && (
                          <>
                            <button
                              onClick={() => handleStatus(r.id, "APPROVED")}
                              className="p-1.5 rounded hover:bg-green-50 text-green-600 transition-colors"
                              title="Approve"
                            >
                              <Check className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => handleStatus(r.id, "DECLINED")}
                              className="p-1.5 rounded hover:bg-red-50 text-red-600 transition-colors"
                              title="Decline"
                            >
                              <X className="w-4 h-4" />
                            </button>
                          </>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>

        {routes.length === 0 && (
          <div className="px-6 py-12 text-center">
            <Truck className="w-12 h-12 mx-auto text-gray-400 mb-4" />
            <p className="text-gray-500">
              No route optimization requests found
            </p>
          </div>
        )}
      </div>

      {/* Route Preview Modal */}
      {selected && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 p-4">
          <div className="bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto">
            <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between sticky top-0 bg-white z-10">
              <div>
                <h2 className="text-xl font-semibold text-gray-900">
                  {selected.routeName}
                </h2>
                <p className="text-sm text-gray-600 mt-1">
                  Route Optimization Details
                </p>
              </div>
              <button
                onClick={() => setSelected(null)}
                className="text-gray-400 hover:text-gray-600 transition-colors"
              >
                <X className="w-6 h-6" />
              </button>
            </div>

            <div className="px-6 py-6 space-y-6">
              <div className="bg-gray-50 rounded-lg p-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-gray-600">Area</p>
                    <p className="text-base font-medium text-gray-900">
                      {selected.area}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-600">Optimization Type</p>
                    <p className="text-base font-medium text-gray-900">
                      {selected.optimizationType}
                    </p>
                  </div>
                </div>
              </div>

              <div>
                <h3 className="text-sm font-semibold text-gray-700 mb-2">
                  Reason for Change
                </h3>
                <div className="bg-white border border-gray-200 rounded-lg p-4">
                  <p className="text-sm text-gray-700 leading-relaxed">
                    {selected.reason}
                  </p>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <h3 className="font-medium text-gray-700 mb-3">
                    Current Route
                  </h3>
                  <div className="border border-gray-200 rounded-lg p-4 bg-gray-50">
                    <p className="text-xs text-gray-600 mb-2">
                      Distance: {selected.currentDistanceKm} km
                    </p>
                    <ul className="space-y-2">
                      {selected.currentStops?.map((s, i) => (
                        <li
                          key={i}
                          className="text-sm text-gray-700 flex items-start"
                        >
                          <span className="inline-block w-6 h-6 rounded-full bg-gray-300 text-gray-700 text-xs flex items-center justify-center mr-2 flex-shrink-0">
                            {i + 1}
                          </span>
                          {s}
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div>
                  <h3 className="font-medium text-gray-700 mb-3">
                    Proposed Route
                  </h3>
                  <div className="border-2 border-green-500 rounded-lg p-4 bg-green-50">
                    <p className="text-xs text-green-700 mb-2 font-semibold">
                      Distance: {selected.proposedDistanceKm} km
                    </p>
                    <ul className="space-y-2">
                      {selected.proposedStops?.map((s, i) => (
                        <li
                          key={i}
                          className="text-sm text-gray-700 flex items-start"
                        >
                          <span className="inline-block w-6 h-6 rounded-full bg-green-600 text-white text-xs flex items-center justify-center mr-2 flex-shrink-0">
                            {i + 1}
                          </span>
                          {s}
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>
              </div>

              {selected.status === "PENDING" && (
                <div className="flex items-center justify-end space-x-3 pt-4 border-t">
                  <button
                    onClick={() => {
                      handleStatus(selected.id, "DECLINED");
                      setSelected(null);
                    }}
                    className="px-6 py-2 text-sm font-medium text-white bg-red-600 rounded hover:bg-red-700 transition-colors flex items-center space-x-2"
                  >
                    <X className="w-4 h-4" />
                    <span>Decline</span>
                  </button>
                  <button
                    onClick={() => {
                      handleStatus(selected.id, "APPROVED");
                      setSelected(null);
                    }}
                    className="px-6 py-2 text-sm font-medium text-white bg-green-600 rounded hover:bg-green-700 transition-colors flex items-center space-x-2"
                  >
                    <Check className="w-4 h-4" />
                    <span>Approve</span>
                  </button>
                </div>
              )}

              {selected.status !== "PENDING" && (
                <div className="flex items-center justify-end pt-4 border-t">
                  <button
                    onClick={() => setSelected(null)}
                    className="px-6 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded hover:bg-gray-50 transition-colors"
                  >
                    Close
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
};

export default RouteChangesPreview;
