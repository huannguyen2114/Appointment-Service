package com.ktpmn.appointment.dto.response;

import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDateTime dateOfBirth;
    private String email;
    private String contactNumber;
}