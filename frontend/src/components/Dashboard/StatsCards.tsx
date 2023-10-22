import {
  UserGroupIcon,
  CurrencyDollarIcon,
  ChartBarIcon,
  ArrowTrendingUpIcon,
} from '@heroicons/react/24/outline';
import { DashboardStats } from '../../types';

interface StatsCardsProps {
  stats: DashboardStats;
}

export default function StatsCards({ stats }: StatsCardsProps) {
  const cards = [
    {
      name: 'Total Contacts',
      value: stats.totalContacts.toLocaleString(),
      icon: UserGroupIcon,
      color: 'bg-blue-500',
      lightColor: 'bg-blue-50',
      textColor: 'text-blue-600',
      change: '+12%',
      changeType: 'positive' as const,
    },
    {
      name: 'Active Deals',
      value: stats.activeDeals.toLocaleString(),
      icon: ChartBarIcon,
      color: 'bg-indigo-500',
      lightColor: 'bg-indigo-50',
      textColor: 'text-indigo-600',
      change: '+8%',
      changeType: 'positive' as const,
    },
    {
      name: 'Revenue',
      value: `$${stats.revenue.toLocaleString()}`,
      icon: CurrencyDollarIcon,
      color: 'bg-green-500',
      lightColor: 'bg-green-50',
      textColor: 'text-green-600',
      change: '+23%',
      changeType: 'positive' as const,
    },
    {
      name: 'Conversion Rate',
      value: `${stats.conversionRate}%`,
      icon: ArrowTrendingUpIcon,
      color: 'bg-amber-500',
      lightColor: 'bg-amber-50',
      textColor: 'text-amber-600',
      change: '+5%',
      changeType: 'positive' as const,
    },
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
      {cards.map((card) => (
        <div key={card.name} className="card">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-500">{card.name}</p>
              <p className="text-2xl font-bold text-gray-900 mt-1">{card.value}</p>
            </div>
            <div className={`${card.lightColor} p-3 rounded-xl`}>
              <card.icon className={`h-6 w-6 ${card.textColor}`} />
            </div>
          </div>
          <div className="mt-3 flex items-center">
            <span className={`text-xs font-medium ${card.changeType === 'positive' ? 'text-green-600' : 'text-red-600'}`}>
              {card.change}
            </span>
            <span className="text-xs text-gray-500 ml-1.5">vs last month</span>
          </div>
        </div>
      ))}
    </div>
  );
}
