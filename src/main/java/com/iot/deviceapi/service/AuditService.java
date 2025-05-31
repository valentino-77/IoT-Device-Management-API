package com.iot.deviceapi.service;

import com.iot.deviceapi.model.AuditLog;
import com.iot.deviceapi.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Service
public class AuditService {
    private final AuditLogRepository repository;

    @Autowired
    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void logAction(String action, String entityType, String entityId, String apiKey, String details) {
        logActionInternal(action, entityType, entityId, apiKey, details);
    }

    public void logAction(String action, String entityId, String apiKey, String details) {
        logActionInternal(action, "DEVICE", entityId, apiKey, details);

    }

    private void logActionInternal(String action, String entityType, String entityId, String apiKey, String details) {
        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityId(entityId);
        log.setApiKey(apiKey);
        log.setDetails(details);
        log.setEntityType(entityType);
        log.setTimestamp(LocalDateTime.now());
        repository.save(log);
    }
}