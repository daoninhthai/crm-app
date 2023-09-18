import { useState, useCallback, useEffect } from 'react';
import { PlusIcon, MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import ContactTable from '../components/Contacts/ContactTable';
import ContactForm, { ContactFormData } from '../components/Contacts/ContactForm';
import { useContacts } from '../hooks/useContacts';
import { Contact, ContactStatus } from '../types';

export default function ContactsPage() {
  const {
    contacts,
    loading,
    error,
    totalPages,
    totalElements,
    currentPage,
    fetchContacts,
    searchContactsList,
    addContact,
    editContact,
    removeContact,
  } = useContacts();

  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [sortField, setSortField] = useState('createdAt');
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('desc');
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingContact, setEditingContact] = useState<Contact | null>(null);

  const handleSearch = useCallback(
    (term: string) => {
      if (term.trim()) {
        searchContactsList(term, 0);
      } else {
        fetchContacts(0);
      }
    },
    [searchContactsList, fetchContacts]
  );

  useEffect(() => {
    const timer = setTimeout(() => {
      handleSearch(searchTerm);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchTerm, handleSearch]);

  const handleSort = (field: string) => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc');
    } else {
      setSortField(field);
      setSortDirection('asc');
    }
  };

  const handleEdit = (contact: Contact) => {
    setEditingContact(contact);
    setIsFormOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this contact?')) {
      try {
        await removeContact(id);
      } catch (err) {
        console.error('Error deleting contact:', err);
      }
    }
  };

  const handleFormSubmit = async (data: ContactFormData) => {
    if (editingContact) {
      await editContact(editingContact.id, data);
    } else {
      await addContact(data);
    }
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setEditingContact(null);
  };

  const filteredContacts = statusFilter === 'ALL'
    ? contacts
    : contacts.filter((c) => c.status === statusFilter);

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Contacts</h1>
          <p className="text-sm text-gray-500 mt-1">{totalElements} contacts total</p>
        </div>
        <button
          onClick={() => setIsFormOpen(true)}
          className="btn-primary flex items-center"
        >
          <PlusIcon className="h-5 w-5 mr-1.5" />
          New Contact
        </button>
      </div>

      <div className="card mb-6">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1 relative">
            <MagnifyingGlassIcon className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search contacts..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10"
            />
          </div>
          <select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            className="input-field w-full sm:w-48"
          >
            <option value="ALL">All Statuses</option>
            {Object.values(ContactStatus).map((status) => (
              <option key={status} value={status}>
                {status.charAt(0) + status.slice(1).toLowerCase()}
              </option>
            ))}
          </select>
        </div>
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm">
          {error}
        </div>
      )}

      <div className="card p-0 overflow-hidden">
        {loading ? (
          <div className="flex items-center justify-center py-12">
            <svg className="animate-spin h-8 w-8 text-indigo-600" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
            </svg>
          </div>
        ) : (
          <ContactTable
            contacts={filteredContacts}
            onEdit={handleEdit}
            onDelete={handleDelete}
            sortField={sortField}
            sortDirection={sortDirection}
            onSort={handleSort}
          />
        )}

        {totalPages > 1 && (
          <div className="flex items-center justify-between px-6 py-3 border-t border-gray-200 bg-gray-50">
            <div className="text-sm text-gray-700">
              Page {currentPage + 1} of {totalPages} ({totalElements} results)
            </div>
            <div className="flex space-x-2">
              <button
                onClick={() => fetchContacts(currentPage - 1)}
                disabled={currentPage === 0}
                className="btn-secondary text-xs disabled:opacity-50"
              >
                Previous
              </button>
              {Array.from({ length: Math.min(5, totalPages) }, (_, i) => {
                const pageNum = Math.max(0, Math.min(currentPage - 2, totalPages - 5)) + i;
                return (
                  <button
                    key={pageNum}
                    onClick={() => fetchContacts(pageNum)}
                    className={`px-3 py-1.5 text-xs rounded-lg font-medium transition-colors ${
                      pageNum === currentPage
                        ? 'bg-indigo-600 text-white'
                        : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
                    }`}
                  >
                    {pageNum + 1}
                  </button>
                );
              })}
              <button
                onClick={() => fetchContacts(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
                className="btn-secondary text-xs disabled:opacity-50"
              >
                Next
              </button>
            </div>
          </div>
        )}
      </div>

      <ContactForm
        isOpen={isFormOpen}
        onClose={handleCloseForm}
        onSubmit={handleFormSubmit}
        contact={editingContact}
      />
    </div>
  );
}
