package com.ktpmn.appointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Return 403 Forbidden
public class DoctorAppointmentMismatchException extends RuntimeException {
    public DoctorAppointmentMismatchException(String message) {
        super(message);
    }
}