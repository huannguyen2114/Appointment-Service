package com.ktpmn.appointment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Staff;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentCreateResponse {

    String id;

    Staff doctor;

    Patient patient;
}
