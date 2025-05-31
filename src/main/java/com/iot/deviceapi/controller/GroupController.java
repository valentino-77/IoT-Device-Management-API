package com.iot.deviceapi.controller;

import com.iot.deviceapi.model.DeviceCommand;
import com.iot.deviceapi.model.DeviceGroup;
import com.iot.deviceapi.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<DeviceGroup> createGroup(
            @RequestBody DeviceGroup group,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @PostMapping("/{groupId}/devices")
    public ResponseEntity<Void> addDevicesToGroup(
            @PathVariable Long groupId,
            @RequestBody List<String> deviceIds,
            @RequestHeader("X-API-Key") String apiKey) {
        groupService.addDevicesToGroup(groupId, deviceIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/command")
    public ResponseEntity<String> sendCommandToGroup(
            @PathVariable Long groupId,
            @RequestBody DeviceCommand command,
            @RequestHeader("X-API-Key") String apiKey) {
        groupService.sendCommandToGroup(groupId, command);
        return ResponseEntity.ok("Command sent to group");
    }
}