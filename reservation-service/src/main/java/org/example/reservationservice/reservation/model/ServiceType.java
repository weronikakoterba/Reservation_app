package org.example.reservationservice.reservation.model;

import lombok.Getter;

@Getter
public enum ServiceType {
    HYBRID_NAILS("Paznokcie hybrydowe bez przedłużania", 60),
    CLASSIC_MANICURE("Manicure klasyczny", 45),
    PEDICURE("Pedicure", 60),
    GEL_NAILS("Paznokcie żelowe na naturalnej płytce", 90),
    GEL_NAILS_WITH_EXTENSION("Paznokcie żelowe z przedłużeniem", 120),
    NAIL_RECONSTRUCTION("Rekonstrukcja paznokcia", 30),
    NAIL_ART("Zdobienie paznokci (1-2 paznokcie)", 15),
    NAIL_REMOVAL("Ściągnięcie hybrydy lub żelu", 30),
    NAIL_OIL_TREATMENT("Zabieg regenerujący na paznokcie i skórki", 40);

    private final String description;
    private final int durationInMinutes;

    ServiceType(String description, int durationInMinutes) {
        this.description = description;
        this.durationInMinutes = durationInMinutes;
    }

}
