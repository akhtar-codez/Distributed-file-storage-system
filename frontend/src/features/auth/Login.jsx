import { useState } from "react";
import api from "../../shared/lib/axios";
import { useAuthStore } from "../auth/authStore";
import { Link } from "react-router-dom";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const login = useAuthStore((state) => state.login);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    setLoading(true);
    setError("");
    try {
        const res = await api.post("/auth/login", form);
        const token = res.data;

        // Decode JWT payload to extract userId
        const payload = JSON.parse(atob(token.split(".")[1]));
        const userId = payload.userId; // now a number, not email

        login(token, userId);
        window.location.href = "/";
    } catch {
        setError("Invalid email or password.");
    } finally {
        setLoading(false);
    }
    };

  return (
    <div className="min-h-screen bg-[#F5F0E8] flex items-center justify-center">
      <div className="bg-white border border-[#D6CDB8] rounded-2xl p-8 w-full max-w-sm shadow-sm">

        <div className="flex items-center gap-3 mb-6">
          <div className="w-9 h-9 rounded-xl bg-[#7BAF6A] flex items-center justify-center">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#fff" strokeWidth="2" strokeLinecap="round">
              <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/>
              <polyline points="17 8 12 3 7 8"/>
              <line x1="12" y1="3" x2="12" y2="15"/>
            </svg>
          </div>
          <div>
            <div className="text-sm font-semibold text-[#3A3328]">DFSS</div>
            <div className="text-[10px] text-[#9A9080]">Distributed Storage</div>
          </div>
        </div>

        <h2 className="text-lg font-semibold text-[#3A3328] mb-1">Welcome back</h2>
        <p className="text-xs text-[#9A9080] mb-6">Sign in to your account</p>

        <div className="mb-3">
          <label className="text-xs font-medium text-[#6A5E52] block mb-1">Email</label>
          <input
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="you@example.com"
            className="w-full border border-[#D6CDB8] rounded-lg px-3 py-2 text-sm text-[#3A3328] outline-none focus:border-[#7BAF6A] bg-[#FAFAF8] placeholder-[#C5BA9E]"
          />
        </div>

        <div className="mb-4">
          <label className="text-xs font-medium text-[#6A5E52] block mb-1">Password</label>
          <div className="relative">
            <input
              name="password"
              type={showPassword ? "text" : "password"}
              value={form.password}
              onChange={handleChange}
              placeholder="••••••••"
              className="w-full border border-[#D6CDB8] rounded-lg px-3 py-2 pr-9 text-sm text-[#3A3328] outline-none focus:border-[#7BAF6A] bg-[#FAFAF8] placeholder-[#C5BA9E]"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-2.5 top-1/2 -translate-y-1/2 text-[#9A9080] hover:text-[#3A3328] transition-all"
            >
              {showPassword ? (
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
                  <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/>
                  <path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              ) : (
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
              )}
            </button>
          </div>
        </div>

        {error && <p className="text-xs text-red-500 mb-3">{error}</p>}

        <button
          onClick={handleSubmit}
          disabled={loading}
          className="w-full bg-[#7BAF6A] hover:bg-[#6A9E5A] text-white text-sm font-medium py-2.5 rounded-lg transition-all disabled:opacity-60"
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>

        <p className="text-xs text-[#9A9080] text-center mt-4">
          Don't have an account?{" "}
          <Link to="/register" className="text-[#7BAF6A] font-medium hover:underline">Register</Link>
        </p>

      </div>
    </div>
  );
}