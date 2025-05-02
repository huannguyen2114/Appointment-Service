package com.ktpmn.appointment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ktpmn.appointment.model.Appointment;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientAppointmentResponse {
    UUID id;
    String firstName;
    String lastName;
    String phoneNumber;
    List<AppointmentResponse> appointments;

}
