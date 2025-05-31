package com.iot.deviceapi.controller;

import com.iot.deviceapi.model.Telemetry;
import com.iot.deviceapi.service.AuditService;
import com.iot.deviceapi.service.DeviceService;
import com.iot.deviceapi.service.TelemetryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/devices/{deviceId}/telemetry")
public class TelemetryController {
    private final TelemetryService telemetryService;
    private final DeviceService deviceService;
    private final AuditService auditService;

    public TelemetryController(TelemetryService telemetryService,
                               DeviceService deviceService,
                               AuditService auditService) {
        this.telemetryService = telemetryService;
        this.deviceService = deviceService;
        this.auditService = auditService;
    }

    @PostMapping
    public ResponseEntity<Telemetry> ingestTelemetry(
            @PathVariable String deviceId,
            @RequestBody Telemetry telemetry,
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Device-Token") String deviceToken) {

        deviceService.updateLastSeen(deviceId);
        telemetry.setDeviceId(deviceId);
        telemetry.setTimestamp(LocalDateTime.now());

        Telemetry savedTelemetry = telemetryService.saveTelemetry(telemetry);

        auditService.logAction(
                "INGEST_TELEMETRY",
                deviceId,
                apiKey,
                "Received: " + telemetry.getMetricType() + "=" + telemetry.getMetricValue()
        );

        return ResponseEntity.ok(savedTelemetry);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Telemetry>> filterTelemetry(
            @PathVariable String deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(telemetryService.getTelemetryBetweenDates(deviceId, start, end));
    }

    @GetMapping("/latest")
    public ResponseEntity<Telemetry> getLatestTelemetry(
            @PathVariable String deviceId,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(telemetryService.getLatestTelemetry(deviceId));
    }
}