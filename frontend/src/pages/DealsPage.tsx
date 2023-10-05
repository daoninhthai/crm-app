import { useState, useEffect, useCallback } from 'react';
import { PlusIcon } from '@heroicons/react/24/outline';
import PipelineBoard from '../components/Deals/PipelineBoard';
import DealForm, { DealFormData } from '../components/Deals/DealForm';
import { Deal, DealStage } from '../types';
import { getPipeline } from '../api/pipeline';
import { createDeal, updateDeal } from '../api/deals';

export default function DealsPage() {
  const [pipeline, setPipeline] = useState<Record<string, Deal[]>>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingDeal, setEditingDeal] = useState<Deal | null>(null);

  const fetchPipeline = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getPipeline();
      setPipeline(data);
    } catch (err) {
      setError('Failed to load pipeline data. Please try again.');
      console.error('Error fetching pipeline:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchPipeline();
  }, [fetchPipeline]);

  const handleDealClick = (deal: Deal) => {
    setEditingDeal(deal);
    setIsFormOpen(true);
  };

  const handleFormSubmit = async (data: DealFormData) => {
    if (editingDeal) {
      await updateDeal(editingDeal.id, data);
    } else {
      await createDeal(data);
    }
    await fetchPipeline();
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
    setEditingDeal(null);
  };

  const totalDeals = Object.values(pipeline).reduce((sum, deals) => sum + deals.length, 0);
  const totalValue = Object.values(pipeline)
    .flat()
    .reduce((sum, deal) => sum + (deal.value || 0), 0);

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Deals Pipeline</h1>
          <p className="text-sm text-gray-500 mt-1">
            {totalDeals} deals &middot; ${totalValue.toLocaleString()} total value
          </p>
        </div>
        <button
          onClick={() => setIsFormOpen(true)}
          className="btn-primary flex items-center"
        >
          <PlusIcon className="h-5 w-5 mr-1.5" />
          New Deal
        </button>
      </div>

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 text-red-700 rounded-lg text-sm">
          {error}
        </div>
      )}

      {loading ? (
        <div className="flex items-center justify-center py-16">
          <svg className="animate-spin h-8 w-8 text-indigo-600" fill="none" viewBox="0 0 24 24">
            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
          </svg>
        </div>
      ) : (
        <PipelineBoard pipeline={pipeline} onDealClick={handleDealClick} />
      )}

      <DealForm
        isOpen={isFormOpen}
        onClose={handleCloseForm}
        onSubmit={handleFormSubmit}
        deal={editingDeal}
      />
    </div>
  );
}
