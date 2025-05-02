package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.response.*;
import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface AppointmentMapper {

    AppointmentCreateResponse toAppointmentCreateResponse(Appointment request);

    AppointmentResponse toAppointmentResponse(Appointment request);

    @Mapping(source = "content", target = "listData")
    @Mapping(source = "pageable.pageNumber", target = "pageNumber")
    @Mapping(source = "numberOfElements", target = "numberOfElements")
    ListResponse<AppointmentResponse> toListAppointmentResponse(Page<Appointment> pageAppointment);

//    default ListResponse<AppointmentResponse> toListAppointmentResponse(Page<Appointment> appointments) {
//        List<AppointmentResponse> responses = appointments.getContent()
//                .stream()
//                .map(this::toAppointmentResponse)
//                .toList();
//        return new ListResponse<>(responses, appointments.getTotalElements(),appointments.getPageable());
//    }

}
