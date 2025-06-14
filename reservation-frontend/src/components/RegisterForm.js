import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';

function RegisterForm() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: ''
  });

  const navigate = useNavigate();

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:8080/users/register', formData);
      alert('Zarejestrowano pomyślnie!');
      navigate('/');
    } catch (err) {
      alert('Błąd rejestracji');
    }
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit}>
        <h1>Rejestracja</h1>
        <input
          name="username"
          placeholder="Nazwa użytkownika"
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="E-mail"
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Hasło"
          onChange={handleChange}
          required
        />
        <button type="submit">Zarejestruj się</button>
        <p>Masz już konto? <Link to="/">Zaloguj się</Link></p>
      </form>
    </div>
  );
}

export default RegisterForm;
