import { render, screen } from "@testing-library/react";
import App from "../App";

describe("App Component", () => {
  test("renders at least one EcoCollect title", () => {
    render(<App />);
    const titles = screen.getAllByText(/EcoCollect/i);
    expect(titles.length).toBeGreaterThan(0);
  });

  test("renders home page content", () => {
    render(<App />);
    expect(screen.getByText(/Smart Waste Management for a/i)).toBeInTheDocument();
    expect(screen.getByText(/Greener Tomorrow/i)).toBeInTheDocument();
    expect(screen.getAllByText(/Get Started/i).length).toBeGreaterThan(0);
  });

  test("renders sustainable future badge", () => {
    render(<App />);
    expect(screen.getByText(/Sustainable Future/i)).toBeInTheDocument();
  });
});
