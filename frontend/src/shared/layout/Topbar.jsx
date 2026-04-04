export default function Topbar() {
  return (
    <div className="h-14 bg-[#F5F0E8] border-b border-[#D6CDB8] flex items-center justify-between px-6">
      
      <div>
        <div className="text-sm font-semibold text-[#3A3328]">Good morning, Akinator</div>
        <div className="text-xs text-[#9A9080]">3 files uploaded this week</div>
      </div>

      <div className="flex items-center gap-3">
        {/* Search */}
        <div className="flex items-center gap-2 bg-white border border-[#D6CDB8] rounded-lg px-3 py-1.5">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="#9A9080" strokeWidth="2" strokeLinecap="round">
            <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
          </svg>
          <input
            type="text"
            placeholder="Search files..."
            className="bg-transparent text-xs text-[#3A3328] outline-none w-36 placeholder-[#B5A898]"
          />
        </div>

        {/* Upload button */}
        <button className="bg-[#7BAF6A] hover:bg-[#6A9E5A] text-white text-xs font-medium px-4 py-2 rounded-full transition-all">
          + Upload File
        </button>
      </div>

    </div>
  );
}