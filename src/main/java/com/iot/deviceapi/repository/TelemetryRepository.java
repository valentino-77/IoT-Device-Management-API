package com.iot.deviceapi.repository;

import com.iot.deviceapi.model.Telemetry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TelemetryRepository extends JpaRepository<Telemetry, Long> {
    List<Telemetry> findByDeviceId(String deviceId);

    List<Telemetry> findByDeviceIdAndTimestampBetween(String deviceId, LocalDateTime start, LocalDateTime end);

    Telemetry findTopByDeviceIdOrderByTimestampDesc(String deviceId);

}
