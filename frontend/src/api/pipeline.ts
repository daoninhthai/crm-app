import apiClient from './client';
import { Deal, DealStage, PipelineSummary } from '../types';

export async function getPipeline(): Promise<Record<DealStage, Deal[]>> {
  const response = await apiClient.get('/pipeline');
  return response.data;
}

export async function moveDeal(dealId: number, targetStage: DealStage): Promise<Deal> {
  const response = await apiClient.put(`/pipeline/deals/${dealId}/move`, {
    targetStage,
  });
  return response.data;
}

export async function getPipelineSummary(): Promise<PipelineSummary[]> {
  const response = await apiClient.get('/pipeline/summary');
  return response.data;
}
