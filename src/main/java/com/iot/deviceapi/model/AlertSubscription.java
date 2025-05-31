package com.iot.deviceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AlertSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String metricType;

    private Double threshold;
    private String operator;
    private String notificationUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean active = true;
}
