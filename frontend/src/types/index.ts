export interface Contact {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  companyId: number | null;
  companyName: string | null;
  position: string;
  notes: string;
  status: ContactStatus;
  createdAt: string;
  updatedAt: string;
}

export enum ContactStatus {
  LEAD = 'LEAD',
  PROSPECT = 'PROSPECT',
  CUSTOMER = 'CUSTOMER',
  INACTIVE = 'INACTIVE',
}

export interface Company {
  id: number;
  name: string;
  industry: string;
  website: string;
  phone: string;
  address: string;
  size: CompanySize;
  contactCount: number;
  dealCount: number;
  createdAt: string;
}

export enum CompanySize {
  STARTUP = 'STARTUP',
  SMALL = 'SMALL',
  MEDIUM = 'MEDIUM',
  LARGE = 'LARGE',
  ENTERPRISE = 'ENTERPRISE',
}

export interface Deal {
  id: number;
  title: string;
  value: number;
  stage: DealStage;
  companyId: number | null;
  companyName: string | null;
  contactId: number | null;
  contactName: string | null;
  expectedCloseDate: string;
  probability: number;
  createdAt: string;
  updatedAt: string;
}

export enum DealStage {
  LEAD = 'LEAD',
  QUALIFIED = 'QUALIFIED',
  PROPOSAL = 'PROPOSAL',
  NEGOTIATION = 'NEGOTIATION',
  CLOSED_WON = 'CLOSED_WON',
  CLOSED_LOST = 'CLOSED_LOST',
}

export interface Activity {
  id: number;
  type: ActivityType;
  subject: string;
  description: string;
  contactId: number | null;
  contactName: string | null;
  dealId: number | null;
  dealTitle: string | null;
  dueDate: string;
  completed: boolean;
  createdBy: string;
  createdAt: string;
}

export enum ActivityType {
  CALL = 'CALL',
  EMAIL = 'EMAIL',
  MEETING = 'MEETING',
  TASK = 'TASK',
  NOTE = 'NOTE',
}

export interface User {
  id: number;
  fullName: string;
  email: string;
  role: string;
}

export interface PipelineSummary {
  stage: DealStage;
  count: number;
  totalValue: number;
}

export interface DashboardStats {
  totalContacts: number;
  activeDeals: number;
  revenue: number;
  conversionRate: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface AuthResponse {
  token: string;
  type: string;
  email: string;
  fullName: string;
}
