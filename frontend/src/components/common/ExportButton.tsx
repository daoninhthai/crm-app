import { useState } from 'react';
import { ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import apiClient from '../../api/client';

interface ExportButtonProps {
  endpoint: string;
  filename: string;
  label?: string;
  className?: string;
}

export default function ExportButton({
  endpoint,
  filename,
  label = 'Export CSV',
  className = '',
}: ExportButtonProps) {
  const [loading, setLoading] = useState(false);

  const handleExport = async () => {
    setLoading(true);
    try {
      const response = await apiClient.get(endpoint, {
        responseType: 'blob',
      });

      const blob = new Blob([response.data], { type: 'text/csv;charset=utf-8;' });
      const url = window.URL.createObjectURL(blob);

      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();

      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Export failed:', err);
      alert('Failed to export data. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <button
      onClick={handleExport}
      disabled={loading}
      className={`btn-secondary flex items-center disabled:opacity-50 ${className}`}
    >
      {loading ? (
        <svg className="animate-spin h-4 w-4 mr-1.5 text-gray-500" fill="none" viewBox="0 0 24 24">
          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
        </svg>
      ) : (
        <ArrowDownTrayIcon className="h-4 w-4 mr-1.5" />
      )}
      {loading ? 'Exporting...' : label}
    </button>
  );
}
