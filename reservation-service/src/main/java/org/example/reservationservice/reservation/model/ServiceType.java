package org.example.reservationservice.reservation.model;

import lombok.Getter;

@Getter
public enum ServiceType {
    HYBRID_NAILS("Paznokcie hybrydowe bez przedłużania", 60, 100.00),
    CLASSIC_MANICURE("Manicure klasyczny", 45,80.00),
    PEDICURE("Pedicure", 60, 100.00),
    GEL_NAILS("Paznokcie żelowe na naturalnej płytce", 90, 120.00),
    GEL_NAILS_WITH_EXTENSION("Paznokcie żelowe z przedłużeniem", 120, 160.00),
    NAIL_RECONSTRUCTION("Rekonstrukcja paznokcia", 30,20.00),
    NAIL_ART("Zdobienie paznokci", 30, 80.00 ),
    NAIL_REMOVAL("Ściągnięcie hybrydy lub żelu", 30, 40.00),
    NAIL_OIL_TREATMENT("Zabieg regenerujący na paznokcie i skórki", 40, 60.00);

    private final String description;
    private final int durationInMinutes;
    private final double price;

    ServiceType(String description, int durationInMinutes, Double price) {
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.price = price;
    }

}
