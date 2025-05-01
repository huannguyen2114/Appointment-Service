package com.ktpmn.appointment.model;

import java.util.UUID;

import jakarta.persistence.Column; // Import Column
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank; // Import NotBlank
// Remove unused imports like Date, Pattern, Email if not needed for other fields
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "patient") // Corrected table name to "patient"
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
