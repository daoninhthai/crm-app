import apiClient from './client';
import { Contact, PageResponse } from '../types';

export interface CreateContactData {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  companyId?: number | null;
  position?: string;
  notes?: string;
  status?: string;
}

export interface UpdateContactData {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  companyId?: number | null;
  position?: string;
  notes?: string;
  status?: string;
}

export async function getContacts(
  page: number = 0,
  size: number = 20,
  sort: string = 'createdAt,desc'
): Promise<PageResponse<Contact>> {
  const response = await apiClient.get('/contacts', {
    params: { page, size, sort },
  });
  return response.data;
}

export async function searchContacts(
  keyword: string,
  page: number = 0,
  size: number = 20
): Promise<PageResponse<Contact>> {
  const response = await apiClient.get('/contacts/search', {
    params: { keyword, page, size },
  });
  return response.data;
}

export async function getContact(id: number): Promise<Contact> {
  const response = await apiClient.get(`/contacts/${id}`);
  return response.data;
}

export async function createContact(data: CreateContactData): Promise<Contact> {
  const response = await apiClient.post('/contacts', data);
  return response.data;
}

export async function updateContact(id: number, data: UpdateContactData): Promise<Contact> {
  const response = await apiClient.put(`/contacts/${id}`, data);
  return response.data;
}

export async function deleteContact(id: number): Promise<void> {
  await apiClient.delete(`/contacts/${id}`);
}

export async function getContactsByCompany(companyId: number): Promise<Contact[]> {
  const response = await apiClient.get(`/contacts/company/${companyId}`);
  return response.data;
}
