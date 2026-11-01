import type { Employee } from '../types'

const API_URL = 'http://localhost:8080'

export async function fetchEmployees(): Promise<Employee[]> {
  const response = await fetch(`${API_URL}/api/employees`)
  if (!response.ok) {
    throw new Error(`Server answered ${response.status}`)
  }
  return response.json()
}
