package com.iot.deviceapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private String entityId;
    private String apiKey;
    private String details;
    private String entityType;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }



}
