package org.example.reservationservice.reservation.model;

public enum ServiceType {
    HYBRID_NAILS("Paznokcie hybrydowe bez przedłużania", 60),
    CLASSIC_MANICURE("Manicure klasyczny", 45),
    PEDICURE("Pedicure", 60);

    private final String description;
    private final int durationInMinutes;

    ServiceType(String description, int durationInMinutes) {
        this.description = description;
        this.durationInMinutes = durationInMinutes;
    }

    public String getDescription() {
        return description;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }
}
