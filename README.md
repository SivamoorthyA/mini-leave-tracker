# Mini Leave Tracker (practice project)

A small copy of the real AICERA HRMS "Leave" module. Purpose: **learn the same tools and the same teamwork steps** in a size we can finish.

This project is separate from the real HRMS. Nothing here can break it.

## What the app does

- Employees list (name, email)
- Apply for leave (type, start date, end date, reason)
- Each leave has a status: Pending, Approved or Rejected
- A manager can approve or reject

## Tools (same as the real project)

| Part | Tool | Folder |
|---|---|---|
| Database | PostgreSQL, running in Docker | docker-compose.yml |
| Backend (server) | Java 21 + Spring Boot 3 | backend/ |
| Frontend (screens) | React + TypeScript + Vite | frontend/ |
| Teamwork | Git + GitHub | whole project |

## How the parts talk

```
Browser (React screens)  ->  Backend (Spring Boot API)  ->  Database (PostgreSQL)
      what you see              the rules + the "brain"         where data is saved
```

## Build order: backend first, then frontend

We start with the database and backend, then add the screens. Why:
- The screens need data from the backend. Without it they show nothing.
- We can test the backend alone (browser or Postman) before any screen exists.
- It matches the real plan: Sprint 1 is the backend foundation.

(Some teams start from the frontend with fake data. That is also valid. See the chat explanation.)

## Plan

Same working style as the real team: small tickets, one branch per ticket, a pull request for each, and a review.

### Sprint 0: Setup (learn the tools)

- [x] 1. Docker Desktop is running (Engine running, green)
- [x] 2. `java -version` shows 21
- [x] 3. `node --version` works, `git --version` works
- [x] 4. Create a GitHub account (use sivamoorthy477@gmail.com)
- [x] 5. `docker run hello-world` works (our first container)
- [x] 6. Make this folder a Git repository (`git init`)
- [x] 7. Create an empty repository on GitHub and push this README
- [x] 8. Practice pull request: change the README on a branch, open a PR, merge it

### Sprint 1: Foundation (backend)

- [X] 9. Create the Spring Boot project in `backend/`
- [X] 10. Run PostgreSQL in Docker (`docker-compose.yml`) and connect the backend to it
- [X] 11. First migration: create the `employees` table
- [X] 12. `GET /api/employees` returns employees from the database
----------------------------------------------------------
### Sprint 2: First module (leave)

- [ ] 13. Write the API contract first: what each request accepts and returns
- [ ] 14. `leave_requests` table, then `POST /api/leaves` (apply for leave)
- [ ] 15. `GET /api/leaves` (list leaves)
- [ ] 16. React: set up the frontend, then the employee list screen
- [ ] 17. React: apply-leave form and leave list screen
- [ ] 18. Backend tests, then frontend tests

### Sprint 3: Finish

- [ ] 19. Approve / reject (status changes)
- [ ] 20. Show errors nicely (bad dates, missing fields)
- [ ] 21. Automatic checks on GitHub (CI) that build and test
- [ ] 22. Demo: show the finished app running

## Steps for every ticket

1. Pick a ticket. Write it down.
2. Agree what the API accepts and returns (for API tickets).
3. Make a branch: `git checkout -b feature/9-create-backend`
4. Ask the AI for a plan before any code. Agree to it.
5. Build in layers: database, backend, screen, tests.
6. Ask the AI to explain every file it changed. If you can't say it in one sentence, ask again.
7. Run it and test it by hand.
8. Commit with a clear message, e.g. `feat(leave): save leave requests`
9. Push, open a pull request on GitHub.
10. Review and merge (Squash and merge). Delete the branch. Tick the box above.

## Rules for learning

- You type the commands. The AI explains first and asks before it creates or installs anything.
- Never commit passwords to Git.


-----------