package com.ktpmn.appointment.exception;

public class PatientHasExistingAppointmentException extends RuntimeException {
    public PatientHasExistingAppointmentException(String message) {
        super(message);
    }
}