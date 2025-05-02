package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.request.CreateShiftRequest;
import com.ktpmn.appointment.model.Shift; // Import Shift model

public interface ShiftService {
    Shift createShift(CreateShiftRequest request);
    // Add other shift-related service methods
}