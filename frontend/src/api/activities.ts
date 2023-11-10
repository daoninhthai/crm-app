import apiClient from './client';
import { Activity, PageResponse } from '../types';

export interface CreateActivityData {
  type: string;
  subject: string;
  description?: string;
  contactId?: number | null;
  dealId?: number | null;
  dueDate?: string;
}

export async function getActivities(
  page: number = 0,
  size: number = 20,
  sort: string = 'createdAt,desc'
): Promise<PageResponse<Activity>> {
  const response = await apiClient.get('/activities', {
    params: { page, size, sort },
  });
  return response.data;
}

export async function getActivity(id: number): Promise<Activity> {
  const response = await apiClient.get(`/activities/${id}`);
  return response.data;
}

export async function createActivity(data: CreateActivityData): Promise<Activity> {
  const response = await apiClient.post('/activities', data);
  return response.data;
}

export async function updateActivity(id: number, data: CreateActivityData): Promise<Activity> {
  const response = await apiClient.put(`/activities/${id}`, data);
  return response.data;
}

export async function completeActivity(id: number): Promise<Activity> {
  const response = await apiClient.patch(`/activities/${id}/complete`);
  return response.data;
}

export async function deleteActivity(id: number): Promise<void> {
  await apiClient.delete(`/activities/${id}`);
}

export async function getUpcomingActivities(limit: number = 10): Promise<Activity[]> {
  const response = await apiClient.get('/activities/upcoming', {
    params: { limit },
  });
  return response.data;
}

export async function getOverdueActivities(): Promise<Activity[]> {
  const response = await apiClient.get('/activities/overdue');
  return response.data;
}

export async function getActivitiesByContact(contactId: number): Promise<Activity[]> {
  const response = await apiClient.get(`/contacts/${contactId}/activities`);
  return response.data;
}

export async function getActivitiesByDeal(dealId: number): Promise<Activity[]> {
  const response = await apiClient.get(`/deals/${dealId}/activities`);
  return response.data;
}
