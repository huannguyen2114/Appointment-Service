package com.ktpmn.appointment.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.persistence.Column; // Import Column
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank; // Import NotBlank
import jakarta.persistence.OneToMany; // Import OneToMany
// Remove unused imports like Date, Pattern, Email if not needed for other fields
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "patient")
@ToString
public class Patient extends Audit { // Assuming Patient should also extend Audit
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id") // Map to id column
    UUID id;

    @NotBlank(message = "First name cannot be blank") // Added validation
    @Column(name = "first_name", length = 100) // Map to first_name column
    String firstName;

    @NotBlank(message = "Last name cannot be blank") // Added validation
    @Column(name = "last_name", length = 100) // Map to last_name column
    String lastName;

    // Removed commented-out fields as they are not in the patient table definition
    // in 001-init.sql

    // created_at and updated_at are inherited from Audit class

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits") // Keep validation as requested
    @Column(name = "phone_number", length = 15, unique = true) // Map to phone_number column (DB allows 15)
    String phoneNumber;

    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Appointment> appointments;

}
