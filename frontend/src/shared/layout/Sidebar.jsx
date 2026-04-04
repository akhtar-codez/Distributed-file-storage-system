import { Link, useLocation } from "react-router-dom";
import { useAuthStore } from "../../features/auth/authStore";

const navMain = [
  { name: "Dashboard", path: "/" },
  { name: "My Files", path: "/files" },
  { name: "Upload", path: "/upload" },
  { name: "Recent", path: "/recent" },
];

const navAccount = [
  { name: "Profile", path: "/profile" },
  { name: "Settings", path: "/settings" },
];

export default function Sidebar() {
  const { pathname } = useLocation();
  const logout = useAuthStore((state) => state.logout);

  return (
    <div className="w-48 bg-[#EDE6D6] border-r border-[#D6CDB8] flex flex-col flex-shrink-0">

      {/* Logo */}
      <div className="flex items-center gap-2 px-3 py-3 border-b border-[#D6CDB8]">
        <div className="w-8 h-8 rounded-lg bg-[#7BAF6A] flex items-center justify-center flex-shrink-0">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#fff" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
        </div>
        <div>
          <div className="text-sm font-semibold text-[#3A3328]">DFSS</div>
          <div className="text-[9px] text-[#9A9080]">Distributed Storage</div>
        </div>
      </div>

      {/* Main Nav */}
      <div className="px-2 pt-3 pb-1">
        <div className="text-[9px] uppercase tracking-widest text-[#B5A898] px-2 mb-1 font-medium">Main</div>
        {navMain.map(item => (
          <Link key={item.path} to={item.path}
            className={`flex items-center gap-2 px-2 py-1.5 rounded-lg text-xs mb-0.5 transition-all
              ${pathname === item.path
                ? "bg-[rgba(123,175,106,0.18)] text-[#4A7A3A] border border-[rgba(123,175,106,0.3)]"
                : "text-[#7A6E62] hover:bg-[rgba(123,175,106,0.1)] hover:text-[#4A7A3A]"
              }`}>
            <span className={`w-1.5 h-1.5 rounded-full flex-shrink-0 ${pathname === item.path ? "bg-[#7BAF6A]" : "bg-[#C5BA9E]"}`}></span>
            {item.name}
          </Link>
        ))}
      </div>

      {/* Account Nav */}
      <div className="px-2 pt-2 pb-1">
        <div className="text-[9px] uppercase tracking-widest text-[#B5A898] px-2 mb-1 font-medium">Account</div>
        {navAccount.map(item => (
          <Link key={item.path} to={item.path}
            className={`flex items-center gap-2 px-2 py-1.5 rounded-lg text-xs mb-0.5 transition-all
              ${pathname === item.path
                ? "bg-[rgba(123,175,106,0.18)] text-[#4A7A3A] border border-[rgba(123,175,106,0.3)]"
                : "text-[#7A6E62] hover:bg-[rgba(123,175,106,0.1)] hover:text-[#4A7A3A]"
              }`}>
            <span className="w-1.5 h-1.5 rounded-full flex-shrink-0 bg-[#C5BA9E]"></span>
            {item.name}
          </Link>
        ))}
      </div>

      {/* User Pill + Logout */}
      <div className="mt-auto p-2 border-t border-[#D6CDB8]">
        <div className="flex items-center gap-2 p-2 rounded-xl bg-white/40 border border-[#D6CDB8]">
          <div className="w-7 h-7 rounded-lg bg-[#7BAF6A] flex items-center justify-center text-white text-[10px] font-medium flex-shrink-0">
            A
          </div>
          <div className="flex-1 min-w-0">
            <div className="text-[11px] font-medium text-[#3A3328]">Akinator</div>
            <div className="text-[9px] text-[#9A9080] truncate">akhtarshah@gmail.com</div>
          </div>
          <button
            onClick={logout}
            title="Logout"
            className="w-6 h-6 rounded-md flex items-center justify-center text-[#9A9080] hover:text-[#C0562A] hover:bg-[rgba(232,137,106,0.1)] transition-all"
          >
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round">
              <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4"/>
              <polyline points="16 17 21 12 16 7"/>
              <line x1="21" y1="12" x2="9" y2="12"/>
            </svg>
          </button>
        </div>
      </div>

    </div>
  );
}