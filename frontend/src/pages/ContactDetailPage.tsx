import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeftIcon,
  EnvelopeIcon,
  PhoneIcon,
  BuildingOfficeIcon,
  PencilIcon,
} from '@heroicons/react/24/outline';
import { Contact, Activity, Deal } from '../types';
import { getContact } from '../api/contacts';

const statusColors: Record<string, string> = {
  LEAD: 'bg-blue-100 text-blue-800',
  PROSPECT: 'bg-yellow-100 text-yellow-800',
  CUSTOMER: 'bg-green-100 text-green-800',
  INACTIVE: 'bg-gray-100 text-gray-800',
};

type TabType = 'activities' | 'deals';

export default function ContactDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [contact, setContact] = useState<Contact | null>(null);
  const [activeTab, setActiveTab] = useState<TabType>('activities');
  const [loading, setLoading] = useState(true);
  const [_activities] = useState<Activity[]>([]);
  const [_deals] = useState<Deal[]>([]);

  useEffect(() => {
    if (id) {
      loadContact(parseInt(id));
    }
  }, [id]);

  const loadContact = async (contactId: number) => {
    try {
      setLoading(true);
      const data = await getContact(contactId);
      setContact(data);
    } catch (err) {
      console.error('Error loading contact:', err);
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

  if (!contact) {
    return (
      <div className="p-6 text-center text-gray-500">Contact not found.</div>
    );
  }

  return (
    <div className="p-6">
      <button
        onClick={() => navigate('/contacts')}
        className="flex items-center text-sm text-gray-500 hover:text-gray-700 mb-6"
      >
        <ArrowLeftIcon className="h-4 w-4 mr-1" />
        Back to Contacts
      </button>

      <div className="card mb-6">
        <div className="flex items-start justify-between">
          <div className="flex items-center space-x-4">
            <div className="h-16 w-16 rounded-full bg-indigo-100 flex items-center justify-center">
              <span className="text-indigo-600 font-bold text-xl">
                {contact.firstName?.charAt(0)}{contact.lastName?.charAt(0)}
              </span>
            </div>
            <div>
              <h1 className="text-2xl font-bold text-gray-900">
                {contact.firstName} {contact.lastName}
              </h1>
              {contact.position && (
                <p className="text-gray-500">{contact.position}</p>
              )}
              <span className={`inline-flex mt-2 px-2.5 py-0.5 rounded-full text-xs font-medium ${statusColors[contact.status]}`}>
                {contact.status}
              </span>
            </div>
          </div>
          <button className="btn-secondary flex items-center">
            <PencilIcon className="h-4 w-4 mr-1.5" />
            Edit
          </button>
        </div>

        <div className="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
            <EnvelopeIcon className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-xs text-gray-500">Email</p>
              <p className="text-sm text-gray-900">{contact.email}</p>
            </div>
          </div>
          <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
            <PhoneIcon className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-xs text-gray-500">Phone</p>
              <p className="text-sm text-gray-900">{contact.phone || 'N/A'}</p>
            </div>
          </div>
          <div className="flex items-center space-x-3 p-3 bg-gray-50 rounded-lg">
            <BuildingOfficeIcon className="h-5 w-5 text-gray-400" />
            <div>
              <p className="text-xs text-gray-500">Company</p>
              <p className="text-sm text-gray-900">{contact.companyName || 'N/A'}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="card p-0 overflow-hidden">
        <div className="border-b border-gray-200">
          <nav className="flex -mb-px">
            <button
              onClick={() => setActiveTab('activities')}
              className={`px-6 py-3 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'activities'
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Activities
            </button>
            <button
              onClick={() => setActiveTab('deals')}
              className={`px-6 py-3 text-sm font-medium border-b-2 transition-colors ${
                activeTab === 'deals'
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
              }`}
            >
              Deals
            </button>
          </nav>
        </div>

        <div className="p-6">
          {activeTab === 'activities' && (
            <div>
              {_activities.length === 0 ? (
                <p className="text-center text-gray-500 py-8">No activities yet.</p>
              ) : (
                <ul className="divide-y divide-gray-200">
                  {_activities.map((activity) => (
                    <li key={activity.id} className="py-3">
                      <p className="text-sm font-medium text-gray-900">{activity.subject}</p>
                      <p className="text-xs text-gray-500">{activity.type} - {activity.createdAt}</p>
                    </li>
                  ))}
                </ul>
              )}
            </div>
          )}

          {activeTab === 'deals' && (
            <div>
              {_deals.length === 0 ? (
                <p className="text-center text-gray-500 py-8">No deals yet.</p>
              ) : (
                <ul className="divide-y divide-gray-200">
                  {_deals.map((deal) => (
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
