import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function LoginForm() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/login', { email, password });
      navigate('/calendar');
    } catch (error) {
      alert('Błąd logowania');
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin}>
        <h1>Zaloguj się</h1>
        <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
        <input type="password" placeholder="Hasło" value={password} onChange={e => setPassword(e.target.value)} required />
        <button type="submit">Zaloguj się</button>
        <p>Nie masz konta? <Link to="/register">Zarejestruj się</Link></p>
      </form>
    </div>
  );
}

export default LoginForm;
