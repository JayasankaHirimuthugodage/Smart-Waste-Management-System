import { render, screen } from "@testing-library/react";
import AuthWrapper from "../__mocks__/AuthWrapper";
import AnalyticsView from "../pages/admin/components/AnalyticsView";

describe("AnalyticsView", () => {
  it("renders analytics dashboard cards", () => {
    render(
      <AuthWrapper>
        <AnalyticsView />
      </AuthWrapper>
    );

    // Check main heading
    expect(screen.getByText(/Let's Analyse/i)).toBeInTheDocument();
    expect(screen.getByText(/Select a dashboard to view detailed analytics and insights/i)).toBeInTheDocument();

    // Check all dashboard cards are present
    expect(screen.getByText(/Waste Collection Performance/i)).toBeInTheDocument();
    expect(screen.getByText(/Monitor collection efficiency, worker performance, and route optimization/i)).toBeInTheDocument();

    expect(screen.getByText(/Recycling Trends/i)).toBeInTheDocument();
    expect(screen.getByText(/Track recycling rates, waste composition, and sustainability metrics/i)).toBeInTheDocument();

    expect(screen.getByText(/Financial Summary/i)).toBeInTheDocument();
    expect(screen.getByText(/View billing data, payment trends, and revenue analytics/i)).toBeInTheDocument();

    expect(screen.getByText(/Environmental Impact/i)).toBeInTheDocument();
    expect(screen.getByText(/Analyze carbon footprint, waste reduction, and environmental KPIs/i)).toBeInTheDocument();
  });

  it("renders all dashboard cards as clickable elements", () => {
    render(
      <AuthWrapper>
        <AnalyticsView />
      </AuthWrapper>
    );

    // Check that all cards have the cursor-pointer class (indicating they're clickable)
    const cards = screen.getAllByRole("generic").filter(el => 
      el.className.includes("cursor-pointer")
    );
    expect(cards).toHaveLength(4);
  });
});
