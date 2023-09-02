import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import MainLayout from './components/Layout/MainLayout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

function DashboardPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Dashboard</h1><p className="text-gray-500 mt-2">Welcome to your CRM dashboard.</p></div>;
}

function ContactsPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Contacts</h1><p className="text-gray-500 mt-2">Manage your contacts here.</p></div>;
}

function CompaniesPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Companies</h1><p className="text-gray-500 mt-2">Manage your companies here.</p></div>;
}

function DealsPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Deals</h1><p className="text-gray-500 mt-2">Manage your deals pipeline here.</p></div>;
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
            <Route path="/dashboard" element={<DashboardPlaceholder />} />
            <Route path="/contacts" element={<ContactsPlaceholder />} />
            <Route path="/contacts/:id" element={<ContactsPlaceholder />} />
            <Route path="/companies" element={<CompaniesPlaceholder />} />
            <Route path="/companies/:id" element={<CompaniesPlaceholder />} />
            <Route path="/deals" element={<DealsPlaceholder />} />
            <Route path="/activities" element={<ActivitiesPlaceholder />} />
          </Route>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
