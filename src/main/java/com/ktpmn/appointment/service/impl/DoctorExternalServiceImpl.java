package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.client.DoctorFeignClient;
import com.ktpmn.appointment.client.dto.DoctorFeignResponse;
import com.ktpmn.appointment.dto.response.DoctorResponse;
import com.ktpmn.appointment.mapper.DoctorMapper;
import com.ktpmn.appointment.service.DoctorExternalService;

import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorExternalServiceImpl implements DoctorExternalService {

    final DoctorFeignClient doctorFeignClient;
    final DoctorMapper doctorMapper;

    @Value("${feign.client.config.patient-service.url}")
    String doctorServiceBaseUrl;

    @Override
    public Optional<DoctorResponse> findDoctorById(UUID doctorId) {
        try {
            log.debug("Fetching doctor by ID {} from external service at base URL: {}", doctorId, doctorServiceBaseUrl);
            DoctorFeignResponse feignResponse = doctorFeignClient.getDoctorById(doctorId);
            log.info("Successfully fetched doctor with ID: {}", doctorId);
            return Optional.ofNullable(doctorMapper.mapFeignResponseToDoctorResponse(feignResponse));
        } catch (FeignException.NotFound e) {
            log.warn("Doctor with ID {} not found in external service (URL: {}).", doctorId, doctorServiceBaseUrl);
            return Optional.empty();
        } catch (FeignException e) {

            log.error("Error fetching doctor ID {} via Feign client (URL: {})", doctorId, doctorServiceBaseUrl, e);
            return Optional.empty();
        }
    }

}