import { Deal, DealStage } from '../../types';
import DealCard from './DealCard';

interface PipelineBoardProps {
  pipeline: Record<string, Deal[]>;
  onDealClick: (deal: Deal) => void;
}

const stageConfig: { key: DealStage; label: string; color: string }[] = [
  { key: DealStage.LEAD, label: 'Lead', color: 'bg-gray-400' },
  { key: DealStage.QUALIFIED, label: 'Qualified', color: 'bg-blue-500' },
  { key: DealStage.PROPOSAL, label: 'Proposal', color: 'bg-yellow-500' },
  { key: DealStage.NEGOTIATION, label: 'Negotiation', color: 'bg-orange-500' },
  { key: DealStage.CLOSED_WON, label: 'Closed Won', color: 'bg-green-500' },
  { key: DealStage.CLOSED_LOST, label: 'Closed Lost', color: 'bg-red-500' },
];

export default function PipelineBoard({ pipeline, onDealClick }: PipelineBoardProps) {
  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(value);
  };

  return (
    <div className="flex space-x-4 overflow-x-auto pb-4" style={{ minHeight: '70vh' }}>
      {stageConfig.map(({ key, label, color }) => {
        const deals = pipeline[key] || [];
        const totalValue = deals.reduce((sum, d) => sum + (d.value || 0), 0);

        return (
          <div
            key={key}
            className="flex-shrink-0 w-72 bg-gray-50 rounded-xl border border-gray-200"
          >
            <div className="p-4 border-b border-gray-200">
              <div className="flex items-center justify-between mb-1">
                <div className="flex items-center space-x-2">
                  <div className={`w-2.5 h-2.5 rounded-full ${color}`} />
                  <h3 className="text-sm font-semibold text-gray-900">{label}</h3>
                </div>
                <span className="inline-flex items-center justify-center w-6 h-6 rounded-full bg-gray-200 text-xs font-medium text-gray-700">
                  {deals.length}
                </span>
              </div>
              <p className="text-xs text-gray-500">{formatCurrency(totalValue)}</p>
            </div>

            <div className="p-3 space-y-3 overflow-y-auto" style={{ maxHeight: 'calc(70vh - 80px)' }}>
              {deals.map((deal) => (
                <DealCard key={deal.id} deal={deal} onClick={onDealClick} />
              ))}
              {deals.length === 0 && (
                <div className="text-center py-8">
                  <p className="text-xs text-gray-400">No deals</p>
                </div>
              )}
            </div>
          </div>
        );
      })}
    </div>
  );
}
