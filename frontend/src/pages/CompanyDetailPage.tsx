import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeftIcon,
  GlobeAltIcon,
  PhoneIcon,
  MapPinIcon,
  PencilIcon,
} from '@heroicons/react/24/outline';
import { Company, Contact, Deal } from '../types';
import { getCompany } from '../api/companies';
import { getContactsByCompany } from '../api/contacts';
import { getDealsByCompany } from '../api/deals';

type TabType = 'contacts' | 'deals';

export default function CompanyDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [company, setCompany] = useState<Company | null>(null);
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [deals, setDeals] = useState<Deal[]>([]);
  const [activeTab, setActiveTab] = useState<TabType>('contacts');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      loadCompanyData(parseInt(id));
    }
  }, [id]);

  const loadCompanyData = async (companyId: number) => {
    try {
      setLoading(true);
      const [companyData, contactsData, dealsData] = await Promise.all([
        getCompany(companyId),
        getContactsByCompany(companyId).catch(() => []),
        getDealsByCompany(companyId).catch(() => []),
      ]);
      setCompany(companyData);
      setContacts(contactsData);
      setDeals(dealsData);
    } catch (err) {
      console.error('Error loading company:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <svg className="animate-spin h-8 w-8 text-indigo-600" fill="none" viewBox="0 0 24 24">
          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
        </svg>
      </div>
    );
  }

  if (!company) {
    return <div className="p-6 text-center text-gray-500">Company not found.</div>;
  }

  return (
    <div className="p-6">
      <button
        onClick={() => navigate('/companies')}
        className="flex items-center text-sm text-gray-500 hover:text-gray-700 mb-6"
      >
        <ArrowLeftIcon className="h-4 w-4 mr-1" />
        Back to Companies
      </button>

      <div className="card mb-6">
        <div className="flex items-start justify-between">
          <div className="flex items-center space-x-4">
            <div className="h-16 w-16 rounded-xl bg-indigo-100 flex items-center justify-center">
              <span className="text-indigo-600 font-bold text-xl">
                {company.name?.charAt(0)?.toUpperCase()}
              </span>
            </div>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">{company.name}</h1>
              {company.industry && (
                <p className="text-gray-500">{company.industry}</p>
              )}
              {company.size && (
                <span className="inline-flex mt-2 px-2.5 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800">
                  {company.size}
                </span>
              )}
            </div>
          </div>
          <button className="btn-secondary flex items-center">
            <PencilIcon className="h-4 w-4 mr-1.5" />
            Edit
          </button>
        </div>

        <div className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
          {company.website && (
            <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
              <GlobeAltIcon className="h-5 w-5 text-gray-400" />
              <div>
                <p className="text-xs text-gray-500">Website</p>
                <p className="text-sm text-indigo-600">{company.website}</p>
              </div>
            </div>
          )}
          <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
            <PhoneIcon className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-xs text-gray-500">Phone</p>
              <p className="text-sm text-gray-900">{company.phone || 'N/A'}</p>
            </div>
          </div>
          <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
            <MapPinIcon className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-xs text-gray-500">Address</p>
              <p className="text-sm text-gray-900">{company.address || 'N/A'}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="card p-0 overflow-hidden">
        <div className="border-b border-gray-200">
          <nav className="flex -mb-px">
            <button
              onClick={() => setActiveTab('contacts')}
              className={`px-6 py-3 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'contacts'
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Contacts ({contacts.length})
            </button>
            <button
              onClick={() => setActiveTab('deals')}
              className={`px-6 py-3 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'deals'
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              Deals ({deals.length})
            </button>
          </nav>
        </div>

        <div className="p-6">
          {activeTab === 'contacts' && (
            <div>
              {contacts.length === 0 ? (
                <p className="text-center text-gray-500 py-8">No contacts associated.</p>
              ) : (
                <ul className="divide-y divide-gray-200">
                  {contacts.map((contact) => (
                    <li key={contact.id} className="py-3 flex items-center justify-between cursor-pointer hover:bg-gray-50 -mx-2 px-2 rounded-lg" onClick={() => navigate(`/contacts/${contact.id}`)}>
                      <div className="flex items-center space-x-3">
                        <div className="h-8 w-8 rounded-full bg-indigo-100 flex items-center justify-center">
                          <span className="text-indigo-600 text-xs font-medium">
                            {contact.firstName?.charAt(0)}{contact.lastName?.charAt(0)}
                          </span>
                        </div>
                        <div>
                          <p className="text-sm font-medium text-gray-900">{contact.firstName} {contact.lastName}</p>
                          <p className="text-xs text-gray-500">{contact.email}</p>
                        </div>
                      </div>
                      <span className="text-xs text-gray-500">{contact.status}</span>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}

          {activeTab === 'deals' && (
            <div>
              {deals.length === 0 ? (
                <p className="text-center text-gray-500 py-8">No deals associated.</p>
              ) : (
                <ul className="divide-y divide-gray-200">
                  {deals.map((deal) => (
                    <li key={deal.id} className="py-3 flex items-center justify-between">
                      <div>
                        <p className="text-sm font-medium text-gray-900">{deal.title}</p>
                        <p className="text-xs text-gray-500">{deal.stage}</p>
                      </div>
                      <p className="text-sm font-semibold text-gray-900">
                        ${deal.value?.toLocaleString()}
                      </p>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
