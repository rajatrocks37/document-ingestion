# Document Ingestion and Q&A System

This project is a microservices-based document ingestion platform with a keyword-based Q&A interface. It includes an Angular 17 frontend, Spring Boot microservices (Auth Server, Document Service), PostgreSQL databases, and Dockerized deployment.

---

## 🚀 Features

- ✅ JWT-based user authentication with role-based access control
- ✅ User registration, login, and management
- ✅ Upload and ingest documents
- ✅ Keyword-based Q&A using full-text search
- ✅ Document management: filter, search, sort, paginate
- ✅ Drag & drop file upload with type validation
- ✅ Role-based menu display in sidebar
- ✅ Fully Dockerized with Angular frontend

---

## 🧰 Technologies Used

- **Frontend:** Angular 17, Tailwind CSS
- **Backend:** Spring Boot 2, Java 17 & 21
- **Authentication:** Spring Security, Spring Authorization Server, JWT
- **Database:** PostgreSQL
- **Search:** Full-text search with `to_tsvector`/`plainto_tsquery`
- **Containerization:** Docker, Docker Compose
- **Build Tools:** Maven
  
---

## 🛠️ Getting Started

### 📦 Prerequisites

- Docker & Docker Compose
- Java 17+, Java 21+ (for local build, if needed)
- Node.js + Angular CLI (for local frontend dev, if needed)

### 🐳 Running with Docker Compose

```bash
docker-compose up --build


This will:

Build and run the Angular app

Build and run both Spring Boot microservices

Set up the PostgreSQL database



🔑 Default Users
These are auto-inserted on startup via SQL scripts:

Username	Password	Role
admin      admin123	ADMIN

