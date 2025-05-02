package com.ktpmn.appointment.model;

// import java.sql.Date; // Remove this import

import java.time.OffsetDateTime; // Import OffsetDateTime
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.constant.AppointmentType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "appointment")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id") // Explicit mapping for ID
    UUID id;

    // Change from UUID to Patient entity for the relationship
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    Patient patient; // Renamed field from patientId to patient

    @ManyToOne // Add ManyToOne annotation for the relationship
    @JoinColumn(name = "doctor_id") // Specifies the foreign key column
    Staff doctor;

    @Enumerated(EnumType.STRING) // Store enum name as string
    @Column(name = "appointment_status", length = 50) // Map to appointment_status column
    AppointmentStatus appointmentStatus;

    @Column(name = "description", columnDefinition = "text") // Map to description column
    String description;

    @Enumerated(EnumType.STRING) // Store enum name as string
    @Column(name = "appointment_type", length = 50) // Map to appointment_type column
    AppointmentType appointmentType;

    @Column(name = "from_date") // Map to from_date column
    OffsetDateTime fromDate; // Changed from Date to OffsetDateTime

    @Column(name = "to_date") // Map to to_date column
    OffsetDateTime toDate; // Changed from Date to OffsetDateTime

    @Column(name = "ordinal_number", insertable = false, updatable = false) // Map to ordinal_number, let DB handle
    // generation
    Integer ordinalNumber;

    // created_at and updated_at are inherited from Audit class
    // Ensure Audit class fields also use OffsetDateTime and @Column if needed
}
