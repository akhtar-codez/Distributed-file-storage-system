import { useState } from "react";
import { useAuthStore } from "./authStore";

export default function ProfilePage() {
  const { userId } = useAuthStore();
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    navigator.clipboard.writeText(userId || "");
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="max-w-lg mx-auto mt-8">
      <h2 className="text-base font-semibold text-[#3A3328] mb-1">Profile</h2>
      <p className="text-xs text-[#9A9080] mb-5">Your account information</p>

      {/* Avatar Card */}
      <div className="bg-white border border-[#D6CDB8] rounded-2xl p-6 mb-4 flex items-center gap-5">
        <div className="w-14 h-14 rounded-2xl bg-[#7BAF6A] flex items-center justify-center text-white text-xl font-semibold flex-shrink-0">
          A
        </div>
        <div>
          <div className="text-base font-semibold text-[#3A3328]">Akinator</div>
          <div className="text-xs text-[#9A9080]">akhtarshah@gmail.com</div>
          <div className="mt-1.5 inline-block text-[10px] bg-[rgba(123,175,106,0.12)] text-[#4A7A3A] border border-[rgba(123,175,106,0.3)] px-2 py-0.5 rounded-full">
            Active
          </div>
        </div>
      </div>

      {/* Info Card */}
      <div className="bg-white border border-[#D6CDB8] rounded-2xl overflow-hidden mb-4">
        <div className="px-5 py-3 border-b border-[#EDE6D6]">
          <span className="text-xs font-medium text-[#6A5E52]">Account Details</span>
        </div>

        <div className="divide-y divide-[#F5F0E8]">
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">Username</span>
            <span className="text-xs font-medium text-[#3A3328]">Akinator</span>
          </div>
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">Email</span>
            <span className="text-xs font-medium text-[#3A3328]">akhtarshah@gmail.com</span>
          </div>
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">User ID</span>
            <div className="flex items-center gap-2">
              <span className="text-xs font-mono text-[#9A9080]">{userId || "—"}</span>
              <button
                onClick={handleCopy}
                className="text-[10px] text-[#7BAF6A] border border-[rgba(123,175,106,0.3)] px-2 py-0.5 rounded-md hover:bg-[rgba(123,175,106,0.08)] transition-all"
              >
                {copied ? "Copied!" : "Copy"}
              </button>
            </div>
          </div>
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">Plan</span>
            <span className="text-xs font-medium text-[#3A3328]">Free</span>
          </div>
        </div>
      </div>

      {/* Storage Summary */}
      <div className="bg-white border border-[#D6CDB8] rounded-2xl overflow-hidden">
        <div className="px-5 py-3 border-b border-[#EDE6D6]">
          <span className="text-xs font-medium text-[#6A5E52]">Storage</span>
        </div>
        <div className="px-5 py-4">
          <div className="flex justify-between text-xs mb-2">
            <span className="text-[#9A9080]">Used</span>
            <span className="text-[#3A3328] font-medium">1.2 GB / 5 GB</span>
          </div>
          <div className="h-1.5 bg-[#EDE6D6] rounded-full overflow-hidden">
            <div className="h-full bg-[#7BAF6A] rounded-full" style={{ width: "24%" }} />
          </div>
          <div className="text-[10px] text-[#9A9080] mt-1.5">24% of free plan used</div>
        </div>
      </div>

    </div>
  );
}