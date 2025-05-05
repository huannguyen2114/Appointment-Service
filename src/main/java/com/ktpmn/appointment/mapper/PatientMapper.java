package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.client.dto.PatientFeignResponse;
import com.ktpmn.appointment.dto.response.PatientResponse;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientResponse mapFeignResponseToPatientResponse(PatientFeignResponse feignResponse) {
        if (feignResponse == null) {
            return null;
        }
        return PatientResponse.builder()
                .id(feignResponse.getId())
                .firstName(feignResponse.getFirstName())
                .lastName(feignResponse.getLastName())
                .contactNumber(feignResponse.getContactNumber())
                .dateOfBirth(feignResponse.getDateOfBirth())
                .email(feignResponse.getEmail())
                .gender(feignResponse.getGender())
                .build();
    }
}