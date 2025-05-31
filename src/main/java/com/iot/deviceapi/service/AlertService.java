package com.iot.deviceapi.service;

import com.iot.deviceapi.model.AlertSubscription;
import com.iot.deviceapi.repository.AlertSubscriptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlertService {
    private final AlertSubscriptionRepository repository;

    public AlertService(AlertSubscriptionRepository repository) {
        this.repository = repository;
    }

    public AlertSubscription createSubscription(AlertSubscription subscription) {
        return repository.save(subscription);
    }

    public List<AlertSubscription> getSubscriptionsForDevice(String deviceId) {
        return repository.findByDeviceId(deviceId);
    }

    public void updateSubscriptionStatus(Long id, boolean active) {
        AlertSubscription subscription = repository.findById(id).orElseThrow();
        subscription.setActive(active);
        repository.save(subscription);
    }
}