import apiClient from './client';
import { DashboardStats, Deal, Activity } from '../types';

export async function getDashboardStats(): Promise<DashboardStats> {
  const response = await apiClient.get('/dashboard/stats');
  return response.data;
}

export async function getRevenue(): Promise<number> {
  const response = await apiClient.get('/dashboard/revenue');
  return response.data;
}

export async function getTopDeals(limit: number = 10): Promise<Deal[]> {
  const response = await apiClient.get('/dashboard/top-deals', {
    params: { limit },
  });
  return response.data;
}

export async function getRecentActivities(): Promise<Activity[]> {
  const response = await apiClient.get('/activities', {
    params: { page: 0, size: 10, sort: 'createdAt,desc' },
  });
  return response.data.content || [];
}
