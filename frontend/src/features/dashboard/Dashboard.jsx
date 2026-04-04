import { useEffect, useState } from "react";
import { getFiles, deleteFile, downloadFile } from "../files/fileAPI";
import { useNavigate } from "react-router-dom";

function formatSize(bytes) {
  if (!bytes) return "0 B";
  if (bytes < 1024) return bytes + " B";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
  return (bytes / 1024 / 1024).toFixed(1) + " MB";
}

function formatDate(dateStr) {
  if (!dateStr) return "-";
  const d = new Date(dateStr);
  return d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
}

const fileEmoji = {
  pdf: "📄", png: "🖼️", jpg: "🖼️", jpeg: "🖼️",
  xlsx: "📊", xls: "📊", zip: "🗜️", rar: "🗜️",
  mp4: "🎬", mkv: "🎬", default: "📁",
};

function getEmoji(filename) {
  const ext = (filename || "").split(".").pop().toLowerCase();
  return fileEmoji[ext] || fileEmoji.default;
}

function getTotalSize(files) {
  const total = files.reduce((acc, f) => acc + (f.fileSize || 0), 0);
  if (total < 1024) return total + " B";
  if (total < 1024 * 1024) return (total / 1024).toFixed(1) + " KB";
  return (total / 1024 / 1024).toFixed(1) + " MB";
}

function getBreakdown(files) {
  const categories = { Documents: 0, Images: 0, Archives: 0, Videos: 0, Others: 0 };
  const colorMap = {
    Documents: "#7BAF6A", Images: "#D4A847",
    Archives: "#C47850", Videos: "#B8A8C8", Others: "#9A9080",
  };
  files.forEach((f) => {
    const ext = (f.fileName || "").split(".").pop().toLowerCase();
    if (["pdf", "doc", "docx", "txt", "xlsx", "xls"].includes(ext)) categories.Documents++;
    else if (["png", "jpg", "jpeg", "gif", "webp"].includes(ext)) categories.Images++;
    else if (["zip", "rar", "tar", "gz"].includes(ext)) categories.Archives++;
    else if (["mp4", "mkv", "avi", "mov"].includes(ext)) categories.Videos++;
    else categories.Others++;
  });
  const max = Math.max(...Object.values(categories), 1);
  return Object.entries(categories).map(([type, count]) => ({
    type, count: `${count} files`,
    width: `${Math.round((count / max) * 100)}%`,
    color: colorMap[type],
  }));
}

export default function Dashboard() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    getFiles()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        setFiles(data);
      })
      .catch(() => setFiles([]))
      .finally(() => setLoading(false));
  }, []);

  const recentFiles = files.slice(0, 4);
  const breakdown = getBreakdown(files);

  const handleDelete = async (fileId) => {
    try {
      await deleteFile(fileId);
      setFiles(files.filter((f) => f.fileId !== fileId));
    } catch {
      alert("Delete failed.");
    }
  };

  const handleDownload = async (fileId, name) => {
    try {
      const res = await downloadFile(fileId);
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const a = document.createElement("a");
      a.href = url;
      a.download = name;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch {
      alert("Download failed.");
    }
  };

  const stats = [
    {
      label: "Total Files", value: loading ? "..." : files.length,
      icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#4A7A3A" strokeWidth="1.8" strokeLinecap="round"><path d="M13 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V9z"/><polyline points="13 2 13 9 20 9"/></svg>,
      bg: "bg-[rgba(123,175,106,0.15)]",
    },
    {
      label: "Storage Used", value: loading ? "..." : getTotalSize(files),
      icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#8A6A10" strokeWidth="1.8" strokeLinecap="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>,
      bg: "bg-[rgba(212,168,71,0.15)]",
    },
    {
      label: "Active Nodes", value: "3",
      icon: <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#8A4A20" strokeWidth="1.8" strokeLinecap="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>,
      bg: "bg-[rgba(196,120,80,0.12)]",
    },
  ];

  return (
    <div className="flex flex-col gap-4">

      {/* Stat Cards */}
      <div className="grid grid-cols-3 gap-3">
        {stats.map((s) => (
          <div key={s.label} className="bg-white border border-[#D6CDB8] rounded-xl p-4 flex items-center gap-3">
            <div className={`w-9 h-9 rounded-xl flex items-center justify-center flex-shrink-0 ${s.bg}`}>
              {s.icon}
            </div>
            <div>
              <div className="text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">{s.label}</div>
              <div className="text-xl font-semibold text-[#3A3328] mt-0.5">{s.value}</div>
            </div>
          </div>
        ))}
      </div>

      {/* Bottom Row */}
      <div className="grid gap-3" style={{ gridTemplateColumns: "1.8fr 1fr" }}>

        {/* Recent Files */}
        <div className="bg-white border border-[#D6CDB8] rounded-xl overflow-hidden">
          <div className="flex items-center justify-between px-4 py-2.5 border-b border-[#EDE6D6]">
            <span className="text-xs font-medium text-[#6A5E52]">Recent Files</span>
            <span
              onClick={() => navigate("/files")}
              className="text-[10px] text-[#7BAF6A] cursor-pointer hover:underline"
            >View all →</span>
          </div>

          {loading && <div className="px-4 py-6 text-xs text-[#9A9080] text-center">Loading...</div>}
          {!loading && recentFiles.length === 0 && (
            <div className="px-4 py-6 text-xs text-[#9A9080] text-center">No files yet.</div>
          )}
          {!loading && recentFiles.map((f) => (
            <div key={f.fileId} className="flex items-center gap-3 px-4 py-2 border-b border-[#F5F0E8] last:border-none">
              <div className="w-7 h-7 rounded-lg bg-[#F5F0E8] border border-[#D6CDB8] flex items-center justify-center text-sm flex-shrink-0">
                {getEmoji(f.fileName)}
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-xs text-[#3A3328] truncate">{f.fileName}</div>
                <div className="text-[9px] text-[#9A9080] uppercase">{(f.fileName || "").split(".").pop()}</div>
              </div>
              <div className="text-[10px] text-[#9A9080] w-10 text-right">{formatSize(f.fileSize)}</div>
              <div className="text-[10px] text-[#9A9080] w-12 text-right">{formatDate(f.uploadedAt)}</div>
              <div className="flex gap-1">
                <button onClick={() => handleDownload(f.fileId, f.fileName)} className="w-6 h-6 rounded-md bg-[rgba(123,175,106,0.1)] border border-[rgba(123,175,106,0.3)] text-[#4A7A3A] text-xs flex items-center justify-center">↓</button>
                <button onClick={() => handleDelete(f.fileId)} className="w-6 h-6 rounded-md bg-[rgba(232,137,106,0.1)] border border-[rgba(232,137,106,0.3)] text-[#C0562A] text-xs flex items-center justify-center">✕</button>
              </div>
            </div>
          ))}
        </div>

        {/* Storage Breakdown */}
        <div className="bg-white border border-[#D6CDB8] rounded-xl overflow-hidden flex flex-col">
          <div className="px-4 py-2.5 border-b border-[#EDE6D6]">
            <span className="text-xs font-medium text-[#6A5E52]">Storage Breakdown</span>
          </div>
          <div className="flex flex-col gap-3 p-4 flex-1">
            {breakdown.map((b) => (
              <div key={b.type} className="flex flex-col gap-1">
                <div className="flex justify-between">
                  <span className="text-[10px] text-[#6A5E52]">{b.type}</span>
                  <span className="text-[10px] text-[#9A9080]">{b.count}</span>
                </div>
                <div className="h-1 rounded-full bg-[#EDE6D6] overflow-hidden">
                  <div className="h-full rounded-full transition-all" style={{ width: b.width, background: b.color }}></div>
                </div>
              </div>
            ))}
          </div>
          <div
            onClick={() => navigate("/upload")}
            className="mx-3 mb-3 bg-[#7BAF6A] hover:bg-[#6A9E5A] rounded-lg py-2 text-center text-xs font-medium text-white cursor-pointer transition-all"
          >
            + Upload New File
          </div>
        </div>

      </div>
    </div>
  );
}