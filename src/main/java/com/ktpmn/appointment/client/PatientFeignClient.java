package com.ktpmn.appointment.client;

import com.ktpmn.appointment.client.dto.PatientFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "patient-service", url = "${feign.client.config.patient-service.url}")
public interface PatientFeignClient {

    @GetMapping("/api/patients/page/{page}/limit/{limit}")
    List<PatientFeignResponse> getPatientsPaginated(
            @PathVariable("page") int page,
            @PathVariable("limit") int limit);

}