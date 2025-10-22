import { useEffect, useState } from "react";
import { getFinancialAnalytics } from "../../services/analyticsService";
import { useAuth } from "../../context/AuthContext";
import DashboardLayout from "../../components/dashboard/AdminDashboardLayout";

import {
  BarChart3,
  Package,
  Truck,
  Trash2,
  CheckSquare,
  DollarSign,
  CreditCard,
  Receipt,
  TrendingUp,
} from "lucide-react";

const FinancialSummary = () => {
  const { user, logout } = useAuth();
  const [activeNav, setActiveNav] = useState("reports");
  const [data, setData] = useState(null);

  useEffect(() => {
    getFinancialAnalytics().then(setData).catch(console.error);
  }, []);

  if (!data)
    return (
      <div className="text-center p-10 text-gray-600">Loading analytics...</div>
    );

  const navItems = [
    { id: "reports", label: "Generate Reports", icon: BarChart3 },
    { id: "pickups", label: "Special Pickups", icon: Package },
    { id: "routes", label: "Route Changes", icon: Truck },
    { id: "bins", label: "Bin Requests", icon: Trash2 },
  ];

  const getStatusColor = (status) => {
    const colors = {
      paid: "#4CBB17",
      pending: "#f59e0b",
      overdue: "#ef4444",
    };
    return colors[status] || "#6b7280";
  };

  return (
    <DashboardLayout
      navItems={navItems}
      activeNav={activeNav}
      onNavClick={() => {}}
      logo="Admin"
      user={user}
      onLogout={logout}
      pageTitle="Financial Summary"
      pageSubtitle="View billing data, payment trends, and revenue analytics"
    >
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-6">
        <Stat
          label="Total Revenue"
          value={`$${data.totalRevenue}`}
          color="#4CBB17"
          Icon={DollarSign}
        />
        <Stat
          label="Pending Payments"
          value={`$${data.pendingPayments}`}
          color="#f59e0b"
          Icon={CreditCard}
        />
        <Stat
          label="Collection Costs"
          value={`$${data.collectionCosts}`}
          color="#ef4444"
          Icon={Receipt}
        />
        <Stat
          label="Net Profit"
          value={`$${data.netProfit}`}
          color="#3b82f6"
          Icon={TrendingUp}
        />
      </div>

      {/* Revenue by Area */}
      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">
          Revenue by Area
        </h2>
        {data.revenueByArea.map((area, idx) => (
          <div key={idx} className="mb-4">
            <div className="flex justify-between mb-2">
              <span className="font-medium text-gray-700">{area.area}</span>
              <span className="text-gray-900">
                ${area.revenue.toLocaleString()}
              </span>
            </div>
            <div className="h-2 bg-gray-200 rounded-full">
              <div
                className="h-2 rounded-full"
                style={{
                  width: `${area.percentage}%`,
                  backgroundColor: "#4CBB17",
                }}
              ></div>
            </div>
          </div>
        ))}
      </div>

      {/* Recent Transactions - Styled Table */}
      <div className="bg-white rounded-lg shadow-md">
        <div className="px-6 py-4 border-b border-gray-200">
          <h2 className="text-lg font-semibold text-gray-900">
            Recent Transactions
          </h2>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50">
              <tr>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  ID
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Customer
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Amount
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Type
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Date
                </th>
                <th className="py-3 px-6 text-left text-xs font-semibold text-gray-700 uppercase tracking-wider">
                  Status
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {data.recentTransactions.map((txn, idx) => (
                <tr key={idx} className="hover:bg-gray-50 transition-colors">
                  <td className="py-4 px-6 text-sm font-medium text-gray-900">
                    #{txn.id}
                  </td>
                  <td className="py-4 px-6 text-sm text-gray-900">
                    {txn.customer}
                  </td>
                  <td className="py-4 px-6 text-sm font-semibold text-gray-900">
                    ${txn.amount}
                  </td>
                  <td className="py-4 px-6 text-sm text-gray-700">
                    {txn.type}
                  </td>
                  <td className="py-4 px-6 text-sm text-gray-700">
                    {txn.date}
                  </td>
                  <td className="py-4 px-6">
                    <span
                      className="inline-flex items-center px-3 py-1 rounded-md text-xs font-medium capitalize"
                      style={{
                        backgroundColor: `${getStatusColor(txn.status)}20`,
                        color: getStatusColor(txn.status),
                      }}
                    >
                      {txn.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
};

const Stat = ({ label, value, color, Icon }) => (
  <div className="bg-white rounded-lg shadow-md p-6">
    <div className="flex justify-between items-center">
      <div>
        <p className="text-sm text-gray-600">{label}</p>
        <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
      </div>
      <div
        className="w-10 h-10 rounded-lg flex items-center justify-center"
        style={{ backgroundColor: `${color}15` }}
      >
        <Icon className="w-5 h-5" style={{ color }} />
      </div>
    </div>
  </div>
);

export default FinancialSummary;
