package com.iot.deviceapi.model;

import lombok.Data;

@Data
public class DeviceCommand {
    private String commandType;
    private String payload;
}
