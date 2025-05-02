package com.ktpmn.appointment.dto.request;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.constant.Sex;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreateRequest {
    String firstName;

    String lastName;

    String email;

    String phoneNumber;

    Role role;

    LocalDate dob; // Changed to LocalDate

    String certificationId;

    Sex sex;

    String citizenId;

}
