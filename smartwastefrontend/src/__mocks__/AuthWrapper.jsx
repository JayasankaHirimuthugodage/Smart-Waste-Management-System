import React from "react";
import { MemoryRouter } from "react-router-dom";
import { AuthProvider } from "../context/AuthContext";

// ✅ Inject fake user immediately (before render)
const AuthWrapper = ({ children }) => {
  const fakeUser = {
    id: "test-user",
    name: "Test Admin",
    role: "admin",
    avatar: "https://via.placeholder.com/150",
  };

  // Patch localStorage and context defaults
  localStorage.setItem("user", JSON.stringify(fakeUser));

  return (
    <MemoryRouter>
      <AuthProvider initialUser={fakeUser}>{children}</AuthProvider>
    </MemoryRouter>
  );
};

export default AuthWrapper;
