package com.ktpmn.appointment.dto.request;

import com.ktpmn.appointment.constant.Role; // Import Role enum
import com.ktpmn.appointment.constant.Sex; // Import Sex enum
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past; // For DOB validation
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate; // Import LocalDate

@Data
public class CreateStaffRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Certification ID cannot be blank")
    private String certificationId;

    @NotNull(message = "Sex cannot be null")
    private Sex sex;

    @NotBlank(message = "Citizen ID cannot be blank")
    private String citizenId;

}