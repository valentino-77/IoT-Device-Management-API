package com.iot.deviceapi.controller;

import com.iot.deviceapi.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/status-counts")
    public ResponseEntity<Map<String, Long>> getDeviceStatusCounts(
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(reportService.getDeviceStatusCounts());
    }

    @GetMapping("/telemetry-stats/{metric}")
    public ResponseEntity<Map<String, Double>> getTelemetryStats(
            @PathVariable String metric,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(reportService.getTelemetryStats(metric));
    }
}