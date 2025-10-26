import '@testing-library/jest-dom';

// Ensure TextEncoder/TextDecoder available for jsdom + Mapbox
if (typeof global.TextEncoder === 'undefined') {
  const { TextEncoder, TextDecoder } = require('util');
  global.TextEncoder = TextEncoder;
  global.TextDecoder = TextDecoder;
}

//  Mock Mapbox (used in map or route components)
jest.mock('mapbox-gl', () => ({
  Map: jest.fn(() => ({
    on: jest.fn(),
    remove: jest.fn(),
  })),
}));

//  Silence React Router v7 deprecation warnings in test output
const originalWarn = console.warn;
console.warn = (...args) => {
  if (
    args[0]?.includes('React Router Future Flag Warning') ||
    args[0]?.includes('Relative route resolution within Splat routes')
  ) {
    return;
  }
  originalWarn(...args);
};

//  Mock react-router-dom navigation functions to prevent real redirects
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => jest.fn(),
  useLocation: () => ({
    pathname: '/admin/dashboard',
    search: '',
    hash: '',
    state: null,
    key: 'default'
  }),
}));
