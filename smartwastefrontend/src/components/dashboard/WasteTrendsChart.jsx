import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const WasteTrendsChart = () => {
  const data = [
    { month: "Jan", general: 800, recyclable: 1200 },
    { month: "Feb", general: 950, recyclable: 1350 },
    { month: "Mar", general: 750, recyclable: 1100 },
    { month: "Apr", general: 1100, recyclable: 1400 },
    { month: "May", general: 900, recyclable: 1250 },
    { month: "Jun", general: 1200, recyclable: 1500 },
  ];

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">Waste Trends</h2>
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
          <XAxis
            dataKey="month"
            tick={{ fill: "#6b7280", fontSize: 12 }}
            axisLine={{ stroke: "#e5e7eb" }}
          />
          <YAxis
            tick={{ fill: "#6b7280", fontSize: 12 }}
            axisLine={{ stroke: "#e5e7eb" }}
            label={{
              value: "Waste (kg)",
              angle: -90,
              position: "insideLeft",
              style: { fill: "#6b7280", fontSize: 12 },
            }}
          />
          <Tooltip
            contentStyle={{
              backgroundColor: "#fff",
              border: "1px solid #e5e7eb",
              borderRadius: "8px",
              boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
            }}
          />
          <Legend wrapperStyle={{ paddingTop: "20px" }} iconType="line" />
          <Line
            type="monotone"
            dataKey="general"
            stroke="#4CBB17"
            strokeWidth={2}
            dot={{ fill: "#4CBB17", r: 4 }}
            activeDot={{ r: 6 }}
            name="General Waste"
          />
          <Line
            type="monotone"
            dataKey="recyclable"
            stroke="#3b82f6"
            strokeWidth={2}
            dot={{ fill: "#3b82f6", r: 4 }}
            activeDot={{ r: 6 }}
            name="Recyclable Waste"
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default WasteTrendsChart;
