import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { fetchEmployees } from '../api/employees'
import { createLeave } from '../api/leaves'
import { LEAVE_TYPES } from '../types'
import type { Employee, NewLeave } from '../types'

const emptyForm: NewLeave = {
  employeeId: '',
  type: 'ANNUAL',
  startDate: '',
  endDate: '',
  reason: '',
}

type LeaveFormProps = {
  onCreated: () => void
}

export function LeaveForm({ onCreated }: LeaveFormProps) {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [form, setForm] = useState<NewLeave>(emptyForm)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  useEffect(() => {
    fetchEmployees()
      .then((list) => {
        setEmployees(list)
        setForm((current) => ({ ...current, employeeId: list[0]?.id ?? '' }))
      })
      .catch((err) => setError(String(err)))
  }, [])

  function update<K extends keyof NewLeave>(field: K, value: NewLeave[K]) {
    setForm((current) => ({ ...current, [field]: value }))
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault()
    setError('')
    setSuccess('')
    setSubmitting(true)
    try {
      const saved = await createLeave(form)
      setSuccess(`Leave request saved with status ${saved.status}`)
      setForm((current) => ({ ...emptyForm, employeeId: current.employeeId }))
      onCreated()
    } catch (err) {
      setError(err instanceof Error ? err.message : String(err))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Apply for leave</h2>

      <p>
        <label>
          Employee{' '}
          <select
            value={form.employeeId}
            onChange={(event) => update('employeeId', event.target.value)}
            required
          >
            {employees.map((employee) => (
              <option key={employee.id} value={employee.id}>
                {employee.name}
              </option>
            ))}
          </select>
        </label>
      </p>

      <p>
        <label>
          Type{' '}
          <select
            value={form.type}
            onChange={(event) => update('type', event.target.value as NewLeave['type'])}
          >
            {LEAVE_TYPES.map((type) => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </label>
      </p>

      <p>
        <label>
          Start date{' '}
          <input
            type="date"
            value={form.startDate}
            onChange={(event) => update('startDate', event.target.value)}
            required
          />
        </label>
      </p>

      <p>
        <label>
          End date{' '}
          <input
            type="date"
            value={form.endDate}
            onChange={(event) => update('endDate', event.target.value)}
            required
          />
        </label>
      </p>

      <p>
        <label>
          Reason (optional){' '}
          <input
            type="text"
            maxLength={200}
            value={form.reason}
            onChange={(event) => update('reason', event.target.value)}
          />
        </label>
      </p>

      <button type="submit" disabled={submitting}>
        {submitting ? 'Saving...' : 'Apply'}
      </button>

      {error && <p style={{ color: 'crimson' }}>Error: {error}</p>}
      {success && <p style={{ color: 'seagreen' }}>{success}</p>}
    </form>
  )
}
