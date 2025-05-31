package com.iot.deviceapi.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data
public class Telemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;
    @Setter
    private String metricType;
    @Setter
    private Double metricValue;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String unit;


}
