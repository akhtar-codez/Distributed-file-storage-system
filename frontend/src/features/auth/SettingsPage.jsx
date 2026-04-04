import { useState } from "react";
import { useAuthStore } from "./authStore";

export default function SettingsPage() {
  const logout = useAuthStore((state) => state.logout);
  const [notifications, setNotifications] = useState(true);
  const [autoDelete, setAutoDelete] = useState(false);
  const [saved, setSaved] = useState(false);

  const handleSave = () => {
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  return (
    <div className="max-w-lg mx-auto mt-8">
      <h2 className="text-base font-semibold text-[#3A3328] mb-1">Settings</h2>
      <p className="text-xs text-[#9A9080] mb-5">Manage your preferences</p>

      {/* Preferences */}
      <div className="bg-white border border-[#D6CDB8] rounded-2xl overflow-hidden mb-4">
        <div className="px-5 py-3 border-b border-[#EDE6D6]">
          <span className="text-xs font-medium text-[#6A5E52]">Preferences</span>
        </div>

        <div className="divide-y divide-[#F5F0E8]">

          {/* Toggle: Notifications */}
          <div className="flex justify-between items-center px-5 py-4">
            <div>
              <div className="text-xs font-medium text-[#3A3328]">Upload Notifications</div>
              <div className="text-[10px] text-[#9A9080] mt-0.5">Get notified when upload completes</div>
            </div>
            <button
              onClick={() => setNotifications(!notifications)}
              className={`w-10 h-5 rounded-full transition-all relative flex-shrink-0 ${notifications ? "bg-[#7BAF6A]" : "bg-[#D6CDB8]"}`}
            >
              <span className={`absolute top-0.5 w-4 h-4 bg-white rounded-full shadow transition-all ${notifications ? "left-5" : "left-0.5"}`} />
            </button>
          </div>

          {/* Toggle: Auto Delete */}
          <div className="flex justify-between items-center px-5 py-4">
            <div>
              <div className="text-xs font-medium text-[#3A3328]">Auto Delete Old Files</div>
              <div className="text-[10px] text-[#9A9080] mt-0.5">Automatically remove files older than 90 days</div>
            </div>
            <button
              onClick={() => setAutoDelete(!autoDelete)}
              className={`w-10 h-5 rounded-full transition-all relative flex-shrink-0 ${autoDelete ? "bg-[#7BAF6A]" : "bg-[#D6CDB8]"}`}
            >
              <span className={`absolute top-0.5 w-4 h-4 bg-white rounded-full shadow transition-all ${autoDelete ? "left-5" : "left-0.5"}`} />
            </button>
          </div>

        </div>
      </div>

      {/* Storage */}
      <div className="bg-white border border-[#D6CDB8] rounded-2xl overflow-hidden mb-4">
        <div className="px-5 py-3 border-b border-[#EDE6D6]">
          <span className="text-xs font-medium text-[#6A5E52]">Storage Settings</span>
        </div>
        <div className="divide-y divide-[#F5F0E8]">
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">Default Upload Node</span>
            <select className="text-xs text-[#3A3328] border border-[#D6CDB8] rounded-lg px-2 py-1 bg-[#FAFAF8] outline-none focus:border-[#7BAF6A]">
              <option>Node 1</option>
              <option>Node 2</option>
              <option>Node 3</option>
            </select>
          </div>
          <div className="flex justify-between items-center px-5 py-3">
            <span className="text-xs text-[#9A9080]">Max File Size</span>
            <select className="text-xs text-[#3A3328] border border-[#D6CDB8] rounded-lg px-2 py-1 bg-[#FAFAF8] outline-none focus:border-[#7BAF6A]">
              <option>50 MB</option>
              <option>100 MB</option>
              <option>500 MB</option>
            </select>
          </div>
        </div>
      </div>

      {/* Save Button */}
      {saved && (
        <div className="text-xs text-[#4A7A3A] bg-[rgba(123,175,106,0.1)] border border-[rgba(123,175,106,0.3)] rounded-lg px-4 py-2.5 mb-3">
          Settings saved successfully.
        </div>
      )}
      <button
        onClick={handleSave}
        className="w-full bg-[#7BAF6A] hover:bg-[#6A9E5A] text-white text-sm font-medium py-2.5 rounded-lg transition-all mb-3"
      >
        Save Settings
      </button>

      {/* Danger Zone */}
      <div className="bg-white border border-[rgba(232,137,106,0.3)] rounded-2xl overflow-hidden">
        <div className="px-5 py-3 border-b border-[rgba(232,137,106,0.2)]">
          <span className="text-xs font-medium text-[#C0562A]">Danger Zone</span>
        </div>
        <div className="px-5 py-4 flex items-center justify-between">
          <div>
            <div className="text-xs font-medium text-[#3A3328]">Sign Out</div>
            <div className="text-[10px] text-[#9A9080] mt-0.5">Log out of your account</div>
          </div>
          <button
            onClick={logout}
            className="text-xs text-[#C0562A] border border-[rgba(232,137,106,0.4)] px-3 py-1.5 rounded-lg hover:bg-[rgba(232,137,106,0.08)] transition-all"
          >
            Sign Out
          </button>
        </div>
      </div>

    </div>
  );
}