package com.iot.deviceapi.service;

import com.iot.deviceapi.model.Telemetry;
import com.iot.deviceapi.repository.TelemetryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetryService {
    private final TelemetryRepository repository;

    public Telemetry saveTelemetry(Telemetry telemetry) {
        return repository.save(telemetry);
    }

    public TelemetryService(TelemetryRepository repository) {
        this.repository = repository;
    }
    public List<Telemetry> getTelemetryBetweenDates(String deviceId, LocalDateTime start, LocalDateTime end) {
        return repository.findByDeviceIdAndTimestampBetween(deviceId, start, end);
    }

    public Telemetry getLatestTelemetry(String deviceId) {
        return repository.findTopByDeviceIdOrderByTimestampDesc(deviceId);
    }


    public List<Telemetry> getTelemetryByDeviceId(String deviceId) {
        return repository.findByDeviceId(deviceId);
    }


}
