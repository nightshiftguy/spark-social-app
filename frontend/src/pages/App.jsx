import { Outlet, useLocation } from 'react-router'
import { useState } from 'react';
import NavBar from '../components/NavBar'
import extractUsernameFromJWT from '../utils/extractUsernameFromJWT';
import '../main.css'

function App() {
  const location = useLocation();
  const [isLogged, setIsLogged] = useState(!!localStorage.getItem('token'));
  const [loggedUserUsername, setLoggedUserUsername] = useState(localStorage.token ? extractUsernameFromJWT(localStorage.token) : null);

  return (
    <>
      <NavBar location={location} isLogged={isLogged} loggedUserUsername={loggedUserUsername}/>
      <main className='container'>
        <div>
          <Outlet context={{ isLogged, setIsLogged, loggedUserUsername, setLoggedUserUsername }}/>
        </div>
      </main>
    </>
  )
}

export default App
