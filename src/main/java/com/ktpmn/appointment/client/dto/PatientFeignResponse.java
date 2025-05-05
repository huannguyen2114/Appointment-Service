package com.ktpmn.appointment.client.dto;

import lombok.Data;
import java.time.LocalDateTime; // Use LocalDateTime as per the external model
import java.util.UUID;

// Define Gender enum based on external service if needed, or use String
// Assuming Gender enum exists or using String for simplicity
// package com.ktpmn.appointment.client.enums;
// public enum Gender { MALE, FEMALE, OTHER }

@Data
public class PatientFeignResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String gender; // Or use a Gender enum if defined locally matching the external one
    private LocalDateTime dateOfBirth;
    private String email;
    private String contactNumber;
    // Add other fields like 'address' if they are present in the actual response
    // and needed
}