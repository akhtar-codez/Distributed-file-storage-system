import { useEffect, useState } from "react";
import { getFiles, deleteFile, downloadFile } from "./fileAPI";

const fileEmoji = {
  pdf: "📄", png: "🖼️", jpg: "🖼️", jpeg: "🖼️",
  xlsx: "📊", xls: "📊", zip: "🗜️", rar: "🗜️",
  mp4: "🎬", mkv: "🎬", default: "📁",
};

function getEmoji(filename) {
  const ext = filename.split(".").pop().toLowerCase();
  return fileEmoji[ext] || fileEmoji.default;
}

function formatSize(bytes) {
  if (bytes < 1024) return bytes + " B";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
  return (bytes / 1024 / 1024).toFixed(1) + " MB";
}

function formatDate(dateStr) {
  const d = new Date(dateStr);
  return d.toLocaleDateString("en-US", { month: "short", day: "numeric" });
}

export default function FilesPage() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchFiles = async () => {
    setLoading(true);
    try {
      const res = await getFiles();
      setFiles(res.data);
    } catch {
      setError("Failed to load files.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchFiles(); }, []);

  const handleDelete = async (id) => {
    try {
      await deleteFile(id);
      setFiles(files.filter((f) => f.id !== id));
    } catch {
      alert("Delete failed.");
    }
  };

  const handleDownload = async (id, name) => {
    try {
      const res = await downloadFile(id);
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

  return (
    <div>
      <div className="flex items-center justify-between mb-5">
        <div>
          <h2 className="text-base font-semibold text-[#3A3328]">My Files</h2>
          <p className="text-xs text-[#9A9080]">{files.length} files stored</p>
        </div>
        <button
          onClick={fetchFiles}
          className="text-xs text-[#7BAF6A] border border-[rgba(123,175,106,0.4)] px-3 py-1.5 rounded-lg hover:bg-[rgba(123,175,106,0.08)] transition-all"
        >
          Refresh
        </button>
      </div>

      <div className="bg-white border border-[#D6CDB8] rounded-xl overflow-hidden">

        {/* Header */}
        <div className="grid grid-cols-12 px-4 py-2.5 border-b border-[#EDE6D6] bg-[#FAFAF8]">
          <span className="col-span-5 text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">Name</span>
          <span className="col-span-2 text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">Type</span>
          <span className="col-span-2 text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">Size</span>
          <span className="col-span-2 text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">Date</span>
          <span className="col-span-1 text-[10px] uppercase tracking-wider text-[#9A9080] font-medium">Actions</span>
        </div>

        {/* Loading */}
        {loading && (
          <div className="px-4 py-8 text-center text-xs text-[#9A9080]">Loading files...</div>
        )}

        {/* Error */}
        {error && (
          <div className="px-4 py-8 text-center text-xs text-red-400">{error}</div>
        )}

        {/* Empty */}
        {!loading && !error && files.length === 0 && (
          <div className="px-4 py-8 text-center text-xs text-[#9A9080]">No files uploaded yet.</div>
        )}

        {/* File Rows */}
        {!loading && files.map((f) => (
          <div key={f.id} className="grid grid-cols-12 px-4 py-3 border-b border-[#F5F0E8] last:border-none items-center hover:bg-[#FAFAF8] transition-all">
            <div className="col-span-5 flex items-center gap-3 min-w-0">
              <div className="w-7 h-7 rounded-lg bg-[#F5F0E8] border border-[#D6CDB8] flex items-center justify-center text-sm flex-shrink-0">
                {getEmoji(f.fileName || f.name)}
              </div>
              <span className="text-xs text-[#3A3328] truncate">{f.fileName || f.name}</span>
            </div>
            <span className="col-span-2 text-xs text-[#9A9080] uppercase">
              {(f.fileName || f.name).split(".").pop()}
            </span>
            <span className="col-span-2 text-xs text-[#9A9080]">
              {f.fileSize ? formatSize(f.fileSize) : "-"}
            </span>
            <span className="col-span-2 text-xs text-[#9A9080]">
              {f.uploadedAt ? formatDate(f.uploadedAt) : "-"}
            </span>
            <div className="col-span-1 flex gap-1">
              <button
                onClick={() => handleDownload(f.id, f.fileName || f.name)}
                className="w-6 h-6 rounded-md bg-[rgba(123,175,106,0.1)] border border-[rgba(123,175,106,0.3)] text-[#4A7A3A] text-xs flex items-center justify-center hover:bg-[rgba(123,175,106,0.2)] transition-all"
              >↓</button>
              <button
                onClick={() => handleDelete(f.id)}
                className="w-6 h-6 rounded-md bg-[rgba(232,137,106,0.1)] border border-[rgba(232,137,106,0.3)] text-[#C0562A] text-xs flex items-center justify-center hover:bg-[rgba(232,137,106,0.2)] transition-all"
              >✕</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}