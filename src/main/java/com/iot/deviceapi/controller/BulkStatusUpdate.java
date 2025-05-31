package com.iot.deviceapi.model;

import lombok.Data;
import java.util.List;

@Data
public class BulkStatusUpdate {
    private List<String> deviceIds;
    private String newStatus;
}
