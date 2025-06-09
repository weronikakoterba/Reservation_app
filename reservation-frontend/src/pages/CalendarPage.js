import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import '../App.css';

const services = [
  { name: 'Paznokcie hybrydowe bez przedłużania', duration: 60 },
  { name: 'Manicure klasyczny', duration: 45 },
  { name: 'Pedicure', duration: 60 },
  { name: 'Paznokcie żelowe', duration: 90 },
  { name: 'Paznokcie żelowe z przedłużaniem', duration: 120 },
  { name: 'Zdobienie paznokci', duration: 30 },
  { name: 'Ściągnięcie hybrydy lub żelu', duration: 20 },
  { name: 'Zabieg regenerujący paznokcie i skórki', duration: 25 }
];

const generateTimeSlots = (start = 10, end = 17, step = 30) => {
  const slots = [];
  for (let hour = start; hour < end; hour++) {
    slots.push(`${hour}:00`);
    if (step === 30) slots.push(`${hour}:30`);
  }
  slots.push(`${end}:00`);
  return slots;
};

const CalendarPage = () => {
  const [selectedServices, setSelectedServices] = useState([]);
  const [date, setDate] = useState(null);
  const timeSlots = generateTimeSlots();

  const toggleService = (service) => {
    setSelectedServices((prev) =>
      prev.includes(service)
        ? prev.filter((s) => s !== service)
        : [...prev, service]
    );
  };

  return (
    <div className="calendar-page-container">
      {/* Usługi */}
      <div className="service-list">
        <h2>Wybierz usługę</h2>
        <ul>
          {services.map((service, idx) => (
            <li key={idx}>
              <label>
                <input
                  type="checkbox"
                  checked={selectedServices.includes(service.name)}
                  onChange={() => toggleService(service.name)}
                />
                {service.name} – {service.duration} min
              </label>
            </li>
          ))}
        </ul>
      </div>

      {/* Kalendarz */}
      {selectedServices.length > 0 && (
        <div className="calendar-container">
          <h1>Wybierz termin</h1>
          <Calendar
            onChange={setDate}
            value={date}
            minDate={new Date()}
          />
        </div>
      )}

      {/* Godziny */}
      {date && selectedServices.length > 0 && (
        <div className="time-slot-container">
          <h2>Dostępne godziny</h2>
          <p>{date.toLocaleDateString()}</p>
          <ul className="time-slot-list">
            {timeSlots.map((slot, idx) => (
              <li key={idx} className="time-slot">{slot}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default CalendarPage;
