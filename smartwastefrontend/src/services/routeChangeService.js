const API_BASE_URL = "http://localhost:8080/api/admin/routes";

/**
 * Get all route change requests
 */
export const getAllRoutes = async () => {
  try {
    const res = await fetch(API_BASE_URL);
    if (!res.ok) throw new Error("Failed to fetch routes");
    return await res.json();
  } catch (err) {
    console.error("Error fetching routes:", err);
    return [];
  }
};

/**
 * Create a new route change request
 */
export const createRouteRequest = async (data) => {
  try {
    const res = await fetch(API_BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });
    if (!res.ok) throw new Error("Failed to create route request");
    return await res.json();
  } catch (err) {
    console.error("Error creating route request:", err);
    return null;
  }
};

/**
 * Update route request status (approve/decline)
 */
export const updateRouteStatus = async (id, status) => {
  try {
    const res = await fetch(`${API_BASE_URL}/${id}/status?status=${status}`, {
      method: "PUT",
    });
    if (!res.ok) throw new Error("Failed to update route status");
    return await res.json();
  } catch (err) {
    console.error("Error updating route status:", err);
    return null;
  }
};

/**
 *  Get high-waste area route suggestions (used by your frontend)
 */
export const getRouteSuggestions = async () => {
  try {
    const res = await fetch(`${API_BASE_URL}/high-waste`);
    if (!res.ok) throw new Error("Failed to fetch route suggestions");
    return await res.json();
  } catch (err) {
    console.error("Error fetching route suggestions:", err);
    return [];
  }
};
