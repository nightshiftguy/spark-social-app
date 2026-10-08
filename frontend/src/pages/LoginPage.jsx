import { useState, useEffect } from 'react';
import { useApiFetch } from '../utils/api';
import { useNavigate, useOutletContext } from 'react-router';
import extractUsernameFromJWT from '../utils/extractUsernameFromJWT';
import Logo from '../components/Logo';
import './LoginAndRegister.css'

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
    <div className='login-container'>
      <Logo/>

      <form onSubmit={submit} className='login-form'>
      <h2>Log in</h2>
      <div>
        <label htmlFor="login">username</label>
        <input name="login"/>
      </div>
      <p className="error">{error && error.login}</p>

      <div>
        <label htmlFor="password">password</label>
        <input name="password" type="password" placeholder="password" />
      </div>
      <p className="error">{error && error.password}</p>

      <button>Login</button>

      <p className="error">{error && error.message}</p>
      <p className='loading'>{loading && "Loading..."}</p>
    </form>
    <p className='email-info'>Don't have an account? Follow instructions at <a href="/register">Register</a></p>
    </div>
  );
}

export default LoginPage