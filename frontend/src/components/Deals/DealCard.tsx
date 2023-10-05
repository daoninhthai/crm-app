import { Deal } from '../../types';
import { BuildingOfficeIcon, UserIcon, CalendarIcon } from '@heroicons/react/24/outline';

interface DealCardProps {
  deal: Deal;
  onClick: (deal: Deal) => void;
}

export default function DealCard({ deal, onClick }: DealCardProps) {
  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(value);
  };

  const probabilityColor =
    deal.probability >= 70
      ? 'bg-green-500'
      : deal.probability >= 40
      ? 'bg-yellow-500'
      : 'bg-red-500';

  return (
    <div
      className="bg-white rounded-lg shadow-sm border border-gray-200 p-4 cursor-pointer hover:shadow-md transition-shadow duration-200"
      onClick={() => onClick(deal)}
    >
      <div className="flex items-start justify-between mb-3">
        <h4 className="text-sm font-semibold text-gray-900 truncate flex-1 mr-2">
          {deal.title}
        </h4>
      </div>

      <div className="text-lg font-bold text-indigo-600 mb-3">
        {formatCurrency(deal.value)}
      </div>

      {deal.probability != null && (
        <div className="mb-3">
          <div className="flex items-center justify-between mb-1">
            <span className="text-xs text-gray-500">Probability</span>
            <span className="text-xs font-medium text-gray-700">{deal.probability}%</span>
          </div>
          <div className="w-full bg-gray-200 rounded-full h-1.5">
            <div
              className={`${probabilityColor} h-1.5 rounded-full transition-all duration-300`}
              style={{ width: `${Math.min(deal.probability, 100)}%` }}
            />
          </div>
        </div>
      )}

      <div className="space-y-1.5">
        {deal.companyName && (
          <div className="flex items-center text-xs text-gray-500">
            <BuildingOfficeIcon className="h-3.5 w-3.5 mr-1.5 flex-shrink-0" />
            <span className="truncate">{deal.companyName}</span>
          </div>
        )}
        {deal.contactName && (
          <div className="flex items-center text-xs text-gray-500">
            <UserIcon className="h-3.5 w-3.5 mr-1.5 flex-shrink-0" />
            <span className="truncate">{deal.contactName}</span>
          </div>
        )}
        {deal.expectedCloseDate && (
          <div className="flex items-center text-xs text-gray-500">
            <CalendarIcon className="h-3.5 w-3.5 mr-1.5 flex-shrink-0" />
            <span>{new Date(deal.expectedCloseDate).toLocaleDateString()}</span>
          </div>
        )}
      </div>
    </div>
  );
}
