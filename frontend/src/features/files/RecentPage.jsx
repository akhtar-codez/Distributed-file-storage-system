import { useEffect, useState } from "react";
import { getFiles, deleteFile, downloadFile } from "./fileAPI";

function formatSize(bytes) {
  if (!bytes) return "0 B";
  if (bytes < 1024) return bytes + " B";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
  return (bytes / 1024 / 1024).toFixed(1) + " MB";
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

export default function RecentPage() {
  const [files, setFiles] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getFiles()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        // Sort by fileId descending — higher id = more recently uploaded
        const sorted = [...data].sort((a, b) => b.fileId - a.fileId);
        setFiles(sorted.slice(0, 10));
      })
      .catch(() => setFiles([]))
      .finally(() => setLoading(false));
  }, []);

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

  return (
    <div>
      <div className="mb-5">
        <h2 className="text-base font-semibold text-[#3A3328]">Recent Files</h2>
        <p className="text-xs text-[#9A9080]">Your 10 most recently uploaded files</p>
      </div>

      <div className="flex flex-col gap-2">
        {loading && (
          <div className="bg-white border border-[#D6CDB8] rounded-xl px-4 py-8 text-center text-xs text-[#9A9080]">
            Loading...
          </div>
        )}

        {!loading && files.length === 0 && (
          <div className="bg-white border border-[#D6CDB8] rounded-xl px-4 py-8 text-center text-xs text-[#9A9080]">
            No files uploaded yet.
          </div>
        )}

        {!loading && files.map((f, index) => (
          <div key={f.fileId} className="bg-white border border-[#D6CDB8] rounded-xl flex items-center gap-4 px-4 py-3 hover:border-[#7BAF6A] transition-all">

            <div className="text-[10px] text-[#B5A898] w-4 flex-shrink-0">{index + 1}</div>

            <div className="w-8 h-8 rounded-lg bg-[#F5F0E8] border border-[#D6CDB8] flex items-center justify-center text-base flex-shrink-0">
              {getEmoji(f.fileName)}
            </div>

            <div className="flex-1 min-w-0">
              <div className="text-sm text-[#3A3328] font-medium truncate">{f.fileName}</div>
              <div className="text-[10px] text-[#9A9080] uppercase">{(f.fileName || "").split(".").pop()}</div>
            </div>

            <div className="text-xs text-[#9A9080] w-16 text-right flex-shrink-0">
              {formatSize(f.fileSize)}
            </div>

            <div className="flex gap-1.5 flex-shrink-0">
              <button
                onClick={() => handleDownload(f.fileId, f.fileName)}
                className="w-7 h-7 rounded-lg bg-[rgba(123,175,106,0.1)] border border-[rgba(123,175,106,0.3)] text-[#4A7A3A] text-xs flex items-center justify-center hover:bg-[rgba(123,175,106,0.2)] transition-all"
              >↓</button>
              <button
                onClick={() => handleDelete(f.fileId)}
                className="w-7 h-7 rounded-lg bg-[rgba(232,137,106,0.1)] border border-[rgba(232,137,106,0.3)] text-[#C0562A] text-xs flex items-center justify-center hover:bg-[rgba(232,137,106,0.2)] transition-all"
              >✕</button>
            </div>

          </div>
        ))}
      </div>
    </div>
  );
}