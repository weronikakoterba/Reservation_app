package org.example.reservationservice.reservation.dto;
public class ServiceDTO {
    private String name;
    private String description;
    private int durationInMinutes;
    private double price;

    public ServiceDTO(String name, String description, int durationInMinutes, double price) {
        this.name = name;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.price = price;
    }

    // Gettery i settery
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getDurationInMinutes() { return durationInMinutes; }
    public void setDurationInMinutes(int durationInMinutes) { this.durationInMinutes = durationInMinutes; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
