package com.iot.deviceapi.repository;

import com.iot.deviceapi.model.AlertSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertSubscriptionRepository extends JpaRepository<AlertSubscription, Long> {
    List<AlertSubscription> findByDeviceId(String deviceId);
    List<AlertSubscription> findByActiveTrue();
}