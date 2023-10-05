import apiClient from './client';
import { Deal, PageResponse } from '../types';

export interface CreateDealData {
  title: string;
  value: number;
  stage?: string;
  companyId?: number | null;
  contactId?: number | null;
  expectedCloseDate?: string;
  probability?: number;
}

export interface UpdateDealData {
  title?: string;
  value?: number;
  stage?: string;
  companyId?: number | null;
  contactId?: number | null;
  expectedCloseDate?: string;
  probability?: number;
}

export async function getDeals(
  page: number = 0,
  size: number = 20,
  sort: string = 'createdAt,desc'
): Promise<PageResponse<Deal>> {
  const response = await apiClient.get('/deals', {
    params: { page, size, sort },
  });
  return response.data;
}

export async function getDeal(id: number): Promise<Deal> {
  const response = await apiClient.get(`/deals/${id}`);
  return response.data;
}

export async function createDeal(data: CreateDealData): Promise<Deal> {
  const response = await apiClient.post('/deals', data);
  return response.data;
}

export async function updateDeal(id: number, data: UpdateDealData): Promise<Deal> {
  const response = await apiClient.put(`/deals/${id}`, data);
  return response.data;
}

export async function deleteDeal(id: number): Promise<void> {
  await apiClient.delete(`/deals/${id}`);
}

export async function getDealsByStage(stage: string): Promise<Deal[]> {
  const response = await apiClient.get(`/deals/stage/${stage}`);
  return response.data;
}

export async function getDealsByCompany(companyId: number): Promise<Deal[]> {
  const response = await apiClient.get(`/deals/company/${companyId}`);
  return response.data;
}
