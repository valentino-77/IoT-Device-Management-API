package com.iot.deviceapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Table(name = "DEVICE")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String model;

    @Column(name = "is_on")
    private Boolean poweredOn;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status = "OFFLINE";

    private String firmwareVersion = "1.0.0";

    @Column(nullable = false)
    private String deviceType;

    private LocalDateTime lastSeen;
    private boolean deleted = false;

    @Column
    private Double currentTemperature;

    @Column
    private Double setPoint;

    @Column
    private Integer brightness;

    @Column
    private String ipAddress;

}
