package com.ktpmn.appointment.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentCreateRequest {
    UUID patientId;

    UUID doctorId;

}
