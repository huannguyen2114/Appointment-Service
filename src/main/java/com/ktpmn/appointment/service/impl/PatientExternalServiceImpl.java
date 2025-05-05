package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.client.PatientFeignClient;
import com.ktpmn.appointment.client.dto.PatientFeignResponse;
import com.ktpmn.appointment.dto.response.PatientResponse;
import com.ktpmn.appointment.mapper.PatientMapper;
import com.ktpmn.appointment.service.PatientExternalService;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Import StringUtils

import java.util.List;
import java.util.Objects; // Import Objects
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // Remove makeFinal = true
public class PatientExternalServiceImpl implements PatientExternalService {

    final PatientFeignClient patientFeignClient; // Mark this as final for constructor injection
    final PatientMapper patientMapper; // + Inject PatientMapper and mark as final

    @Value("${feign.client.config.patient-service.page-size}")
    int patientPageSize; // Keep this non-final, @Value injection will handle it

    @Override
    public Optional<PatientResponse> findPatientById(UUID patientId) {
        return findPatientByIdViaPagination(patientId)
                .map(patientMapper::mapFeignResponseToPatientResponse); // Use mapper instance
    }

    @Override
    public Optional<PatientResponse> findPatientByPhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            log.warn("Attempted to find patient with empty or null phone number.");
            return Optional.empty();
        }

        int currentPage = 1;
        boolean morePages = true;

        while (morePages) {
            log.debug("Fetching patient page: {}, size: {} to find phone number: {}", currentPage, patientPageSize,
                    phoneNumber);
            List<PatientFeignResponse> patientPage;
            try {
                patientPage = patientFeignClient.getPatientsPaginated(currentPage, patientPageSize);
            } catch (FeignException e) {
                log.error("Error fetching patient page {} via Feign client while searching for phone number {}",
                        currentPage, phoneNumber, e);
                return Optional.empty();
            }

            if (patientPage == null || patientPage.isEmpty()) {
                morePages = false;
            } else {
                Optional<PatientFeignResponse> foundPatient = patientPage.stream()

                        .filter(p -> Objects.equals(phoneNumber, p.getContactNumber()))
                        .findFirst();

                if (foundPatient.isPresent()) {
                    log.info("Successfully found patient with phone number: {}", phoneNumber);
                    // Use mapper instance
                    return foundPatient.map(patientMapper::mapFeignResponseToPatientResponse);
                }

                if (patientPage.size() < patientPageSize) {
                    morePages = false;
                } else {
                    currentPage++;
                }
            }
        }

        log.warn("Patient with phone number {} not found after checking all pages in external service.", phoneNumber);
        return Optional.empty();
    }

    private Optional<PatientFeignResponse> findPatientByIdViaPagination(UUID patientId) {
        int currentPage = 1;
        boolean morePages = true;

        while (morePages) {
            log.debug("Fetching patient page: {}, size: {} to find ID: {}", currentPage, patientPageSize, patientId);
            List<PatientFeignResponse> patientPage;
            try {
                patientPage = patientFeignClient.getPatientsPaginated(currentPage, patientPageSize);
            } catch (FeignException e) {
                log.error("Error fetching patient page {} via Feign client for patient ID {}", currentPage, patientId,
                        e);
                return Optional.empty();
            }

            if (patientPage == null || patientPage.isEmpty()) {
                morePages = false;
            } else {
                Optional<PatientFeignResponse> foundPatient = patientPage.stream()
                        .filter(p -> patientId.equals(p.getId()))
                        .findFirst();

                if (foundPatient.isPresent()) {
                    log.info("Successfully found patient with ID: {}", patientId);
                    return foundPatient;
                }

                if (patientPage.size() < patientPageSize) {
                    morePages = false;
                } else {
                    currentPage++;
                }
            }
        }

        log.warn("Patient with ID {} not found after checking all pages in external service.", patientId);
        return Optional.empty();
    }

    // Remove the private mapFeignResponseToPatientResponse method as it's now in
    // PatientMapper
    // private PatientResponse
    // mapFeignResponseToPatientResponse(PatientFeignResponse feignResponse) { ... }
}