import axios from "axios";

const api = axios.create({
  baseURL: "/api",
});

// Attach JWT token automatically to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Unwrap standard API response wrapper { status, message, data }
// Skip unwrapping for blob responses (file downloads) to avoid corrupting binary data
api.interceptors.response.use((response) => {
  if (response.config.responseType === "blob") {
    return response;
  }
  if (response.data && response.data.data !== undefined) {
    response.data = response.data.data;
  }
  return response;
});

export default api;