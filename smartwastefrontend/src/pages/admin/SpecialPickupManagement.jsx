import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../components/dashboard/AdminDashboardLayout";
import {
  BarChart3,
  Package,
  Truck,
  Trash2,
  LineChart,
  Check,
  X,
} from "lucide-react";
import {
  getAllSpecialPickups,
  updateSpecialPickupStatus,
} from "../../services/specialPickupService";

const SpecialPickupManagement = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [statusFilter, setStatusFilter] = useState("all");
  const [currentPage, setCurrentPage] = useState(1);
  const [activeNav, setActiveNav] = useState("pickups");
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadPickups();
  }, []);

  const loadPickups = async () => {
    setLoading(true);
    try {
      const data = await getAllSpecialPickups();
      setRequests(data);
    } catch (err) {
      console.error("Error fetching pickups:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (id) => {
    await updateSpecialPickupStatus(id, "approved");
    setRequests((prev) =>
      prev.map((r) => (r.id === id ? { ...r, status: "approved" } : r))
    );
  };

  const handleDecline = async (id) => {
    await updateSpecialPickupStatus(id, "declined");
    setRequests((prev) =>
      prev.map((r) => (r.id === id ? { ...r, status: "declined" } : r))
    );
  };

  const filtered =
    statusFilter === "all"
      ? requests
      : requests.filter((r) => r.status === statusFilter);

  const getStatusStyle = (status) => {
    const styles = {
      pending: { bg: "#fef3c7", color: "#d97706", text: "Pending" },
      approved: { bg: "#d1fae5", color: "#059669", text: "Approved" },
      declined: { bg: "#fee2e2", color: "#dc2626", text: "Declined" },
    };
    return styles[status] || styles.pending;
  };

  const handleLogout = () => logout();

  const handleNavClick = (navId) => {
    setActiveNav(navId);
    if (navId === "bins") navigate("/admin/bins");
    else if (navId === "pickups") navigate("/admin/pickups");
    else if (navId === "analytics") navigate("/admin/analytics");
    else if (navId === "reports") navigate("/admin/dashboard");
  };

  return (
    <DashboardLayout
      navItems={[
        { id: "reports", label: "Generate Reports", icon: BarChart3 },
        { id: "pickups", label: "Special Pickups", icon: Package },
        { id: "routes", label: "Route Changes", icon: Truck },
        { id: "bins", label: "Bin Requests", icon: Trash2 },
        { id: "analytics", label: "Analytics", icon: LineChart },
      ]}
      activeNav={activeNav}
      onNavClick={handleNavClick}
      logo="Admin"
      user={user}
      onLogout={handleLogout}
      pageTitle="Special Pickup Requests"
      pageSubtitle="Manage special waste collection requests from residents"
    >
      <div className="bg-white rounded-lg shadow-sm">
        <div className="px-6 py-4 border-b border-gray-200 flex justify-between">
          <div className="flex items-center space-x-3">
            <label className="text-sm font-medium text-gray-700">
              Filter by Status:
            </label>
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-3 py-1.5 border border-gray-300 rounded text-sm"
            >
              <option value="all">All</option>
              <option value="pending">Pending</option>
              <option value="approved">Approved</option>
              <option value="declined">Declined</option>
            </select>
          </div>
          <div className="text-sm text-gray-600">
            {filtered.length} requests found
          </div>
        </div>

        <div className="overflow-x-auto">
          {loading ? (
            <div className="text-center p-6 text-gray-600">Loading...</div>
          ) : (
            <table className="w-full">
              <thead className="bg-gray-50">
                <tr>
                  {[
                    "ID",
                    "Name",
                    "Area",
                    "Date",
                    "Type",
                    "Status",
                    "Actions",
                  ].map((h) => (
                    <th
                      key={h}
                      className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider"
                    >
                      {h}
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filtered.map((req) => {
                  const s = getStatusStyle(req.status);
                  return (
                    <tr key={req.id} className="hover:bg-gray-50 transition">
                      <td className="py-4 px-6 text-sm text-gray-900">
                        #{req.id}
                      </td>
                      <td className="py-4 px-6 text-sm">{req.name}</td>
                      <td className="py-4 px-6 text-sm">{req.area}</td>
                      <td className="py-4 px-6 text-sm">{req.date}</td>
                      <td className="py-4 px-6 text-sm">{req.type}</td>
                      <td className="py-4 px-6">
                        <span
                          className="inline-flex px-3 py-1 rounded-md text-xs font-medium"
                          style={{
                            backgroundColor: s.bg,
                            color: s.color,
                          }}
                        >
                          {s.text}
                        </span>
                      </td>
                      <td className="py-4 px-6 flex space-x-3">
                        {req.status === "pending" ? (
                          <>
                            <button
                              onClick={() => handleApprove(req.id)}
                              className="text-green-600 hover:text-green-700"
                            >
                              <Check className="w-5 h-5" />
                            </button>
                            <button
                              onClick={() => handleDecline(req.id)}
                              className="text-red-600 hover:text-red-700"
                            >
                              <X className="w-5 h-5" />
                            </button>
                          </>
                        ) : (
                          <span className="text-gray-400 text-xs italic">
                            No actions
                          </span>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
};

export default SpecialPickupManagement;
