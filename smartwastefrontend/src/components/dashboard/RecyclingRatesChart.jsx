import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const RecyclingRatesChart = () => {
  const data = [
    { area: "Downtown", rate: 68 },
    { area: "North", rate: 72 },
    { area: "South", rate: 65 },
    { area: "Industrial", rate: 58 },
  ];

  return (
    <div className="bg-white rounded-lg shadow-md p-6">
      <h2 className="text-lg font-semibold text-gray-900 mb-4">
        Recycling Rates by Area
      </h2>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
          <XAxis
            dataKey="area"
            tick={{ fill: "#6b7280", fontSize: 12 }}
            axisLine={{ stroke: "#e5e7eb" }}
          />
          <YAxis
            tick={{ fill: "#6b7280", fontSize: 12 }}
            axisLine={{ stroke: "#e5e7eb" }}
            domain={[0, 100]}
            label={{
              value: "Rate (%)",
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
            formatter={(value) => [`${value}%`, "Recycling Rate"]}
          />
          <Legend wrapperStyle={{ paddingTop: "20px" }} />
          <Bar
            dataKey="rate"
            fill="#3b82f6"
            radius={[8, 8, 0, 0]}
            name="Recycling Rate"
          />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default RecyclingRatesChart;
