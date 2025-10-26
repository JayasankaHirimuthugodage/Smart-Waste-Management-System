export const getBins = jest.fn(() =>
  Promise.resolve([
    { id: 1, location: "Colombo", capacity: 80 },
    { id: 2, location: "Galle", capacity: 65 },
  ])
);
