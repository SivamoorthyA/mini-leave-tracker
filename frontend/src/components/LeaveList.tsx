import { useEffect, useState } from 'react'
import { fetchEmployees } from '../api/employees'
import { fetchLeaves } from '../api/leaves'
import type { Employee, Leave } from '../types'

export function LeaveList() {
  const [leaves, setLeaves] = useState<Leave[]>([])
  const [employees, setEmployees] = useState<Employee[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([fetchLeaves(), fetchEmployees()])
      .then(([leaveList, employeeList]) => {
        setLeaves(leaveList)
        setEmployees(employeeList)
      })
      .catch((err) => setError(String(err)))
      .finally(() => setLoading(false))
  }, [])

  function employeeName(employeeId: string) {
    return employees.find((employee) => employee.id === employeeId)?.name ?? employeeId
  }

  if (loading) return <p>Loading...</p>
  if (error) return <p style={{ color: 'crimson' }}>Error: {error}</p>
  if (leaves.length === 0) return <p>No leave requests yet.</p>

  return (
    <table>
      <thead>
        <tr>
          <th>Employee</th>
          <th>Type</th>
          <th>From</th>
          <th>To</th>
          <th>Status</th>
          <th>Reason</th>
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
          </tr>
        ))}
      </tbody>
    </table>
  )
}
