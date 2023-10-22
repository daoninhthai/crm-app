import {
  PhoneIcon,
  EnvelopeIcon,
  CalendarIcon,
  ClipboardDocumentCheckIcon,
  DocumentTextIcon,
} from '@heroicons/react/24/outline';
import { Activity, ActivityType } from '../../types';

interface RecentActivitiesProps {
  activities: Activity[];
}

const activityIcons: Record<string, { icon: typeof PhoneIcon; bg: string; color: string }> = {
  [ActivityType.CALL]: { icon: PhoneIcon, bg: 'bg-blue-50', color: 'text-blue-600' },
  [ActivityType.EMAIL]: { icon: EnvelopeIcon, bg: 'bg-green-50', color: 'text-green-600' },
  [ActivityType.MEETING]: { icon: CalendarIcon, bg: 'bg-purple-50', color: 'text-purple-600' },
  [ActivityType.TASK]: { icon: ClipboardDocumentCheckIcon, bg: 'bg-amber-50', color: 'text-amber-600' },
  [ActivityType.NOTE]: { icon: DocumentTextIcon, bg: 'bg-gray-50', color: 'text-gray-600' },
};

function timeAgo(dateString: string): string {
  const now = new Date();
  const date = new Date(dateString);
  const diffMs = now.getTime() - date.getTime();
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMins / 60);
  const diffDays = Math.floor(diffHours / 24);

  if (diffMins < 1) return 'just now';
  if (diffMins < 60) return `${diffMins}m ago`;
  if (diffHours < 24) return `${diffHours}h ago`;
  if (diffDays < 7) return `${diffDays}d ago`;
  return date.toLocaleDateString();
}

export default function RecentActivities({ activities }: RecentActivitiesProps) {
  return (
    <div className="card">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-semibold text-gray-900">Recent Activities</h3>
        <a href="/activities" className="text-sm text-indigo-600 hover:text-indigo-500 font-medium">
          View all
        </a>
      </div>

      <div className="space-y-1">
        {activities.length === 0 ? (
          <p className="text-center text-gray-500 py-8 text-sm">No recent activities.</p>
        ) : (
          <ul className="divide-y divide-gray-100">
            {activities.map((activity) => {
              const iconConfig = activityIcons[activity.type] || activityIcons[ActivityType.NOTE];
              const IconComponent = iconConfig.icon;

              return (
                <li key={activity.id} className="py-3 flex items-start space-x-3">
                  <div className={`${iconConfig.bg} p-2 rounded-lg flex-shrink-0`}>
                    <IconComponent className={`h-4 w-4 ${iconConfig.color}`} />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate">
                      {activity.subject}
                    </p>
                    <div className="flex items-center space-x-2 mt-0.5">
                      {activity.contactName && (
                        <span className="text-xs text-gray-500">{activity.contactName}</span>
                      )}
                      <span className="text-xs text-gray-400">{timeAgo(activity.createdAt)}</span>
                    </div>
                  </div>
                  {activity.completed && (
                    <span className="inline-flex px-2 py-0.5 rounded text-xs font-medium bg-green-100 text-green-800">
                      Done
                    </span>
                  )}
                </li>
              );
            })}
          </ul>
        )}
      </div>
    </div>
  );
}
