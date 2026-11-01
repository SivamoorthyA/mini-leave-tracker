export type Employee = {
  id: string
  name: string
  email: string
}

export const LEAVE_TYPES = ['ANNUAL', 'SICK', 'CASUAL'] as const

export type LeaveType = (typeof LEAVE_TYPES)[number]

export type LeaveStatus = 'PENDING' | 'APPROVED' | 'REJECTED'

export type NewLeave = {
  employeeId: string
  type: LeaveType
  startDate: string
  endDate: string
  reason: string
}

export type Leave = NewLeave & {
  id: string
  status: LeaveStatus
}
