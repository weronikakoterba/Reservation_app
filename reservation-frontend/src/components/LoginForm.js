import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function LoginForm() {
  const [username, setUsername] = useState(''); // email → username
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post('http://localhost:8080/users/login', {
        username,
        password
      });

      const token = res.data.token;
      localStorage.setItem('token', token);

      navigate('/calendar');
    } catch (error) {
      alert('Błąd logowania');
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin}>
        <h1>Zaloguj się</h1>
        <input
          type="text"
          placeholder="Nazwa użytkownika"
          value={username}
          onChange={e => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Hasło"
          value={password}
          onChange={e => setPassword(e.target.value)}
          required
        />
        <button type="submit">Zaloguj się</button>
        <p>Nie masz konta? <Link to="/register">Zarejestruj się</Link></p>
      </form>
    </div>
  );
}

export default LoginForm;
