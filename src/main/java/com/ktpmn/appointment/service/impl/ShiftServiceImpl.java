package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.dto.request.CreateShiftRequest;

import com.ktpmn.appointment.exception.ResourceNotFoundException;
import com.ktpmn.appointment.model.Shift;

import com.ktpmn.appointment.repository.ShiftRepository;

import com.ktpmn.appointment.service.DoctorExternalService;
import com.ktpmn.appointment.service.ShiftService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    private final DoctorExternalService doctorExternalService;

    @Override
    @Transactional
    public Shift createShift(CreateShiftRequest request) {
        doctorExternalService.findDoctorById(request.getStaffId())
                .orElseThrow(() -> {
                    log.warn("Doctor/Staff with ID {} not found via external service.", request.getStaffId());
                    return new ResourceNotFoundException("Doctor/Staff", request.getStaffId().toString());
                });
        log.info("Successfully validated existence of doctor/staff with ID: {}", request.getStaffId());

        Shift newShift = Shift.builder()
                .doctorId(request.getStaffId())
                .shiftName(request.getShiftName())
                .fromTime(request.getStartTime())
                .toTime(request.getEndTime())
                .build();

        return shiftRepository.save(newShift);
    }

}