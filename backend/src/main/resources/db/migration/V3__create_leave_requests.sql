CREATE TABLE leave_requests (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_id UUID         NOT NULL REFERENCES employees (id),
    type        VARCHAR(20)  NOT NULL CHECK (type IN ('ANNUAL', 'SICK', 'CASUAL')),
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    reason      VARCHAR(200),
    status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CHECK (end_date >= start_date)
);
