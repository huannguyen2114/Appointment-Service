package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.dto.request.CreateShiftRequest;
import com.ktpmn.appointment.exception.AppException; // Assuming you have a custom exception
import com.ktpmn.appointment.exception.ErrorCode; // Assuming you have error codes
import com.ktpmn.appointment.model.Shift;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.ShiftRepository;
import com.ktpmn.appointment.repository.StaffRepository;
import com.ktpmn.appointment.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final StaffRepository staffRepository; // Inject StaffRepository

    @Override
    @Transactional
    public Shift createShift(CreateShiftRequest request) {
        // 1. Find the staff member
        Staff staff = staffRepository.findById(request.getStaffId())
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_FOUND)); // Use appropriate error code

        // Optional: Add validation for time overlap for the given staff member on that
        // date

        // 2. Create the Shift entity
        Shift newShift = Shift.builder()
                .staff(staff) // Set the staff member
                .shiftName(request.getShiftName())
                .fromTime(request.getStartTime())
                .toTime(request.getEndTime())
                // Set other fields from the request
                .build();

        // 3. Save the shift
        return shiftRepository.save(newShift);
    }

    // Implement other methods from ShiftService interface here
}