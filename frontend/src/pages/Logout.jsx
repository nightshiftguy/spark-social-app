import { useEffect } from 'react';
import { Navigate, useOutletContext } from 'react-router';

export default function Logout() {
    const {isLogged, setIsLogged} = useOutletContext();
  useEffect(() => {
    setIsLogged(false);
    localStorage.removeItem('token');
  }, []);

  return <Navigate to="/login" replace />;
}