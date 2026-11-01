import type { Employee } from '../types'
import { API_URL, errorMessage } from './client'

export async function fetchEmployees(): Promise<Employee[]> {
  const response = await fetch(`${API_URL}/api/employees`)
  if (!response.ok) {
    throw new Error(await errorMessage(response))
  }
  return response.json()
}
