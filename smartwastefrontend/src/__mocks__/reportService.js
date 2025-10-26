export const generateReport = jest.fn(() =>
  Promise.resolve({
    reportType: "Waste Summary",
    area: "Colombo",
    totalWaste: 1200,
    recyclingRate: 78,
  })
);
