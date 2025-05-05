package com.ktpmn.appointment.model;

import java.time.LocalTime; // Import LocalTime
import java.util.UUID;

import jakarta.persistence.Column; // Import Column
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank; // Import NotBlank
import jakarta.validation.constraints.NotNull; // Import NotNull
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "shift")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shift extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;

    @NotBlank(message = "Shift name cannot be blank")
    @Column(name = "shift_name", length = 50)
    String shiftName;

    @NotNull(message = "From time cannot be null")
    @Column(name = "from_time")
    LocalTime fromTime;

    @NotNull(message = "To time cannot be null")
    @Column(name = "to_time")
    LocalTime toTime;

}
