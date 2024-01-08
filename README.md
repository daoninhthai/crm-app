# CRM Application

A full-stack Customer Relationship Management (CRM) application built with Spring Boot and React. Manage contacts, companies, deals, and activities with a modern, responsive interface featuring a Kanban-style sales pipeline board and analytics dashboard.

## Features

- **Contact Management** - Create, update, search, and organize contacts with status tracking (Lead, Prospect, Customer, Inactive)
- **Company Management** - Manage company profiles with industry, size, and associated contacts/deals
- **Sales Pipeline (Kanban Board)** - Visual drag-and-drop deal pipeline with stages: Lead, Qualified, Proposal, Negotiation, Closed Won, Closed Lost
- **Dashboard & Analytics** - Real-time stats cards, revenue bar chart, pipeline overview, and recent activities
- **Activity Tracking** - Log calls, emails, meetings, tasks, and notes linked to contacts and deals
- **CSV Export** - Export contacts and deals data to CSV files
- **JWT Authentication** - Secure user registration and login with token-based authentication
- **Responsive Design** - Clean, modern UI with Tailwind CSS that works on desktop and mobile

## Tech Stack

| Layer     | Technology                                      |
|-----------|------------------------------------------------|
| Backend   | Java 17, Spring Boot 3.1.5, Spring Security     |
| Database  | PostgreSQL 16, Spring Data JPA, Flyway Migrations |
| Auth      | JWT (JSON Web Tokens)                           |
| Frontend  | React 18, TypeScript 5.3, Vite 5                |
| Styling   | Tailwind CSS 3.3, Headless UI, Heroicons        |
| Routing   | React Router DOM 6                              |
| HTTP      | Axios 1.6                                       |
| DevOps    | Docker, Docker Compose, Nginx                   |

## Project Structure

```
crm-app/
├── src/main/java/com/daoninhthai/crm/
│   ├── CrmApplication.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── ContactController.java
│   │   ├── CompanyController.java
│   │   ├── DealController.java
│   │   ├── ActivityController.java
│   │   ├── PipelineController.java
│   │   ├── DashboardController.java
│   │   └── ExportController.java
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── ContactService.java
│   │   ├── CompanyService.java
│   │   ├── DealService.java
│   │   ├── ActivityService.java
│   │   ├── PipelineService.java
│   │   ├── DashboardService.java
│   │   └── ExportService.java
│   ├── entity/
│   │   ├── Contact.java
│   │   ├── Company.java
│   │   ├── Deal.java
│   │   ├── Activity.java
│   │   └── User.java
│   ├── repository/
│   ├── dto/
│   ├── mapper/
│   ├── security/
│   └── exception/
├── src/main/resources/
│   ├── application.yml
│   └── db/migration/
│       ├── V1__create_companies_table.sql
│       ├── V2__create_contacts_table.sql
│       ├── V3__create_deals_table.sql
│       ├── V4__create_activities_table.sql
│       └── V5__create_users_table.sql
├── frontend/
│   ├── src/
│   │   ├── api/           # API client and service modules
│   │   ├── components/    # Reusable UI components
│   │   │   ├── Layout/    # Sidebar, Header, MainLayout
│   │   │   ├── Contacts/  # ContactTable, ContactForm
│   │   │   ├── Companies/ # CompanyForm
│   │   │   ├── Deals/     # PipelineBoard, DealCard, DealForm
│   │   │   ├── Dashboard/ # StatsCards, Charts
│   │   │   ├── Activities/# ActivityForm
│   │   │   └── common/    # SearchBar, Pagination, ExportButton
│   │   ├── pages/         # Page components
│   │   ├── context/       # AuthContext
│   │   ├── hooks/         # Custom hooks
│   │   └── types/         # TypeScript interfaces
│   ├── Dockerfile
│   └── nginx.conf
├── Dockerfile
├── docker-compose.yml
├── docker-compose.dev.yml
└── pom.xml
```

## Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL 16 (or use Docker)
- Maven 3.8+

### Database Setup

Start PostgreSQL with Docker:

```bash
docker compose -f docker-compose.dev.yml up -d
```

### Backend Setup

```bash
# Run the Spring Boot application
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`.

### Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend will start on `http://localhost:3000` with API proxy to the backend.

### Docker Quickstart

Run the entire stack with a single command:

```bash
docker compose up --build
```

This starts:
- **PostgreSQL** on port `5432`
- **Backend API** on port `8080`
- **Frontend (Nginx)** on port `3000`

## API Endpoints

### Authentication
| Method | Endpoint             | Description          |
|--------|---------------------|----------------------|
| POST   | /api/auth/register   | Register new user    |
| POST   | /api/auth/login      | Login and get JWT    |

### Contacts
| Method | Endpoint                    | Description              |
|--------|-----------------------------|--------------------------|
| GET    | /api/contacts               | List contacts (paginated)|
| GET    | /api/contacts/{id}          | Get contact by ID        |
| POST   | /api/contacts               | Create contact           |
| PUT    | /api/contacts/{id}          | Update contact           |
| DELETE | /api/contacts/{id}          | Delete contact           |
| GET    | /api/contacts/search        | Search contacts          |

### Companies
| Method | Endpoint                    | Description               |
|--------|-----------------------------|---------------------------|
| GET    | /api/companies              | List companies (paginated)|
| GET    | /api/companies/{id}         | Get company by ID         |
| POST   | /api/companies              | Create company            |
| PUT    | /api/companies/{id}         | Update company            |
| DELETE | /api/companies/{id}         | Delete company            |

### Deals
| Method | Endpoint                    | Description             |
|--------|-----------------------------|-------------------------|
| GET    | /api/deals                  | List deals (paginated)  |
| GET    | /api/deals/{id}             | Get deal by ID          |
| POST   | /api/deals                  | Create deal             |
| PUT    | /api/deals/{id}             | Update deal             |
| DELETE | /api/deals/{id}             | Delete deal             |

### Pipeline
| Method | Endpoint                         | Description            |
|--------|----------------------------------|------------------------|
| GET    | /api/pipeline                    | Get deals by stage     |
| PUT    | /api/pipeline/deals/{id}/move    | Move deal to stage     |
| GET    | /api/pipeline/summary            | Pipeline summary stats |

### Dashboard
| Method | Endpoint                    | Description            |
|--------|-----------------------------|------------------------|
| GET    | /api/dashboard/stats        | Dashboard statistics   |
| GET    | /api/dashboard/revenue      | Total revenue          |
| GET    | /api/dashboard/top-deals    | Top deals by value     |

### Activities
| Method | Endpoint                              | Description                |
|--------|---------------------------------------|----------------------------|
| GET    | /api/activities                       | List activities (paginated)|
| POST   | /api/activities                       | Create activity            |
| PUT    | /api/activities/{id}                  | Update activity            |
| PATCH  | /api/activities/{id}/complete         | Mark as complete           |
| GET    | /api/contacts/{id}/activities         | Activities by contact      |
| GET    | /api/deals/{id}/activities            | Activities by deal         |

### Export
| Method | Endpoint                    | Description            |
|--------|-----------------------------|------------------------|
| GET    | /api/export/contacts        | Export contacts as CSV |
| GET    | /api/export/deals           | Export deals as CSV    |

## Screenshots

*Screenshots will be added here.*

## Author

**daoninhthai** - [GitHub](https://github.com/daoninhthai)

## License

This project is licensed under the MIT License.
