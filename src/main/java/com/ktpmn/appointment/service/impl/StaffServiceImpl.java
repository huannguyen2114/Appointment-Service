package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;

import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.dto.request.CreateStaffRequest;
import com.ktpmn.appointment.repository.StaffRepository;
import com.ktpmn.appointment.service.StaffService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Import List
import java.util.UUID;
import java.util.stream.Collectors; // Import Collectors

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {
    StaffRepository staffRepository;
    // StaffMapper staffMapper; // Remove StaffMapper field

    // Optional: Inject other repositories or services if needed for validation
    // (e.g., check uniqueness)

    @Override
    @Transactional
    public Staff createStaff(CreateStaffRequest request) {
        // Optional: Add more complex validation here, e.g., checking if
        // email/phone/citizenId/certificationId already exist
        // Example:
        // if (staffRepository.existsByEmail(request.getEmail())) {
        // throw new AppException(ErrorCode.EMAIL_EXISTED); // Define appropriate
        // ErrorCode
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
    }

    @Override
    public ListResponse<StaffResponse> getAllDoctors(Pageable pageable) {
        Page<Staff> resultPage = staffRepository.findByRole(Role.DOCTOR, pageable);

        // Manually map Staff entities to StaffResponse DTOs
        List<StaffResponse> staffResponses = resultPage.getContent().stream()
                .map(this::mapStaffToStaffResponse) // Use helper method for mapping
                .collect(Collectors.toList());

        // Build the ListResponse manually
        return ListResponse.<StaffResponse>builder()
                .listData(staffResponses)
                .pageNumber(resultPage.getPageable().getPageNumber())
                .numberOfElements(resultPage.getNumberOfElements())
                // You might want to add totalElements and totalPages if needed in ListResponse
                // .totalElements(resultPage.getTotalElements())
                // .totalPages(resultPage.getTotalPages())
                .build();
    }

    // Helper method to map Staff entity to StaffResponse DTO
    private StaffResponse mapStaffToStaffResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhoneNumber())
                .role(staff.getRole())
                .dob(staff.getDob())
                .certificationId(staff.getCertificationId())
                .sex(staff.getSex())
                .citizenId(staff.getCitizenId())
                .createdAt(staff.getCreatedAt())
                .updatedAt(staff.getUpdatedAt())
                .build();
    }

    @Override
    public Staff fingById(UUID id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff findByIdAndRole(UUID id, Role role) {
        return staffRepository.findByIdAndRole(id, role);
    }
}
