import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

function LoginPlaceholder() {
  return <div className="flex items-center justify-center min-h-screen bg-gray-100"><p className="text-gray-500">Login Page</p></div>;
}

function RegisterPlaceholder() {
  return <div className="flex items-center justify-center min-h-screen bg-gray-100"><p className="text-gray-500">Register Page</p></div>;
}

function DashboardPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Dashboard</h1></div>;
}

function ContactsPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Contacts</h1></div>;
}

function CompaniesPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Companies</h1></div>;
}

function DealsPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Deals</h1></div>;
}

function ActivitiesPlaceholder() {
  return <div className="p-6"><h1 className="text-2xl font-bold text-gray-900">Activities</h1></div>;
}

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPlaceholder />} />
        <Route path="/register" element={<RegisterPlaceholder />} />
        <Route path="/dashboard" element={<DashboardPlaceholder />} />
        <Route path="/contacts" element={<ContactsPlaceholder />} />
        <Route path="/companies" element={<CompaniesPlaceholder />} />
        <Route path="/deals" element={<DealsPlaceholder />} />
        <Route path="/activities" element={<ActivitiesPlaceholder />} />
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
