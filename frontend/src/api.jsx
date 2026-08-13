import { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router";

const API_URL = import.meta.env.VITE_API_URL;

export function useApiFetch(route, options={}, dontFetchYet=false) {
  const navigate = useNavigate();
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);
  const [loading , setLoading] = useState(true);
  const [status, setStatus] = useState(null);

  const handleTokenExpiration = useCallback(() => {
    console.warn("JWT Token expired redirecting to login page");
    localStorage.removeItem('token');
    navigate("/login");
  }, [navigate]);

  useEffect(() => {
    async function fetchData() {
    if(dontFetchYet===true) {
      setLoading(false);
      return;
    }
    if(route===null) {
      setLoading(false);
      return;
    }
    const token = localStorage.getItem('token');
    try {
      const res = await fetch(API_URL + route, {
        ...options,
        headers: {
          'Content-Type': 'application/json',
          ...(token && { Authorization: `Bearer ${token}` }),
          ...(options.headers || {}),
        },
      });
      let json = null;
      try{
       json = await res.json();
      } catch{
        json = null;
      }

      // Handle token expiration and unauthorized access
      if(res.status === 401 && json?.message === "JWT token has expired") {
        handleTokenExpiration();
      }
      if(res.status === 403) {
        navigate("/");
      }

      if(res.status >= 400) {
        let error = new Error(json?.message || 'Error fetching data');
        error = {...error, ...json}
        throw error;
      }

      setData(json);
      setError(null);
      setLoading(false);
      setStatus(res.status);
    } catch (e) {
      setError(e);
      setData(null);
      setLoading(false);
      setStatus(e.status || 500);
    }
  }
  fetchData();
  }, [route, options, navigate, handleTokenExpiration, dontFetchYet]);

  return { data, error, loading, status };
}