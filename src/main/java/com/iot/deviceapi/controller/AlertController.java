package com.iot.deviceapi.controller;

import com.iot.deviceapi.model.AlertSubscription;
import com.iot.deviceapi.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<AlertSubscription> createSubscription(
            @RequestBody AlertSubscription subscription,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(alertService.createSubscription(subscription));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<AlertSubscription>> getSubscriptionsForDevice(
            @PathVariable String deviceId,
            @RequestHeader("X-API-Key") String apiKey) {
        return ResponseEntity.ok(alertService.getSubscriptionsForDevice(deviceId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateSubscriptionStatus(
            @PathVariable Long id,
            @RequestParam boolean active,
            @RequestHeader("X-API-Key") String apiKey) {
        alertService.updateSubscriptionStatus(id, active);
        return ResponseEntity.noContent().build();
    }
}