import { render, screen } from "@testing-library/react";
import AuthWrapper from "../__mocks__/AuthWrapper";
import StatsGrid from "../pages/admin/components/StatsGrid";

// Mock statistics data with proper structure
const mockStatistics = [
  {
    id: 1,
    label: "Total Collections",
    value: "1,234",
    change: "+12%",
    icon: () => <span>📊</span>,
    iconColor: "#4CBB17"
  },
  {
    id: 2,
    label: "Active Routes",
    value: "45",
    change: "+5%",
    icon: () => <span>🗺️</span>,
    iconColor: "#3b82f6"
  },
  {
    id: 3,
    label: "Pending Requests",
    value: "23",
    change: "-8%",
    icon: () => <span>⏳</span>,
    iconColor: "#f59e0b"
  },
  {
    id: 4,
    label: "Completion Rate",
    value: "94%",
    change: "+2%",
    icon: () => <span>✅</span>,
    iconColor: "#10b981"
  }
];

describe("SpecialPickupPage (Stats Grid)", () => {
  it("renders stats grid component", () => {
    render(
      <AuthWrapper>
        <StatsGrid statistics={mockStatistics} />
      </AuthWrapper>
    );

    // Check if the stats grid renders without errors
    expect(screen.getByText(/Total Collections/i)).toBeInTheDocument();
    expect(screen.getByText(/Active Routes/i)).toBeInTheDocument();
    expect(screen.getByText(/Pending Requests/i)).toBeInTheDocument();
    expect(screen.getByText(/Completion Rate/i)).toBeInTheDocument();
  });

  it("displays numerical values", () => {
    render(
      <AuthWrapper>
        <StatsGrid statistics={mockStatistics} />
      </AuthWrapper>
    );

    // Check for numerical values in the stats
    expect(screen.getByText("1,234")).toBeInTheDocument();
    expect(screen.getByText("45")).toBeInTheDocument();
    expect(screen.getByText("23")).toBeInTheDocument();
    expect(screen.getByText("94%")).toBeInTheDocument();
  });
});
