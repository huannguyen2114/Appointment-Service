package com.ktpmn.appointment.model;

// import java.util.Date; // Remove this import

import java.time.LocalDate; // Import LocalDate
import java.util.UUID;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.constant.Sex;

import jakarta.persistence.Column; // Import Column
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // Import EnumType
import jakarta.persistence.Enumerated; // Import Enumerated
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email; // Keep Email import
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Import NotNull
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "staff")
public class Staff extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id") // Map to id column
    UUID id;

    @NotBlank(message = "First name cannot be blank")
    @Column(name = "first_name", length = 100) // Map to first_name column
    String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Column(name = "last_name", length = 100) // Map to last_name column
    String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format") // Keep validation
    @Column(name = "email", length = 100, unique = true) // Map to email column
    String email;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits") // Keep validation as requested
    @Column(name = "phone_number", length = 15, unique = true) // Map to phone_number column (DB allows 15)
    String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @Enumerated(EnumType.STRING) // Store enum name as string
    @Column(name = "role", length = 50) // Map to role column
    Role role;

    @NotNull(message = "Date of birth cannot be null")
    @Column(name = "dob") // Map to dob column
    LocalDate dob; // Changed to LocalDate

    @NotBlank(message = "Certification ID cannot be blank")
    @Column(name = "certification_id", length = 20, unique = true) // Map to certification_id column
    String certificationId;

    @NotNull(message = "Sex cannot be null")
    @Enumerated(EnumType.STRING) // Store enum name as string
    @Column(name = "sex", length = 50) // Map to sex column
    Sex sex;

    @NotBlank(message = "Citizen ID cannot be blank")
    @Column(name = "citizen_id", length = 12, unique = true) // Map to citizen_id column
    String citizenId;

    // created_at and updated_at are inherited from Audit class
    // Ensure Audit class fields use appropriate types (e.g., OffsetDateTime) and
    // @Column if needed
}
