import { useState, useEffect } from 'react';
import { useApiFetch } from '../utils/api';
import { useNavigate, useOutletContext } from 'react-router';
import extractUsernameFromJWT from '../utils/extractUsernameFromJWT';

function LoginPage() {
  const { setIsLogged, setLoggedUserUsername } = useOutletContext();
  const [routeAndOptions, setRouteAndOptions] = useState({ route: null, options: {} });
  const {data, error, loading, status} = useApiFetch(routeAndOptions.route, routeAndOptions.options);
  const navigate = useNavigate();

  useEffect(() => {
    if((!error && status===200)) {
      if(!data.token){ throw new error("No token provided after user logged in")}
      const username = extractUsernameFromJWT(data.token);
      setLoggedUserUsername(username);
      localStorage.setItem('token', data.token);
      setIsLogged(true);
      navigate("/");
    }
  }, [error, status]);
  
  const submit = async (e) => {
    e.preventDefault();
    let newFormData = Object.fromEntries(new FormData(e.target));
    const data = JSON.stringify(newFormData);
    setRouteAndOptions({
      route: 'auth/login',
      options: {
        method: 'POST',
        body: data,
      }
    });
  };

  return (
    <form onSubmit={submit} className='login-form'>
      <h2>Log in</h2>
      <input name="login" placeholder="username" />
      <p className="error">{error && error.login}</p>
      <input name="password" type="password" placeholder="password" />
      <p className="error">{error && error.password}</p>

      <p className="error">{error && error.message}</p>
      <button>Login</button>

      <p className='email-info'>Don't have an account? Follow instructions at <a href="/register">Register</a></p>
      {loading && <p>Loading...</p>}
    </form>
  );
}

export default LoginPage