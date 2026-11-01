import type { Leave, NewLeave } from '../types'
import { API_URL, errorMessage } from './client'

export async function fetchLeaves(): Promise<Leave[]> {
  const response = await fetch(`${API_URL}/api/leaves`)
  if (!response.ok) {
    throw new Error(await errorMessage(response))
  }
  return response.json()
}

export async function createLeave(newLeave: NewLeave): Promise<Leave> {
  const response = await fetch(`${API_URL}/api/leaves`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(newLeave),
  })
  if (!response.ok) {
    throw new Error(await errorMessage(response))
  }
  return response.json()
}
