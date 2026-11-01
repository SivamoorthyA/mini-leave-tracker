import { EmployeeList } from './components/EmployeeList'
import { LeaveForm } from './components/LeaveForm'

function App() {
  return (
    <main>
      <h1>Mini Leave Tracker</h1>
      <LeaveForm />
      <h2>Employees</h2>
      <EmployeeList />
    </main>
  )
}

export default App
