package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.mapper.StaffMapper;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.dto.request.CreateStaffRequest;
import com.ktpmn.appointment.model.Staff; // Import Staff model
import com.ktpmn.appointment.repository.StaffRepository;
import com.ktpmn.appointment.service.StaffService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {
    StaffRepository staffRepository;
    StaffMapper staffMapper;

    private final StaffRepository staffRepository;
    // Optional: Inject other repositories or services if needed for validation (e.g., check uniqueness)

    @Override
    @Transactional
    public Staff createStaff(CreateStaffRequest request) {
        // Optional: Add more complex validation here, e.g., checking if email/phone/citizenId/certificationId already exist
        // Example:
        // if (staffRepository.existsByEmail(request.getEmail())) {
        //     throw new AppException(ErrorCode.EMAIL_EXISTED); // Define appropriate ErrorCode
        // }
        // ... similar checks for other unique fields

        Staff newStaff = Staff.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .dob(request.getDob())
                .certificationId(request.getCertificationId())
                .sex(request.getSex())
                .citizenId(request.getCitizenId())
                // Set other fields from the request if any were added
                .build();

        return staffRepository.save(newStaff);
    @Override
    public Staff fingById(UUID id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff findByIdAndRole(UUID id, Role role) {
        return staffRepository.findByIdAndRole(id, role);
    }
}
