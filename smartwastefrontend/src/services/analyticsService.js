// src/services/analyticsService.js
import axios from "axios";

const API_BASE = "http://localhost:8080/api/admin/analytics";

export const getPerformanceAnalytics = async () => {
  const res = await axios.get(`${API_BASE}/performance`);
  return res.data;
};

export const getRecyclingAnalytics = async () => {
  const res = await axios.get(`${API_BASE}/recycling`);
  return res.data;
};

export const getFinancialAnalytics = async () => {
  const res = await axios.get(`${API_BASE}/financial`);
  return res.data;
};

export const getEnvironmentalAnalytics = async () => {
  const res = await axios.get(`${API_BASE}/environmental`);
  return res.data;
};
