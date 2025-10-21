import axios from "axios";
const API_BASE = "http://localhost:8080/api/admin/pickups";

export const getAllSpecialPickups = async () => {
  const res = await axios.get(`${API_BASE}/all`);
  return res.data;
};

export const updateSpecialPickupStatus = async (id, status) => {
  const res = await axios.put(`${API_BASE}/${id}/status`, null, {
    params: { status },
  });
  return res.data;
};
