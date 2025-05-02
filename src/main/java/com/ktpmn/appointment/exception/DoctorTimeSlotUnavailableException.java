package com.ktpmn.appointment.exception;

public class DoctorTimeSlotUnavailableException extends RuntimeException {
    public DoctorTimeSlotUnavailableException(String message) {
        super(message);
    }
}