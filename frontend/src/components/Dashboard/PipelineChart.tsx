import { PipelineSummary, DealStage } from '../../types';

interface PipelineChartProps {
  summaries: PipelineSummary[];
}

const stageColors: Record<string, { bg: string; bar: string }> = {
  [DealStage.LEAD]: { bg: 'bg-gray-100', bar: 'bg-gray-400' },
  [DealStage.QUALIFIED]: { bg: 'bg-blue-100', bar: 'bg-blue-500' },
  [DealStage.PROPOSAL]: { bg: 'bg-yellow-100', bar: 'bg-yellow-500' },
  [DealStage.NEGOTIATION]: { bg: 'bg-orange-100', bar: 'bg-orange-500' },
  [DealStage.CLOSED_WON]: { bg: 'bg-green-100', bar: 'bg-green-500' },
  [DealStage.CLOSED_LOST]: { bg: 'bg-red-100', bar: 'bg-red-500' },
};

const stageLabels: Record<string, string> = {
  [DealStage.LEAD]: 'Lead',
  [DealStage.QUALIFIED]: 'Qualified',
  [DealStage.PROPOSAL]: 'Proposal',
  [DealStage.NEGOTIATION]: 'Negotiation',
  [DealStage.CLOSED_WON]: 'Closed Won',
  [DealStage.CLOSED_LOST]: 'Closed Lost',
};

export default function PipelineChart({ summaries }: PipelineChartProps) {
  const maxCount = Math.max(...summaries.map((s) => s.count), 1);

  const formatCurrency = (value: number): string => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(value);
  };

  return (
    <div className="card">
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-lg font-semibold text-gray-900">Pipeline Overview</h3>
      </div>

      <div className="space-y-4">
        {summaries.map((summary) => {
          const colors = stageColors[summary.stage] || stageColors[DealStage.LEAD];
          const label = stageLabels[summary.stage] || summary.stage;
          const widthPercent = (summary.count / maxCount) * 100;

          return (
            <div key={summary.stage}>
              <div className="flex items-center justify-between mb-1.5">
                <div className="flex items-center space-x-2">
                  <div className={`w-3 h-3 rounded-full ${colors.bar}`} />
                  <span className="text-sm font-medium text-gray-700">{label}</span>
                </div>
                <div className="text-right">
                  <span className="text-sm font-semibold text-gray-900">{summary.count} deals</span>
                  <span className="text-xs text-gray-500 ml-2">
                    {formatCurrency(summary.totalValue)}
                  </span>
                </div>
              </div>
              <div className="w-full bg-gray-100 rounded-full h-2.5">
                <div
                  className={`${colors.bar} h-2.5 rounded-full transition-all duration-500`}
                  style={{ width: `${Math.max(widthPercent, 2)}%` }}
                />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
