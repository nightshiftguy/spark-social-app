import { Outlet, useLocation } from 'react-router'
import NavBar from '../components/NavBar'
import '../main.css'

function App() {
  const location = useLocation();
  return (
    <>
      <NavBar location={location}/>
      <main className='container'>
        <div>
          <Outlet />
        </div>
      </main>
    </>
  )
}

export default App
