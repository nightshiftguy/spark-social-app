import { useState, useEffect } from 'react';
import { useApiFetch } from '../utils/api';
import { useNavigate, useOutletContext } from 'react-router';
import extractUsernameFromJWT from '../utils/extractUsernameFromJWT';
import Logo from '../components/Logo';
import './LoginAndRegister.css'

function RegisterPage() {
  const { setIsLogged, setLoggedUserUsername } = useOutletContext();
  const [routeAndOptions, setRouteAndOptions] = useState({ route: null, options: {} });
  const {data, error, loading, status} = useApiFetch(routeAndOptions.route, routeAndOptions.options);
  const navigate = useNavigate();

  useEffect(() => {
    if((!error && status===200)) {
      if(!data.token){ throw new error("No token provided after user registered")}
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
      route: 'auth/register',
      options: {
        method: 'POST',
        body: data,
      }
    });
  };

  return (<div className='register-container'>
    <Logo/>

    <form onSubmit={submit} className='login-form'>
      <h2>Register</h2>
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

      <button>Register</button>

      <p className="error">{error && error.message}</p>
      <p className='loading'>{loading && "Loading..."}</p>
    </form>
    <p className='email-info'>Already have account? Visit <a href="/login">Log in</a></p>
  </div>);    
}

export default RegisterPage