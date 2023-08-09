import axios from "axios";

const BASE_URL = "http://localhost:8085/api";

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: false,
});

// Add an interceptor to include the JWT token in the request headers
axiosInstance.interceptors.request.use((config) => {
  const authDataString = localStorage.getItem("authData");
  if (authDataString) {
    const authData = JSON.parse(authDataString);
    const token = authData.accessToken; // Use the correct key "accessToken" to get the token
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// Log the request details before making the API call
// axiosInstance.interceptors.request.use((config) => {
//   console.log("Request Method:", config.method);
//   console.log("Request URL:", config.url);
//   console.log("Request Headers:", config.headers);
//   console.log("Request Body:", config.data);
//   return config;
// });

export default axiosInstance;
