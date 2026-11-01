import { useEffect, useState } from 'react'
import { fetchEmployees } from '../api/employees'
import { fetchLeaves, updateLeaveStatus } from '../api/leaves'
import type { Employee, Leave, LeaveStatus } from '../types'

type LeaveListProps = {
  leaveVersion: number
}

export function LeaveList({ leaveVersion }: LeaveListProps) {
  const [leaves, setLeaves] = useState<Leave[]>([])
  const [employees, setEmployees] = useState<Employee[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [actionError, setActionError] = useState('')
  const [busyId, setBusyId] = useState('')
  const [decisions, setDecisions] = useState(0)

  // Runs on first load, and again when a leave is saved (leaveVersion) or decided (decisions).
  useEffect(() => {
    Promise.all([fetchLeaves(), fetchEmployees()])
      .then(([leaveList, employeeList]) => {
        setError('')
        setLeaves(leaveList)
        setEmployees(employeeList)
      })
      .catch((err) => setError(String(err)))
      .finally(() => setLoading(false))
  }, [leaveVersion, decisions])

  function employeeName(employeeId: string) {
    return employees.find((employee) => employee.id === employeeId)?.name ?? employeeId
  }

  async function decide(id: string, status: LeaveStatus) {
    setActionError('')
    setBusyId(id)
    try {
      await updateLeaveStatus(id, status)
    } catch (err) {
      setActionError(err instanceof Error ? err.message : String(err))
    } finally {
      setBusyId('')
      // Reload even after an error, so the table shows what the server really has.
      setDecisions((count) => count + 1)
    }
  }

  if (loading) return <p>Loading...</p>
  if (error) return <p style={{ color: 'crimson' }}>Error: {error}</p>
  if (leaves.length === 0) return <p>No leave requests yet.</p>

  return (
    <>
      {actionError && <p style={{ color: 'crimson' }}>Error: {actionError}</p>}
      <table>
        <thead>
          <tr>
            <th>Employee</th>
            <th>Type</th>
            <th>From</th>
            <th>To</th>
            <th>Status</th>
            <th>Reason</th>
            <th>Decision</th>
          </tr>
        </thead>
        <tbody>
          {leaves.map((leave) => (
            <tr key={leave.id}>
              <td>{employeeName(leave.employeeId)}</td>
              <td>{leave.type}</td>
              <td>{leave.startDate}</td>
              <td>{leave.endDate}</td>
              <td>{leave.status}</td>
              <td>{leave.reason || '-'}</td>
              <td>
                {leave.status === 'PENDING' && (
                  <>
                    <button
                      type="button"
                      disabled={busyId === leave.id}
                      onClick={() => decide(leave.id, 'APPROVED')}
                    >
                      Approve
                    </button>{' '}
                    <button
                      type="button"
                      disabled={busyId === leave.id}
                      onClick={() => decide(leave.id, 'REJECTED')}
                    >
                      Reject
                    </button>
                  </>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}
