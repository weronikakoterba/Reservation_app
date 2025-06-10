import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { FaEdit } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import '../App.css';

const UserPage = () => {
  const [user, setUser] = useState({ id: null, username: '', email: '' });
  const [reservations, setReservations] = useState([]);

  const [isEditing, setIsEditing] = useState(false);
  const [editedUser, setEditedUser] = useState({ username: '', email: '' });

  const [showPasswordForm, setShowPasswordForm] = useState(false);
  const [passwords, setPasswords] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  });
  const [passwordMessage, setPasswordMessage] = useState('');

  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  // Pobierz dane użytkownika i rezerwacje
  useEffect(() => {
    axios.get('http://localhost:8080/users/me', {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(res => {
      setUser(res.data);
      setEditedUser(res.data);

      if (res.data.id) {
        axios.get(`http://localhost:8080/user/${res.data.id}`, {
          headers: { Authorization: `Bearer ${token}` }
        })
        .then(reservRes => {
          setReservations(reservRes.data);
        })
        .catch(err => {
          console.error('Błąd pobierania rezerwacji', err);
        });
      }
    })
    .catch(err => {
      console.error('Błąd pobierania danych użytkownika', err);
    });
  }, [token]);

  // Edycja danych użytkownika
  const handleChange = (e) => {
    setEditedUser(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSave = () => {
    axios.put('http://localhost:8080/users/update', editedUser, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(res => {
      setUser(res.data);
      setIsEditing(false);
      navigate('/user-panel');
    })
    .catch(err => {
      console.error('Błąd zapisu danych', err);
    });
  };

  // Zmiana hasła
  const handlePasswordChange = (e) => {
    setPasswords(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleChangePassword = () => {
    if (passwords.newPassword !== passwords.confirmPassword) {
      setPasswordMessage('Nowe hasła nie są takie same');
      return;
    }

    axios.post('http://localhost:8080/users/change-password', {
      oldPassword: passwords.oldPassword,
      newPassword: passwords.newPassword
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(() => {
      setPasswordMessage('Hasło zostało zmienione.');
      setShowPasswordForm(false);
      setPasswords({ oldPassword: '', newPassword: '', confirmPassword: '' });
    })
    .catch(() => {
      setPasswordMessage('Nieprawidłowe stare hasło.');
    });
  };

  return (
    <div className="user-page-container flex-container">
      <div className="user-info left-panel">
        <h2>Dane użytkownika</h2>

        <div className="form-group">
          <label>Login:</label>
          {isEditing ? (
            <input
              type="text"
              name="username"
              value={editedUser.username}
              onChange={handleChange}
              className="input"
            />
          ) : (
            <p>{user.username}</p>
          )}
        </div>

        <div className="form-group">
          <label>Email:</label>
          {isEditing ? (
            <input
              type="email"
              name="email"
              value={editedUser.email}
              onChange={handleChange}
              className="input"
            />
          ) : (
            <p>{user.email}</p>
          )}
        </div>

        {isEditing ? (
          <div className="button-group">
            <button className="button save" onClick={handleSave}>Zapisz</button>
            <button className="button cancel" onClick={() => setIsEditing(false)}>Anuluj</button>
          </div>
        ) : (
          <button className="button edit" onClick={() => setIsEditing(true)}>
            <FaEdit className="edit-icon" />
            Edytuj dane
          </button>
        )}

        <button className="button password" onClick={() => {
          setShowPasswordForm(prev => !prev);
          setPasswordMessage('');
          setPasswords({ oldPassword: '', newPassword: '', confirmPassword: '' });
        }}>
          {showPasswordForm ? 'Anuluj zmianę hasła' : 'Zmień hasło'}
        </button>

        {showPasswordForm && (
          <div className="password-form">
            <div className="form-group">
              <label>Stare hasło:</label>
              <input
                type="password"
                name="oldPassword"
                value={passwords.oldPassword}
                onChange={handlePasswordChange}
                className="input"
              />
            </div>
            <div className="form-group">
              <label>Nowe hasło:</label>
              <input
                type="password"
                name="newPassword"
                value={passwords.newPassword}
                onChange={handlePasswordChange}
                className="input"
              />
            </div>
            <div className="form-group">
              <label>Potwierdź nowe hasło:</label>
              <input
                type="password"
                name="confirmPassword"
                value={passwords.confirmPassword}
                onChange={handlePasswordChange}
                className="input"
              />
            </div>
            <button className="button save" onClick={handleChangePassword}>Zapisz hasło</button>
            {passwordMessage && <p className="message">{passwordMessage}</p>}
          </div>
        )}
      </div>

      <div className="reservations right-panel">
        <h2>Twoje rezerwacje</h2>
        {reservations.length === 0 ? (
          <p>Brak rezerwacji.</p>
        ) : (
          <ul>
            {reservations.map((res) => (
              <li key={res.id} className="reservation-item">
                <strong>Data:</strong> {new Date(res.date).toLocaleDateString()} <br />
                <strong>Godzina:</strong> {res.time} <br />
                <strong>Opis:</strong> {res.description || '-'}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default UserPage;
