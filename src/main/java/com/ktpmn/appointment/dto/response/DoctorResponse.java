package com.ktpmn.appointment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DoctorResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private String contactNumber;
}