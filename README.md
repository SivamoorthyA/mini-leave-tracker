# Mini Leave Tracker (practice project)

A small copy of the real AICERA HRMS "Leave" module. Purpose: **learn the same tools and the same teamwork steps** in a size we can finish.

This project is separate from the real HRMS. Nothing here can break it.

## What the app does

- Employees list (name, email)
- Apply for leave (type, start date, end date, reason)
- Leave list with status: Pending, Approved or Rejected
- Approve or reject a pending leave (a decision is final)

## Tools

| Part | Tool | Folder |
|---|---|---|
| Database | PostgreSQL 16, running in Docker | `docker-compose.yml` |
| Database changes | Flyway migrations | `backend/src/main/resources/db/migration/` |
| Backend (server) | Java 21 + Spring Boot 4.0.9 | `backend/` |
| Frontend (screens) | React 19 + TypeScript + Vite | `frontend/` |
| Automatic checks | GitHub Actions | `.github/workflows/ci.yml` |
| Teamwork | Git + GitHub, pull requests | whole project |

## How the parts talk

```
Browser (React, port 5173)  ->  Backend (Spring Boot API, port 8080)  ->  PostgreSQL (Docker, port 5432)
      what you see                  the rules + the "brain"                   where data is saved
```

Backend layers: **Controller** (the URL) -> **Service** (the rules) -> **Repository** (the database).

## Before you start (install once)

| Tool | Version |
|---|---|
| Git | any recent |
| Java JDK | 21 |
| Node.js | 24 (the real HRMS plan says 22, both work) |
| Docker Desktop | running (green "Engine running") |

## Run it on your computer

Open three terminals, one for each step.

1. **Database** (project root):
   ```
   docker compose up -d
   ```
2. **Backend** (port 8080). Flyway creates the tables on first start:
   ```
   cd backend
   .\mvnw.cmd spring-boot:run
   ```
3. **Frontend** (port 5173). Run `npm install` only the first time:
   ```
   cd frontend
   npm install
   npm run dev
   ```
4. Open `http://localhost:5173`.

Stop the backend and frontend with Ctrl+C. Stop the database with `docker compose down` (your data stays). `docker compose down -v` also deletes the data.

## Run the checks

```
cd backend
.\mvnw.cmd test            # needs the database running

cd frontend
npm run lint
npm run build              # also runs the TypeScript type check
```

GitHub runs the same checks on every pull request (see `.github/workflows/ci.yml`).

## API

The contract is written first, before the code: [backend/docs/API.md](backend/docs/API.md).

| Endpoint | What it does |
|---|---|
| `GET /api/employees` | List employees |
| `POST /api/leaves` | Apply for leave (starts as PENDING) |
| `GET /api/leaves` | List leave requests |
| `PATCH /api/leaves/{id}/status` | Approve or reject a pending leave |

## Database changes (Flyway)

- Every change is a new numbered file: `V1__...sql`, `V2__...sql`, and so on.
- **Never edit a migration that has already run.** Add a new one. Flyway stores a checksum and refuses to start if an old file changed.
- Two people must not use the same version number. Check `main` for the highest number before you add one.

## Troubleshooting

| Problem | Fix |
|---|---|
| `Cannot connect to the Docker daemon` | Open Docker Desktop and wait for "Engine running" |
| Backend says it cannot connect to the database | Run `docker compose up -d` and check `docker ps` |
| `port 5432 is already in use` | Another PostgreSQL is running on your PC. Stop it, or change the port in `docker-compose.yml` and `application.properties` |
| `invalid value for parameter "TimeZone": "Asia/Calcutta"` | Already handled in `LeaveTrackerApplication.java` (the app uses UTC) |
| Browser says "blocked by CORS policy" | The frontend must run on `http://localhost:5173`, the address allowed in `application.properties` |

The database password in `docker-compose.yml` and `application.properties` is for local practice only. Never commit real passwords.

## What was built (each line is a merged pull request)

### Sprint 0: Setup
- [x] Docker, Java 21, Node and Git installed and working
- [x] GitHub account and repository (PR #1 was the practice pull request)

### Sprint 1: Foundation (backend)
- [x] Spring Boot project in `backend/` (PR #2)
- [x] PostgreSQL in Docker, backend connected (PR #3)
- [x] First migration: `employees` table (PR #4)
- [x] `GET /api/employees` (PR #5)

### Sprint 2: Leave module
- [x] Leave API contract (PR #7)
- [x] `leave_requests` table (PR #8)
- [x] `POST` and `GET /api/leaves` with validation and standard errors (PR #9)
- [x] Backend tests (PR #10)
- [x] React project with Vite (PR #11)
- [x] CORS, so the browser may call the API (PR #12)
- [x] Employees screen (PR #13)
- [x] Apply-leave form (PR #14)
- [x] Leave list (PR #15), refreshing after a new leave (PR #16)

### Sprint 3: Finish
- [x] Approve and reject: backend and buttons (PR #17)
- [x] Automatic checks on GitHub, CI (PR #18)
- [x] README brought up to date (this pull request)
- [ ] Frontend tests (Vitest and React Testing Library)
- [ ] Protect `main` on GitHub: require a pull request and green checks
- [ ] Styling (plain CSS first, a UI library later)
- [ ] Demo: show the finished app running

### Not built on purpose
Login and roles, payroll, leave balances, file uploads. The real HRMS plan covers these.

## Steps for every ticket

1. Pick a ticket. Write it down.
2. Agree what the API accepts and returns (for API tickets). Update `backend/docs/API.md` first.
3. Make a branch: `git switch -c feature/28-short-name`
4. Ask the AI for a plan before any code. Agree to it.
5. Build in layers: database, backend, screen, tests.
6. Ask the AI to explain every file it changed. If you can't say it in one sentence, ask again.
7. Run it and test it by hand. Run the checks above.
8. Commit with a clear message, e.g. `feat(leave): save leave requests`. Add files by folder (`git add backend`), not `git add .`, so one pull request holds one kind of change.
9. Push, open a pull request on GitHub. Wait for the green checks.
10. Review and merge (Squash and merge). Delete the branch. Then `git switch main` and `git pull`.

## Rules for learning

- You type the commands. The AI explains first and asks before it creates or installs anything.
- Never commit passwords to Git.
