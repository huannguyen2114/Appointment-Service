package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.response.PatientAppointmentResponse;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.dto.response.PatientResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Staff;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    Patient toPatient(PatientCreateRequest request);

    PatientCreateResponse toPatientCreateReponse(Patient staff);

    PatientResponse toPatientResponse(Patient patient);

    PatientAppointmentResponse toPatientAppointmentResponse(Patient patient);

}
