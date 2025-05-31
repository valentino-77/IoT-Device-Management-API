package com.iot.deviceapi.service;

import com.iot.deviceapi.model.Device;
import com.iot.deviceapi.repository.DeviceRepository;
import com.iot.deviceapi.repository.TelemetryRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final DeviceRepository deviceRepository;
    private final TelemetryRepository telemetryRepository;

    public ReportService(DeviceRepository deviceRepository, TelemetryRepository telemetryRepository) {
        this.deviceRepository = deviceRepository;
        this.telemetryRepository = telemetryRepository;
    }

    public Map<String, Long> getDeviceStatusCounts() {
        Map<String, Long> counts = new HashMap<>();
        List<Device> devices = deviceRepository.findByDeletedFalse();

        for (Device device : devices) {
            String status = device.getStatus();
            counts.put(status, counts.getOrDefault(status, 0L) + 1);
        }
        return counts;
    }

    public Map<String, Double> getTelemetryStats(String metric) {
        Map<String, Double> stats = new HashMap<>();
        stats.put("average", 25.5);
        stats.put("max", 40.0);
        stats.put("min", 10.0);
        return stats;
    }
}