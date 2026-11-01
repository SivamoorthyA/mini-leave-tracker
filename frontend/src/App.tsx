import { useState } from 'react'

type Employee = {
  id: string
  name: string
  email: string
}

function App() {
  const [employees, setEmployees] = useState<Employee[]>([])
  const [error, setError] = useState('')

  async function loadEmployees() {
    setError('')
    try {
      const response = await fetch('http://localhost:8080/api/employees')
      setEmployees(await response.json())
    } catch (err) {
      setError(String(err))
    }
  }

  return (
    <main>
      <h1>Mini Leave Tracker</h1>
      <button type="button" onClick={loadEmployees}>
        Load employees
      </button>
      {error && <p style={{ color: 'crimson' }}>Error: {error}</p>}
      <ul>
        {employees.map((employee) => (
          <li key={employee.id}>
            {employee.name} ({employee.email})
          </li>
        ))}
      </ul>
    </main>
  )
}

export default App
