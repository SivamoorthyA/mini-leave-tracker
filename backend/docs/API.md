# Leave API contract

Agreed before any code is written. Frontend and backend both follow this file.
If the contract must change, change this file first, in its own pull request.

Base URL (local): `http://localhost:8080`
All requests and responses use JSON. Dates are `YYYY-MM-DD`. Ids are UUIDs.

## Data shapes

### LeaveRequest (response)

| Field | Type | Notes |
|---|---|---|
| id | uuid | Created by the server |
| employeeId | uuid | Must be an existing employee |
| type | string | `ANNUAL`, `SICK` or `CASUAL` |
| startDate | date | First day of leave |
| endDate | date | Last day of leave |
| reason | string or null | Optional, up to 200 characters |
| status | string | `PENDING`, `APPROVED` or `REJECTED` |

## Endpoints

### POST /api/leaves: apply for leave

Request body:

```json
{
  "employeeId": "1906bb71-e713-452b-b30b-67b7dde3bb59",
  "type": "ANNUAL",
  "startDate": "2026-11-02",
  "endDate": "2026-11-04",
  "reason": "family function"
}
```

Success: `201 Created`

```json
{
  "id": "0c9d3b5e-5a8f-4b1e-9d2a-1f6a7e3c2b10",
  "employeeId": "1906bb71-e713-452b-b30b-67b7dde3bb59",
  "type": "ANNUAL",
  "startDate": "2026-11-02",
  "endDate": "2026-11-04",
  "reason": "family function",
  "status": "PENDING"
}
```

Errors:

| Status | When |
|---|---|
| 400 Bad Request | A required field is missing, `type` is not one of the allowed values, `endDate` is before `startDate`, or `reason` is longer than 200 characters |
| 404 Not Found | `employeeId` does not match any employee |

### GET /api/leaves: list leave requests

Success: `200 OK`. Returns a list of LeaveRequest. The list is empty (`[]`) when there are none.

```json
[
  {
    "id": "0c9d3b5e-5a8f-4b1e-9d2a-1f6a7e3c2b10",
    "employeeId": "1906bb71-e713-452b-b30b-67b7dde3bb59",
    "type": "ANNUAL",
    "startDate": "2026-11-02",
    "endDate": "2026-11-04",
    "reason": "family function",
    "status": "PENDING"
  }
]
```

### PATCH /api/leaves/{id}/status: approve or reject

Request body:

```json
{ "status": "APPROVED" }
```

`status` must be `APPROVED` or `REJECTED`.

Success: `200 OK`. Returns the updated LeaveRequest with the new status.

Errors:

| Status | When |
|---|---|
| 400 Bad Request | `status` is missing, is not a known value, or is `PENDING`; or `{id}` is not a valid UUID |
| 404 Not Found | No leave request has this id |
| 409 Conflict | The leave was already approved or rejected. A decision cannot be changed. |

## Error response shape

Every error uses the same body, so the frontend can show it the same way:

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "endDate must not be before startDate"
}
```

## Rules

- A new leave request always starts as `PENDING`. The client cannot set the status.
- `endDate` must be on or after `startDate`. A one-day leave has the same start and end date.
- Only a `PENDING` leave can be approved or rejected. Once decided, the status is final.
- The client may only set `APPROVED` or `REJECTED`. It can never set a leave back to `PENDING`.
