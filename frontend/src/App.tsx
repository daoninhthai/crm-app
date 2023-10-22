import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import MainLayout from './components/Layout/MainLayout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ContactsPage from './pages/ContactsPage';
import ContactDetailPage from './pages/ContactDetailPage';
import DealsPage from './pages/DealsPage';

function CompaniesPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Companies</h1><p className="text-gray-500 mt-2">Manage your companies here.</p></div>;
}

function ActivitiesPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Activities</h1><p className="text-gray-500 mt-2">Track your activities here.</p></div>;
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            element={
              <ProtectedRoute>
                <MainLayout />
              </ProtectedRoute>
            }
          >
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/contacts" element={<ContactsPage />} />
            <Route path="/contacts/:id" element={<ContactDetailPage />} />
            <Route path="/companies" element={<CompaniesPlaceholder />} />
            <Route path="/companies/:id" element={<CompaniesPlaceholder />} />
            <Route path="/deals" element={<DealsPage />} />
            <Route path="/activities" element={<ActivitiesPlaceholder />} />
          </Route>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
