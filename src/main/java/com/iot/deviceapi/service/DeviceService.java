package com.iot.deviceapi.service;

import com.iot.deviceapi.model.BulkStatusUpdate;
import com.iot.deviceapi.model.Device;
import com.iot.deviceapi.model.DeviceCommand;
import com.iot.deviceapi.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class DeviceService {

    private final DeviceRepository repository;
    private final AuditService auditService;

    public DeviceService(DeviceRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public Device updateDeviceConfig(String id, Map<String, Object> updates) {
        Device device = repository.findById(id).orElseThrow();

        updates.forEach((key, value) -> {
            switch (key) {
                case "location" -> device.setLocation((String) value);
                case "brightness" -> device.setBrightness((Integer) value);
                case "on" -> device.setPoweredOn((Boolean) value);
                case "setPoint" -> device.setSetPoint((Double) value);
            }
        });

        return repository.save(device);
    }

    public void updateLastSeen(String deviceId) {
        Device device = repository.findById(deviceId).orElseThrow();
        device.setLastSeen(LocalDateTime.now());
        repository.save(device);
    }

    public Device getDeviceById(String id) {
        return repository.findById(id).orElseThrow();
    }

    public void deleteDevice(String id) {
        Device device = repository.findById(id).orElseThrow();
        device.setDeleted(true);
        repository.save(device);
    }

    public List<Device> getAllDevices() {
        return repository.findByDeletedFalse();
    }

    public List<Device> getDevicesByType(String deviceType) {
        return repository.findByDeviceType(deviceType);
    }

    public Device updateFirmware(String id, String version) {
        Device device = repository.findById(id).orElseThrow();
        device.setFirmwareVersion(version);
        device.setLastSeen(LocalDateTime.now());
        return repository.save(device);
    }

    public void sendCommand(String id, DeviceCommand command) {
        System.out.println("Dispatching command to " + id + ": " + command);
    }

    public void bulkUpdateStatus(BulkStatusUpdate update) {
        update.getDeviceIds().forEach(id -> {
            Device device = repository.findById(id).orElseThrow();
            device.setStatus(update.getNewStatus());
            repository.save(device);
        });
    }

    public Device register(Device device) {
        if (device.getStatus() == null) {
            device.setStatus("ONLINE");
        }
        if (device.getLastSeen() == null) {
            device.setLastSeen(LocalDateTime.now());
        }
        if (device.getFirmwareVersion() == null) {
            device.setFirmwareVersion("1.0.0");
        }
        if (device.getPoweredOn() == null) {
            device.setPoweredOn(false);
        }
        if (device.getBrightness() == null) {
            device.setBrightness(0);
        }

        return repository.save(device);
    }

    public void rolloutFirmware(List<String> deviceIds, String version) {
        deviceIds.forEach(id -> updateFirmware(id, version));
    }
}