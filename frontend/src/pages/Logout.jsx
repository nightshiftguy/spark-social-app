import { useEffect } from 'react';
import { Navigate, useOutletContext } from 'react-router';

export default function Logout() {
  const {setIsLogged, setLoggedUserUsername} = useOutletContext();
  useEffect(() => {
    setIsLogged(false);
    setLoggedUserUsername(undefined);
    localStorage.removeItem('token');
  });

  return <Navigate to="/login" replace />;
}