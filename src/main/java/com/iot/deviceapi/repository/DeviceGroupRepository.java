package com.iot.deviceapi.repository;

import com.iot.deviceapi.model.DeviceGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {
}