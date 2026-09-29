package com.example.Drone_Tracking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "drone_pads")
public class DronePad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String padCode;
    private String airspaceSector;
    private Double hourlyRate;
    private String status; // AVAILABLE, OCCUPIED, MAINTENANCE

    public DronePad() {}

    public DronePad(String padCode, String airspaceSector, Double hourlyRate, String status) {
        this.padCode = padCode;
        this.airspaceSector = airspaceSector;
        this.hourlyRate = hourlyRate;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPadCode() { return padCode; }
    public void setPadCode(String padCode) { this.padCode = padCode; }
    public String getAirspaceSector() { return airspaceSector; }
    public void setAirspaceSector(String airspaceSector) { this.airspaceSector = airspaceSector; }
    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}