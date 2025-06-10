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
  const [selectedSlot, setSelectedSlot] = useState(null);
  const [paymentMessage, setPaymentMessage] = useState('');
  const [isProcessingPayment, setIsProcessingPayment] = useState(false);
  const [showSummaryModal, setShowSummaryModal] = useState(false);

  const navigate = useNavigate();

  const goToUserPanel = () => {
    navigate('/user-panel');
  };

  useEffect(() => {
    axios.get('http://localhost:8081/services')
        .then(res => setServices(res.data))
        .catch(err => console.error('Błąd pobierania usług:', err));
  }, []);

  useEffect(() => {
    axios.get('http://localhost:8081/reservations/available')
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

  const handleSlotClick = (slot) => {
    setSelectedSlot(slot);
    setPaymentMessage('');
  };

  const handleBookingInitiation = () => {
    if (!selectedSlot || !date || !selectedService) {
      return alert('Wybierz usługę, datę i godzinę');
    }
    setShowSummaryModal(true);
  };

  const confirmBookingWithPayment = async () => {
    const matchedReservation = availableReservations.find(r => {
      const d = new Date(r.startTime);
      const [h, m] = selectedSlot.split(':').map(Number);
      return d.toDateString() === date.toDateString() && d.getHours() === h && d.getMinutes() === m;
    });

    if (!matchedReservation) {
      alert('Nie znaleziono rezerwacji dla wybranego terminu.');
      return;
    }

    const token = localStorage.getItem('token');

    try {
      setIsProcessingPayment(true);
      setPaymentMessage('⏳ Przetwarzanie płatności...');

      await axios.put(
          `http://localhost:8081/reservations/${matchedReservation.id}/bookWithPayment`,
          null,
          { headers: { Authorization: `Bearer ${token}` } }
      );

      setPaymentMessage('✅ Rezerwacja została potwierdzona i opłacona!');
      setSelectedSlot(null);
      setDate(null);
      setShowSummaryModal(false);
    } catch (err) {
      console.error('Błąd rezerwacji z płatnością:', err);
      setPaymentMessage('❌ Nie udało się dokonać rezerwacji z płatnością.');
    } finally {
      setIsProcessingPayment(false);
    }
  };

  return (
      <div className="calendar-page-container">
        <div className="user-icon" onClick={goToUserPanel} title="Panel użytkownika">
          <FaUserCircle />
        </div>

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

        {selectedService && (
            <div className="calendar-container">
              <h1>Wybierz termin</h1>
              <Calendar onChange={setDate} value={date} minDate={new Date()} />
            </div>
        )}

        {date && selectedService && (
            <div className="time-slot-container">
              <h2>Dostępne godziny</h2>
              <p>{date.toLocaleDateString()}</p>
              <ul className="time-slot-list">
                {generateTimeSlots().map((slot, idx) => {
                  const isTaken = getTakenSlots(date).includes(slot);
                  const isSelected = selectedSlot === slot;
                  return (
                      <li
                          key={idx}
                          className={`time-slot ${isTaken ? 'disabled' : ''} ${isSelected ? 'selected' : ''}`}
                          onClick={() => !isTaken && handleSlotClick(slot)}
                      >
                        {slot}
                      </li>
                  );
                })}
              </ul>

              {paymentMessage && (
                  <div style={{ marginTop: '10px', fontWeight: 'bold', color: isProcessingPayment ? 'blue' : paymentMessage.includes('✅') ? 'green' : 'red' }}>
                    {paymentMessage}
                  </div>
              )}

              {selectedSlot && (
                  <button
                      className="button reserve-button"
                      style={{ float: 'right', marginTop: '10px' }}
                      onClick={handleBookingInitiation}
                      disabled={isProcessingPayment}
                  >
                    Zarezerwuj
                  </button>
              )}
            </div>
        )}

        {/* MODAL Z POTWIERDZENIEM */}
        {showSummaryModal && (
            <div className="modal-overlay">
              <div className="modal-content">
                <h3>Potwierdzenie rezerwacji</h3>
                <p><strong>Usługa:</strong> {selectedService}</p>
                <p><strong>Data:</strong> {date?.toLocaleDateString()}</p>
                <p><strong>Godzina:</strong> {selectedSlot}</p>
                <button onClick={confirmBookingWithPayment} disabled={isProcessingPayment}>
                  {isProcessingPayment ? 'Przetwarzanie...' : 'Potwierdź i zapłać'}
                </button>
                <button onClick={() => setShowSummaryModal(false)} disabled={isProcessingPayment}>
                  Anuluj
                </button>
              </div>
            </div>
        )}
      </div>
  );
};

export default CalendarPage;
