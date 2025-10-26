import { render, screen, waitFor } from "@testing-library/react";
import AuthWrapper from "../__mocks__/AuthWrapper";
import BinRequestsManagement from "../pages/admin/BinRequestsManagement";
import axios from "axios";

// Mock axios
jest.mock("axios");
const mockedAxios = axios;

describe("BinRequestsManagement", () => {
  beforeEach(() => {
    mockedAxios.get.mockClear();
  });

  it("renders bin management interface", async () => {
    // Mock successful API response
    mockedAxios.get.mockResolvedValueOnce({
      data: [
        { id: 1, binId: "BIN001", ownerId: "user123", address: "Colombo 03", status: "Pending" },
        { id: 2, binId: "BIN002", ownerId: "user456", address: "Kandy", status: "Approved" }
      ]
    });

    render(
      <AuthWrapper>
        <BinRequestsManagement />
      </AuthWrapper>
    );

    // Check if the component renders the main interface
    expect(screen.getByText(/Bin Requests Management/i)).toBeInTheDocument();
    expect(screen.getByText(/Manage bin approvals, rejections, and history/i)).toBeInTheDocument();
    
    // Check if filter dropdown is present
    expect(screen.getByText(/Filter by Status:/i)).toBeInTheDocument();
    
    // Wait for API call and check if data is loaded
    await waitFor(() => {
      expect(mockedAxios.get).toHaveBeenCalledWith("http://localhost:8080/api/admin/bins/all");
    });
  });

  it("displays loading state initially", () => {
    // Mock a delayed response
    mockedAxios.get.mockImplementation(() => new Promise(() => {}));

    render(
      <AuthWrapper>
        <BinRequestsManagement />
      </AuthWrapper>
    );

    expect(screen.getByText(/Loading.../i)).toBeInTheDocument();
  });
});
