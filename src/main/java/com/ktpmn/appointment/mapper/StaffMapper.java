package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper
public interface StaffMapper {
    StaffCreateResponse toStaffCreateResponse(Staff staff);

    Staff toStaff(StaffCreateRequest staffCreateRequest);

    StaffResponse toStaffResponse(Staff staff);

    @Mapping(source = "content", target = "listData")
    @Mapping(source = "pageable.pageNumber", target = "pageNumber")
    @Mapping(source = "numberOfElements", target = "numberOfElements")
    ListResponse<StaffResponse> toListStaffResponse(Page<Staff> pageStaff);

}
