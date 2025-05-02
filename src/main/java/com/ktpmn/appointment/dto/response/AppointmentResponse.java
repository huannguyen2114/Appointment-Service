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
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentResponse {

    String id;

    StaffResponse doctor;

    PatientResponse patient;

    AppointmentStatus appointmentStatus;

    String description;

    AppointmentType appointmentType;

    OffsetDateTime fromDate; // Changed from Date to OffsetDateTime

    OffsetDateTime toDate; // Changed from Date to OffsetDateTime

    Integer ordinalNumber;

}
