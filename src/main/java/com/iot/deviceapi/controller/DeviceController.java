package com.iot.deviceapi.controller;

import com.iot.deviceapi.model.BulkStatusUpdate;
import com.iot.deviceapi.model.Device;
import com.iot.deviceapi.model.DeviceCommand;
import com.iot.deviceapi.service.AuditService;
import com.iot.deviceapi.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    private final AuditService auditService;
    private final DeviceService service;

    public DeviceController(DeviceService service, AuditService auditService) {
        this.service = service;
        this.auditService = auditService;
    }

    @PatchMapping("/{id}/config")
    public ResponseEntity<Device> updateDeviceConfig(
            @PathVariable String id,
            @RequestBody Map<String, Object> configUpdates,
            @RequestHeader("X-API-Key") String apiKey) {

        Device updatedDevice = service.updateDeviceConfig(id, configUpdates);

        auditService.logAction(
                "UPDATE_CONFIG",
                id,
                apiKey,
                "Updated config: " + configUpdates.toString()
        );

        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(
            @PathVariable String id,
            @RequestHeader("X-API-Key") String apiKey) {
        Device device = service.getDeviceById(id);
        auditService.logAction("GET_DEVICE", id, apiKey, "Retrieved device: " + device.getName());
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @PathVariable String id,
            @RequestHeader("X-API-Key") String apiKey) {
        Device device = service.getDeviceById(id);
        service.deleteDevice(id);
        auditService.logAction("DELETE_DEVICE", id, apiKey, "Deleted device: " + device.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/firmware-rollout")
    public ResponseEntity<String> firmwareRollout(
            @RequestParam String version,
            @RequestBody List<String> deviceIds,
            @RequestHeader("X-API-Key") String apiKey) {

        service.rolloutFirmware(deviceIds, version);

        auditService.logAction(
                "FIRMWARE_ROLLOUT",
                "MULTIPLE",
                apiKey,
                "Rolled out v" + version + " to " + deviceIds.size() + " devices"
        );

        return ResponseEntity.ok("Firmware rollout started");
    }

    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices(
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(service.getAllDevices());
    }

    @GetMapping("/type/{deviceType}")
    public ResponseEntity<List<Device>> getDevicesByType(
            @PathVariable String deviceType,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(service.getDevicesByType(deviceType));
    }

    @PatchMapping("/{id}/firmware")
    public ResponseEntity<Device> updateFirmware(
            @PathVariable String id,
            @RequestParam String version,
            @RequestHeader("X-API-Key") String apiKey) {
        Device device = service.updateFirmware(id, version);
        auditService.logAction("UPDATE_FIRMWARE", id, apiKey, "Updated to v" + version);
        return ResponseEntity.ok(device);
    }

    @PostMapping("/{id}/commands")
    public ResponseEntity<String> sendCommand(
            @PathVariable String id,
            @RequestBody DeviceCommand command,
            @RequestHeader("X-API-Key") String apiKey,
            @RequestHeader("X-Device-Token") String deviceToken) {
        service.sendCommand(id, command);
        auditService.logAction("SEND_COMMAND", id, apiKey, "Sent: " + command.getCommandType());
        return ResponseEntity.ok("Command sent");
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Device> registerDevice(
            @RequestBody Device device,
            @RequestHeader("X-API-Key") String apiKey) {
        Device savedDevice = service.register(device);
        auditService.logAction("REGISTER_DEVICE", savedDevice.getId(), apiKey, "Registered: " + savedDevice.getName());
        return ResponseEntity.ok(savedDevice);
    }

    @PostMapping("/bulk-status")
    public ResponseEntity<String> bulkStatusUpdate(
            @RequestBody BulkStatusUpdate update,
            @RequestHeader("X-API-Key") String apiKey) {
        service.bulkUpdateStatus(update);
        auditService.logAction(
                "BULK_STATUS_UPDATE",
                "MULTIPLE",
                apiKey,
                "Set status to " + update.getNewStatus() + " for " + update.getDeviceIds().size() + " devices"
        );
        return ResponseEntity.ok("Bulk update completed");
    }
}
