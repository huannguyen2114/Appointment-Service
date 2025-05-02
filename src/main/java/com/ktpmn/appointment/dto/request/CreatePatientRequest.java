package com.ktpmn.appointment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePatientRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    // Add other relevant fields if needed, e.g., dob, address, contact info
    // Ensure these fields exist in the Patient entity as well.
}