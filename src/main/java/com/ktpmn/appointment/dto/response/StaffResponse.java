package com.ktpmn.appointment.dto.response;

import com.ktpmn.appointment.constant.Role; // Import Role
import com.ktpmn.appointment.constant.Sex; // Import Sex
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate; // Import LocalDate
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDate dob;
    private String certificationId;
    private Sex sex;
    private String citizenId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    // Add other fields from the Staff entity you want to expose
}