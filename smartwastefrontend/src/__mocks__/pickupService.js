export const getPickups = jest.fn(() =>
  Promise.resolve([
    { id: 1, name: "John Doe", address: "Colombo 03" },
    { id: 2, name: "Jane Smith", address: "Kandy" },
  ])
);
