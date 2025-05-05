package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.dto.response.AppointmentResponse;
import com.ktpmn.appointment.dto.response.DoctorResponse;
import com.ktpmn.appointment.dto.response.PatientResponse;
import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.service.DoctorExternalService;
import com.ktpmn.appointment.service.PatientExternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentMapper {

    PatientExternalService patientExternalService;
    DoctorExternalService doctorExternalService;

    public AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment) {

        DoctorResponse doctorResponse = null;
        if (appointment.getDoctorId() != null) {
            doctorResponse = doctorExternalService.findDoctorById(appointment.getDoctorId())
                    .orElseGet(() -> {
                        log.warn("Could not find external details for doctor ID {} referenced in appointment {}",
                                appointment.getDoctorId(), appointment.getId());
                        // Return a minimal response or throw an exception based on requirements
                        return DoctorResponse.builder()
                                .firstName("N/A") // Indicate data is missing
                                .lastName("N/A")
                                .build();
                    });
        }

        // Fetch Patient details externally
        PatientResponse patientResponse = null;
        if (appointment.getPatientId() != null) {
            patientResponse = patientExternalService.findPatientById(appointment.getPatientId())
                    .orElseGet(() -> {
                        log.warn("Could not find external details for patient ID {} referenced in appointment {}",
                                appointment.getPatientId(), appointment.getId());
                        return PatientResponse.builder()
                                .firstName("N/A")
                                .lastName("N/A")
                                .build();
                    });
        }

        return AppointmentResponse.builder()
                .id(appointment.getId().toString())
                .doctor(doctorResponse) // Use the fetched doctorResponse
                .patient(patientResponse)
                .appointmentStatus(appointment.getAppointmentStatus())
                .description(appointment.getDescription())
                .appointmentType(appointment.getAppointmentType())
                .fromDate(appointment.getFromDate())
                .toDate(appointment.getToDate())
                .ordinalNumber(appointment.getOrdinalNumber())
                .build();
    }
}