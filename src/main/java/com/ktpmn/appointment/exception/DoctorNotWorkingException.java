package com.ktpmn.appointment.exception;

public class DoctorNotWorkingException extends RuntimeException {
    public DoctorNotWorkingException(String message) {
        super(message);
    }
}