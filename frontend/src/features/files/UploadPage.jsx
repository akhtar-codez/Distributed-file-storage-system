import { useState } from "react";
import { uploadFile } from "./fileAPI";
import { useAuthStore } from "../auth/authStore";

export default function UploadPage() {
  const [file, setFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [status, setStatus] = useState("idle"); // idle | uploading | success | error
  const userId = useAuthStore((state) => state.userId);

  const handleDrop = (e) => {
    e.preventDefault();
    const dropped = e.dataTransfer.files[0];
    if (dropped) setFile(dropped);
  };

  const handleUpload = async () => {
    if (!file) return;
    setStatus("uploading");
    setProgress(0);
    try {
      await uploadFile(file, userId, setProgress);
      setStatus("success");
      setFile(null);
    } catch {
      setStatus("error");
    }
  };

  return (
    <div className="max-w-xl mx-auto mt-8">
      <h2 className="text-base font-semibold text-[#3A3328] mb-1">Upload File</h2>
      <p className="text-xs text-[#9A9080] mb-5">Upload files to your distributed storage</p>

      {/* Drop Zone */}
      <div
        onDrop={handleDrop}
        onDragOver={(e) => e.preventDefault()}
        className="border-2 border-dashed border-[#D6CDB8] rounded-2xl p-10 flex flex-col items-center justify-center gap-3 bg-white cursor-pointer hover:border-[#7BAF6A] transition-all"
        onClick={() => document.getElementById("fileInput").click()}
      >
        <div className="w-12 h-12 rounded-2xl bg-[rgba(123,175,106,0.12)] flex items-center justify-center">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="#7BAF6A" strokeWidth="1.8" strokeLinecap="round">
            <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4"/>
            <polyline points="17 8 12 3 7 8"/>
            <line x1="12" y1="3" x2="12" y2="15"/>
          </svg>
        </div>
        <div className="text-sm font-medium text-[#3A3328]">
          {file ? file.name : "Drag & drop or click to browse"}
        </div>
        <div className="text-xs text-[#9A9080]">
          {file ? `${(file.size / 1024 / 1024).toFixed(2)} MB` : "Any file type supported"}
        </div>
        <input
          id="fileInput"
          type="file"
          className="hidden"
          onChange={(e) => setFile(e.target.files[0])}
        />
      </div>

      {/* Progress Bar */}
      {status === "uploading" && (
        <div className="mt-4">
          <div className="flex justify-between text-xs text-[#9A9080] mb-1">
            <span>Uploading...</span>
            <span>{progress}%</span>
          </div>
          <div className="h-1.5 bg-[#EDE6D6] rounded-full overflow-hidden">
            <div
              className="h-full bg-[#7BAF6A] rounded-full transition-all"
              style={{ width: `${progress}%` }}
            />
          </div>
        </div>
      )}

      {/* Success / Error */}
      {status === "success" && (
        <div className="mt-4 text-xs text-[#4A7A3A] bg-[rgba(123,175,106,0.1)] border border-[rgba(123,175,106,0.3)] rounded-lg px-4 py-2.5">
          File uploaded successfully.
        </div>
      )}
      {status === "error" && (
        <div className="mt-4 text-xs text-red-500 bg-red-50 border border-red-200 rounded-lg px-4 py-2.5">
          Upload failed. Please try again.
        </div>
      )}

      {/* Upload Button */}
      <button
        onClick={handleUpload}
        disabled={!file || status === "uploading"}
        className="mt-4 w-full bg-[#7BAF6A] hover:bg-[#6A9E5A] text-white text-sm font-medium py-2.5 rounded-lg transition-all disabled:opacity-50"
      >
        {status === "uploading" ? "Uploading..." : "Upload File"}
      </button>
    </div>
  );
}