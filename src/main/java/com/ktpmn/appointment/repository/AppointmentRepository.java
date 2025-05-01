package com.ktpmn.appointment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktpmn.appointment.model.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

}