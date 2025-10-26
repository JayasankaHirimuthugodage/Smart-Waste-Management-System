export const getAllRoutes = jest.fn(() =>
  Promise.resolve([
    { id: 1, routeName: "Colombo Central" },
    { id: 2, routeName: "Kandy Express" },
  ])
);
