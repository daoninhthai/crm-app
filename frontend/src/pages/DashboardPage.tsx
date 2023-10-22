import { useState, useEffect } from 'react';
import StatsCards from '../components/Dashboard/StatsCards';
import RevenueChart from '../components/Dashboard/RevenueChart';
import PipelineChart from '../components/Dashboard/PipelineChart';
import RecentActivities from '../components/Dashboard/RecentActivities';
import { DashboardStats, PipelineSummary, Activity } from '../types';
import { getDashboardStats, getRecentActivities } from '../api/dashboard';
import { getPipelineSummary } from '../api/pipeline';

export default function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats>({
    totalContacts: 0,
    activeDeals: 0,
    revenue: 0,
    conversionRate: 0,
  });
  const [pipelineSummaries, setPipelineSummaries] = useState<PipelineSummary[]>([]);
  const [recentActivities, setRecentActivities] = useState<Activity[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      const [statsData, pipelineData, activitiesData] = await Promise.all([
        getDashboardStats().catch(() => ({
          totalContacts: 0,
          activeDeals: 0,
          revenue: 0,
          conversionRate: 0,
        })),
        getPipelineSummary().catch(() => []),
        getRecentActivities().catch(() => []),
      ]);

      setStats(statsData);
      setPipelineSummaries(pipelineData);
      setRecentActivities(activitiesData);
    } catch (err) {
      console.error('Error loading dashboard data:', err);
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

  return (
    <div className="p-6 space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-sm text-gray-500 mt-1">Welcome back! Here's what's happening with your CRM.</p>
      </div>

      <StatsCards stats={stats} />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <RevenueChart />
        <PipelineChart summaries={pipelineSummaries} />
      </div>

      <RecentActivities activities={recentActivities} />
    </div>
  );
}
