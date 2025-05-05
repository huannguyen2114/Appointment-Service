package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.client.dto.DoctorFeignResponse;
import com.ktpmn.appointment.dto.response.DoctorResponse;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorResponse mapFeignResponseToDoctorResponse(DoctorFeignResponse feignResponse) {
        if (feignResponse == null) {
            return null;
        }
        return DoctorResponse.builder()
                .id(feignResponse.getId())
                .firstName(feignResponse.getFirstName())
                .lastName(feignResponse.getLastName())
                .email(feignResponse.getEmail())
                .specialization(feignResponse.getSpecialization())
                .contactNumber(feignResponse.getContactNumber())
                .build();
    }
}