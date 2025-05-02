package com.ktpmn.appointment.model;

import java.time.OffsetDateTime; // Use OffsetDateTime
import org.hibernate.annotations.CreationTimestamp; // Import Hibernate annotation
import org.hibernate.annotations.UpdateTimestamp; // Import Hibernate annotation
import jakarta.persistence.Column; // Import Column
import jakarta.persistence.MappedSuperclass; // Import MappedSuperclass
import lombok.Data;

@Data
@MappedSuperclass // Indicate this is a base class for entities
public class Audit {

    @CreationTimestamp // Automatically set on creation
    @Column(name = "created_at", nullable = false, updatable = false) // Map to DB column
    private OffsetDateTime createdAt; // Changed type

    @UpdateTimestamp // Automatically set on update
    @Column(name = "updated_at", nullable = false) // Map to DB column
    private OffsetDateTime updatedAt; // Changed type
}
