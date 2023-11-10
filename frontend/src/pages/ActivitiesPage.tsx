import { useState, useEffect, useCallback } from 'react';
import { PlusIcon, CheckCircleIcon } from '@heroicons/react/24/outline';
import {
  PhoneIcon,
  EnvelopeIcon,
  CalendarIcon,
  ClipboardDocumentCheckIcon,
  DocumentTextIcon,
} from '@heroicons/react/24/outline';
import ActivityForm, { ActivityFormData } from '../components/Activities/ActivityForm';
import { Activity, ActivityType, PageResponse } from '../types';
import { getActivities, createActivity, updateActivity, completeActivity } from '../api/activities';

const activityTabs = [
  { key: 'ALL', label: 'All' },
  { key: ActivityType.CALL, label: 'Calls' },
  { key: ActivityType.EMAIL, label: 'Emails' },
  { key: ActivityType.MEETING, label: 'Meetings' },
  { key: ActivityType.TASK, label: 'Tasks' },
  { key: ActivityType.NOTE, label: 'Notes' },
];

const activityIcons: Record<string, typeof PhoneIcon> = {
  [ActivityType.CALL]: PhoneIcon,
  [ActivityType.EMAIL]: EnvelopeIcon,
  [ActivityType.MEETING]: CalendarIcon,
  [ActivityType.TASK]: ClipboardDocumentCheckIcon,
  [ActivityType.NOTE]: DocumentTextIcon,
};

const activityColors: Record<string, string> = {
  [ActivityType.CALL]: 'bg-blue-50 text-blue-600',
  [ActivityType.EMAIL]: 'bg-green-50 text-green-600',
  [ActivityType.MEETING]: 'bg-purple-50 text-purple-600',
  [ActivityType.TASK]: 'bg-amber-50 text-amber-600',
  [ActivityType.NOTE]: 'bg-gray-50 text-gray-600',
};

export default function ActivitiesPage() {
  const [activities, setActivities] = useState<Activity[]>([]);
  const [loading, setLoading] = useState(true);
  const [activeFilter, setActiveFilter] = useState('ALL');
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingActivity, setEditingActivity] = useState<Activity | null>(null);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  const fetchActivities = useCallback(async (page: number = 0) => {
    try {
      setLoading(true);
      const data: PageResponse<Activity> = await getActivities(page, 20);
      setActivities(data.content);
      setTotalPages(data.totalPages);
      setCurrentPage(data.number);
    } catch (err) {
      console.error('Error fetching activities:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchActivities();
  }, [fetchActivities]);

  const handleComplete = async (id: number) => {
    try {
      await completeActivity(id);
      await fetchActivities(currentPage);
    } catch (err) {
      console.error('Error completing activity:', err);
    }
  };

  const handleFormSubmit = async (data: ActivityFormData) => {
    if (editingActivity) {
      await updateActivity(editingActivity.id, data);
    } else {
      await createActivity(data);
    }
    await fetchActivities(currentPage);
  };

  const filteredActivities = activeFilter === 'ALL'
    ? activities
    : activities.filter((a) => a.type === activeFilter);

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Activities</h1>
          <p className="text-sm text-gray-500 mt-1">Track all your interactions and tasks</p>
        </div>
        <button onClick={() => setIsFormOpen(true)} className="btn-primary flex items-center">
          <PlusIcon className="h-5 w-5 mr-1.5" />
          New Activity
        </button>
      </div>

      <div className="card mb-6 p-0">
        <div className="flex border-b border-gray-200 overflow-x-auto">
          {activityTabs.map((tab) => (
            <button
              key={tab.key}
              onClick={() => setActiveFilter(tab.key)}
              className={`px-5 py-3 text-sm font-medium whitespace-nowrap border-b-2 transition-colors ${
                activeFilter === tab.key
                  ? 'border-indigo-600 text-indigo-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {tab.label}
            </button>
          ))}
        </div>
      </div>

      <div className="card p-0">
        {loading ? (
          <div className="flex items-center justify-center py-12">
            <svg className="animate-spin h-8 w-8 text-indigo-600" fill="none" viewBox="0 0 24 24">
              <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
              <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
            </svg>
          </div>
        ) : filteredActivities.length === 0 ? (
          <div className="text-center py-12 text-gray-500">No activities found.</div>
        ) : (
          <ul className="divide-y divide-gray-200">
            {filteredActivities.map((activity) => {
              const IconComponent = activityIcons[activity.type] || DocumentTextIcon;
              const colorClass = activityColors[activity.type] || activityColors[ActivityType.NOTE];

              return (
                <li key={activity.id} className="px-6 py-4 flex items-center space-x-4 hover:bg-gray-50 transition-colors">
                  <div className={`p-2.5 rounded-lg flex-shrink-0 ${colorClass}`}>
                    <IconComponent className="h-5 w-5" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex items-center space-x-2">
                      <p className={`text-sm font-medium ${activity.completed ? 'text-gray-400 line-through' : 'text-gray-900'}`}>
                        {activity.subject}
                      </p>
                      {activity.completed && (
                        <span className="inline-flex px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800">
                          Done
                        </span>
                      )}
                    </div>
                    <div className="flex items-center space-x-3 mt-1">
                      {activity.contactName && (
                        <span className="text-xs text-gray-500">{activity.contactName}</span>
                      )}
                      {activity.dueDate && (
                        <span className="text-xs text-gray-400">
                          Due: {new Date(activity.dueDate).toLocaleDateString()}
                        </span>
                      )}
                    </div>
                  </div>
                  <div className="flex items-center space-x-2">
                    {!activity.completed && (
                      <button
                        onClick={() => handleComplete(activity.id)}
                        className="p-1.5 rounded-lg text-gray-400 hover:text-green-600 hover:bg-green-50 transition-colors"
                        title="Mark as complete"
                      >
                        <CheckCircleIcon className="h-5 w-5" />
                      </button>
                    )}
                  </div>
                </li>
              );
            })}
          </ul>
        )}

        {totalPages > 1 && (
          <div className="flex items-center justify-between px-6 py-3 border-t border-gray-200 bg-gray-50">
            <span className="text-sm text-gray-700">Page {currentPage + 1} of {totalPages}</span>
            <div className="flex space-x-2">
              <button
                onClick={() => fetchActivities(currentPage - 1)}
                disabled={currentPage === 0}
                className="btn-secondary text-xs disabled:opacity-50"
              >
                Previous
              </button>
              <button
                onClick={() => fetchActivities(currentPage + 1)}
                disabled={currentPage >= totalPages - 1}
                className="btn-secondary text-xs disabled:opacity-50"
              >
                Next
              </button>
            </div>
          </div>
        )}
      </div>

      <ActivityForm
        isOpen={isFormOpen}
        onClose={() => { setIsFormOpen(false); setEditingActivity(null); }}
        onSubmit={handleFormSubmit}
        activity={editingActivity}
      />
    </div>
  );
}
