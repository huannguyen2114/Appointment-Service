package com.ktpmn.appointment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.constant.AppointmentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {
    String id;
    DoctorResponse doctor; // Changed from StaffResponse
    PatientResponse patient;
    // UUID patientId; // Remove if full PatientResponse is always included
    AppointmentStatus appointmentStatus;
    String description;
    AppointmentType appointmentType;
    OffsetDateTime fromDate;
    OffsetDateTime toDate;
    Integer ordinalNumber;
}
