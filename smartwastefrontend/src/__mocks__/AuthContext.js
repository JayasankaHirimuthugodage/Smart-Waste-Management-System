import React from "react";

//  Define a default fake user (prevents null/undefined errors in layouts)
const fakeUser = {
  id: "test-user",
  name: "Test Admin",
  role: "ADMIN",
  email: "testadmin@example.com",
  avatar: "https://via.placeholder.com/100", // added to fix user.avatar issue
};

//  Create a mock context with default methods
export const AuthContext = React.createContext({
  user: fakeUser,
  login: jest.fn(),
  logout: jest.fn(),
  isAuthenticated: jest.fn(() => true),
  hasRole: jest.fn(() => true),
});

//  Provide the mock context for all child components
export const AuthProvider = ({ children }) => (
  <AuthContext.Provider
    value={{
      user: fakeUser,
      login: jest.fn(),
      logout: jest.fn(),
      isAuthenticated: jest.fn(() => true),
      hasRole: jest.fn(() => true),
    }}
  >
    {children}
  </AuthContext.Provider>
);

//  Custom hook
export const useAuth = () => React.useContext(AuthContext);
