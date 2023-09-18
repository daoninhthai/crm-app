import { useState, useEffect, useCallback } from 'react';
import { Contact, PageResponse } from '../types';
import {
  getContacts,
  searchContacts,
  createContact,
  updateContact,
  deleteContact,
  CreateContactData,
  UpdateContactData,
} from '../api/contacts';

interface UseContactsReturn {
  contacts: Contact[];
  loading: boolean;
  error: string | null;
  totalPages: number;
  totalElements: number;
  currentPage: number;
  fetchContacts: (page?: number) => Promise<void>;
  searchContactsList: (keyword: string, page?: number) => Promise<void>;
  addContact: (data: CreateContactData) => Promise<Contact>;
  editContact: (id: number, data: UpdateContactData) => Promise<Contact>;
  removeContact: (id: number) => Promise<void>;
  setCurrentPage: (page: number) => void;
}

export function useContacts(pageSize: number = 20): UseContactsReturn {
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);

  const fetchContacts = useCallback(async (page: number = 0) => {
    setLoading(true);
    setError(null);
    try {
      const data: PageResponse<Contact> = await getContacts(page, pageSize);
      setContacts(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(data.number);
    } catch (err) {
      setError('Failed to fetch contacts. Please try again.');
      console.error('Error fetching contacts:', err);
    } finally {
      setLoading(false);
    }
  }, [pageSize]);

  const searchContactsList = useCallback(async (keyword: string, page: number = 0) => {
    setLoading(true);
    setError(null);
    try {
      const data: PageResponse<Contact> = await searchContacts(keyword, page, pageSize);
      setContacts(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(data.number);
    } catch (err) {
      setError('Failed to search contacts. Please try again.');
      console.error('Error searching contacts:', err);
    } finally {
      setLoading(false);
    }
  }, [pageSize]);

  const addContact = async (data: CreateContactData): Promise<Contact> => {
    const newContact = await createContact(data);
    await fetchContacts(currentPage);
    return newContact;
  };

  const editContact = async (id: number, data: UpdateContactData): Promise<Contact> => {
    const updated = await updateContact(id, data);
    await fetchContacts(currentPage);
    return updated;
  };

  const removeContact = async (id: number): Promise<void> => {
    await deleteContact(id);
    await fetchContacts(currentPage);
  };

  useEffect(() => {
    fetchContacts();
  }, [fetchContacts]);

  return {
    contacts,
    loading,
    error,
    totalPages,
    totalElements,
    currentPage,
    fetchContacts,
    searchContactsList,
    addContact,
    editContact,
    removeContact,
    setCurrentPage,
  };
}
