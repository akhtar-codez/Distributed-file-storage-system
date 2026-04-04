import api from "../../shared/lib/axios";

export const getFiles = () => api.get("/files");

export const uploadFile = (file, userId, onProgress) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("userId", userId);

  return api.post("/files/upload", formData, {
    onUploadProgress: (e) => {
      const percent = Math.round((e.loaded * 100) / e.total);
      onProgress(percent);
    },
  });
};

export const deleteFile = (id) => api.delete(`/files/${id}`);

export const downloadFile = (id) => api.get(`/files/${id}/download`, {
  responseType: "blob",
});