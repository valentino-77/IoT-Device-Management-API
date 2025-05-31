package com.iot.deviceapi.service;

import com.iot.deviceapi.model.DeviceCommand;
import com.iot.deviceapi.model.DeviceGroup;
import com.iot.deviceapi.repository.DeviceGroupRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupService {
    private final DeviceGroupRepository repository;
    private final DeviceService deviceService;

    public GroupService(DeviceGroupRepository repository, DeviceService deviceService) {
        this.repository = repository;
        this.deviceService = deviceService;
    }

    public DeviceGroup createGroup(DeviceGroup group) {
        return repository.save(group);
    }

    public void addDevicesToGroup(Long groupId, List<String> deviceIds) {
        DeviceGroup group = repository.findById(groupId).orElseThrow();
        group.getDeviceIds().addAll(deviceIds);
        repository.save(group);
    }

    public void sendCommandToGroup(Long groupId, DeviceCommand command) {
        DeviceGroup group = repository.findById(groupId).orElseThrow();
        for (String deviceId : group.getDeviceIds()) {
            deviceService.sendCommand(deviceId, command);
        }
    }
}