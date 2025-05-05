package com.ktpmn.appointment.model;

// import java.sql.Date; // Remove this import
import java.time.OffsetDateTime; // Import OffsetDateTime
import java.util.UUID;

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
    @Column(name = "id")
    UUID id;

    UUID patientId;

    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", length = 50)
    AppointmentStatus appointmentStatus;

    @Column(name = "description", columnDefinition = "text")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", length = 50)
    AppointmentType appointmentType;

    @Column(name = "from_date")
    OffsetDateTime fromDate;

    @Column(name = "to_date")
    OffsetDateTime toDate;

    @Column(name = "ordinal_number", insertable = false, updatable = false)
    Integer ordinalNumber;

}
