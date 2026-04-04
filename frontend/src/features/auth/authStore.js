import { create } from "zustand";

export const useAuthStore = create((set) => ({
  token: localStorage.getItem("token") || null,
  userId: localStorage.getItem("userId") || null,
  isAuthenticated: !!localStorage.getItem("token"),

  login: (token, userId) => {
    localStorage.setItem("token", token);
    localStorage.setItem("userId", userId);
    set({ token, userId, isAuthenticated: true });
  },

  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    set({ token: null, userId: null, isAuthenticated: false });
  },
}));