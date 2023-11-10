import apiClient from './client';
import { Company, PageResponse } from '../types';

export interface CreateCompanyData {
  name: string;
  industry?: string;
  website?: string;
  phone?: string;
  address?: string;
  size?: string;
}

export async function getCompanies(
  page: number = 0,
  size: number = 20,
  sort: string = 'createdAt,desc'
): Promise<PageResponse<Company>> {
  const response = await apiClient.get('/companies', {
    params: { page, size, sort },
  });
  return response.data;
}

export async function searchCompanies(
  name: string,
  page: number = 0,
  size: number = 20
): Promise<PageResponse<Company>> {
  const response = await apiClient.get('/companies/search', {
    params: { name, page, size },
  });
  return response.data;
}

export async function getCompany(id: number): Promise<Company> {
  const response = await apiClient.get(`/companies/${id}`);
  return response.data;
}

export async function createCompany(data: CreateCompanyData): Promise<Company> {
  const response = await apiClient.post('/companies', data);
  return response.data;
}

export async function updateCompany(id: number, data: CreateCompanyData): Promise<Company> {
  const response = await apiClient.put(`/companies/${id}`, data);
  return response.data;
}

export async function deleteCompany(id: number): Promise<void> {
  await apiClient.delete(`/companies/${id}`);
}
