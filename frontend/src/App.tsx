import { useState } from 'react'
import { EmployeeList } from './components/EmployeeList'
import { LeaveForm } from './components/LeaveForm'
import { LeaveList } from './components/LeaveList'

function App() {
  // Bumped whenever a leave is saved, so the list knows to load again.
  const [leaveVersion, setLeaveVersion] = useState(0)

  return (
    <main>
      <h1>Mini Leave Tracker</h1>
      <LeaveForm onCreated={() => setLeaveVersion((version) => version + 1)} />
      <h2>Leave requests</h2>
      <LeaveList leaveVersion={leaveVersion} />
      <h2>Employees</h2>
      <EmployeeList />
    </main>
  )
}

export default App
