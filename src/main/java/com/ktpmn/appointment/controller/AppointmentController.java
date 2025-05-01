package com.ktpmn.appointment.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.service.AppointmentService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/")
@Slf4j
@RestController
@AllArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/")
    public List<Appointment> helloWorldString() {
        return appointmentService.helString();
    }

}