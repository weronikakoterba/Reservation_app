import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import { FaUserCircle } from 'react-icons/fa';
import { useNavigate } from 'react-router-dom';
import 'react-calendar/dist/Calendar.css';
import '../App.css';
import axios from 'axios';

const generateTimeSlots = (start = 10, end = 17, step = 30) => {
  const slots = [];
  for (let hour = start; hour < end; hour++) {
    slots.push(`${hour}:00`);
    if (step === 30) slots.push(`${hour}:30`);
  }
  return slots;
};

const CalendarPage = () => {
  const [services, setServices] = useState([]);
  const [selectedService, setSelectedService] = useState(null);
  const [date, setDate] = useState(null);
  const [availableReservations, setAvailableReservations] = useState([]);
  const navigate = useNavigate();

  const goToUserPanel = () => {
    navigate('/user-panel');
  };

  useEffect(() => {
    axios.get('http://localhost:8080/services')
      .then(res => setServices(res.data))
      .catch(err => console.error('Błąd pobierania usług:', err));
  }, []);

  useEffect(() => {
    axios.get('http://localhost:8080/reservations/available')
      .then(res => setAvailableReservations(res.data))
      .catch(err => console.error('Błąd pobierania terminów:', err));
  }, []);

  const getTakenSlots = (selectedDate) => {
    return availableReservations
      .filter(r => {
        const resDate = new Date(r.startTime);
        return resDate.toDateString() === selectedDate.toDateString();
      })
      .map(r => {
        const d = new Date(r.startTime);
        return `${d.getHours()}:${d.getMinutes().toString().padStart(2, '0')}`;
      });
  };

  const handleReservationSubmit = async (slot) => {
    if (!selectedService || !date) return;

    const selectedServiceObj = services.find(s => s.name === selectedService);
    if (!selectedServiceObj) return;

    const [hour, minute] = slot.split(':').map(Number);
    const startDate = new Date(date);
    startDate.setHours(hour, minute, 0, 0);

    const reservationData = {
      startTime: startDate.toISOString(),
      price: selectedServiceObj.price,
      description: selectedServiceObj.description,
      serviceType: selectedServiceObj.type,
    };

    const token = localStorage.getItem('token');

    try {
      await axios.post('http://localhost:8080/reservations', reservationData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      alert('Rezerwacja zapisana!');
    } catch (err) {
      console.error('Błąd rezerwacji:', err);
      alert('Nie udało się zapisać rezerwacji.');
    }
  };

  return (
    <div className="calendar-page-container">
      {/* Ikona użytkownika */}
      <div className="user-icon" onClick={goToUserPanel} title="Panel użytkownika">
        <FaUserCircle />
      </div>

      {/* Lista usług */}
      <div className="service-list">
        <h2>Wybierz usługę</h2>
        <ul>
          {services.map((service, idx) => (
            <li key={idx}>
              <label>
                <input
                  type="radio"
                  name="service"
                  checked={selectedService === service.name}
                  onChange={() => setSelectedService(service.name)}
                />
                {service.description} – {service.durationInMinutes} min – {service.price} zł
              </label>
            </li>
          ))}
        </ul>
      </div>

      {/* Kalendarz */}
      {selectedService && (
        <div className="calendar-container">
          <h1>Wybierz termin</h1>
          <Calendar onChange={setDate} value={date} minDate={new Date()} />
        </div>
      )}

      {/* Dostępne godziny */}
      {date && selectedService && (
        <div className="time-slot-container">
          <h2>Dostępne godziny</h2>
          <p>{date.toLocaleDateString()}</p>
          <ul className="time-slot-list">
            {generateTimeSlots().map((slot, idx) => {
              const isTaken = getTakenSlots(date).includes(slot);
              return (
                <li
                  key={idx}
                  className={`time-slot ${isTaken ? 'disabled' : ''}`}
                  onClick={() => !isTaken && handleReservationSubmit(slot)}
                >
                  {slot}
                </li>
              );
            })}
          </ul>
        </div>
      )}
    </div>
  );
};

export default CalendarPage;
