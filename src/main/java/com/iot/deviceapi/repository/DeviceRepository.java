package com.iot.deviceapi.repository;

import com.iot.deviceapi.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByDeletedFalse();

    List<Device> findByDeviceType(String deviceType);
}
