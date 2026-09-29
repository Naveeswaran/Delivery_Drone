package com.example.Drone_Tracking.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "docking_sessions")
public class DockingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String droneIdentifier;

    @ManyToOne
    @JoinColumn(name = "pad_id")
    private DronePad dronePad;

    @ManyToOne
    @JoinColumn(name = "operator_id")
    private Operator operator;

    private LocalDateTime landedAt;
    private LocalDateTime departedAt;
    private Double totalDurationMinutes;
    private Double calculatedFee;
    private String status; // LANDED, DEPARTED

    public DockingSession() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDroneIdentifier() { return droneIdentifier; }
    public void setDroneIdentifier(String droneIdentifier) { this.droneIdentifier = droneIdentifier; }
    public DronePad getDronePad() { return dronePad; }
    public void setDronePad(DronePad dronePad) { this.dronePad = dronePad; }
    public Operator getOperator() { return operator; }
    public void setOperator(Operator operator) { this.operator = operator; }
    public LocalDateTime getLandedAt() { return landedAt; }
    public void setLandedAt(LocalDateTime landedAt) { this.landedAt = landedAt; }
    public LocalDateTime getDepartedAt() { return departedAt; }
    public void setDepartedAt(LocalDateTime departedAt) { this.departedAt = departedAt; }
    public Double getTotalDurationMinutes() { return totalDurationMinutes; }
    public void setTotalDurationMinutes(Double totalDurationMinutes) { this.totalDurationMinutes = totalDurationMinutes; }
    public Double getCalculatedFee() { return calculatedFee; }
    public void setCalculatedFee(Double calculatedFee) { this.calculatedFee = calculatedFee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}