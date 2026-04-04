import { BrowserRouter, Routes, Route } from "react-router-dom";
import Layout from "./shared/layout/Layout";
import Dashboard from "./features/dashboard/Dashboard";
import Login from "./features/auth/Login";
import Register from "./features/auth/Register";
import UploadPage from "./features/files/UploadPage";
import FilesPage from "./features/files/FilesPage";
import RecentPage from "./features/files/RecentPage";
import ProfilePage from "./features/auth/ProfilePage";
import SettingsPage from "./features/auth/SettingsPage";
import ProtectedRoute from "./app/ProtectedRoute";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/*" element={
          <ProtectedRoute>
            <Layout>
              <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/upload" element={<UploadPage />} />
                <Route path="/files" element={<FilesPage />} />
                <Route path="/recent" element={<RecentPage />} />
                <Route path="/profile" element={<ProfilePage />} />
                <Route path="/settings" element={<SettingsPage />} />
              </Routes>
            </Layout>
          </ProtectedRoute>
        } />
      </Routes>
    </BrowserRouter>
  );
}

export default App;