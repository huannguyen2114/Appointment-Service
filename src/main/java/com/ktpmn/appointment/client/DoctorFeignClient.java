package com.ktpmn.appointment.client;

import com.ktpmn.appointment.client.dto.DoctorFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType; // Import MediaType
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

// Ensure this URL property points to the base URL (e.g., http://patient:8080)
@FeignClient(name = "doctor-service", url = "${feign.client.config.patient-service.url}")
public interface DoctorFeignClient {

    // Add 'produces' to explicitly set the Accept header to application/json
    @GetMapping(value = "/api/doctors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    DoctorFeignResponse getDoctorById(@PathVariable("id") UUID id);

}