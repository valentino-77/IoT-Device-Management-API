package com.iot.deviceapi;

import com.iot.deviceapi.service.AuditService;
import com.iot.deviceapi.service.DeviceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan("com.iot.deviceapi.model")
public class DeviceApiApplication {
	@Bean
	public CommandLineRunner demo(DeviceService deviceService, AuditService auditService) {
		return args -> {
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DeviceApiApplication.class, args);

	}


}

