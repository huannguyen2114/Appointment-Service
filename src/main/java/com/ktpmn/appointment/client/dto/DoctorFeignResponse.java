package com.ktpmn.appointment.client.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DoctorFeignResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization; // Add fields from the example payload
    private String contactNumber;
}