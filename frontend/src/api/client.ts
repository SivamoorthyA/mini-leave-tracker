export const API_URL = 'http://localhost:8080'

// The backend sends errors as { status, error, message }; show its message to the user.
export async function errorMessage(response: Response): Promise<string> {
  try {
    const body = await response.json()
    return body.message ?? `Server answered ${response.status}`
  } catch {
    return `Server answered ${response.status}`
  }
}
