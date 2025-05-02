package com.ktpmn.appointment.scheduler;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j // Optional: for logging
public class AppointmentStatusUpdater {

    private final AppointmentRepository appointmentRepository;

    // Define statuses that should be considered 'pending' and thus eligible for
    // expiration
    private static final List<AppointmentStatus> PENDING_STATUSES = Arrays.asList(
            AppointmentStatus.WAITING,
            AppointmentStatus.CONFIRMED
    // Add any other statuses that mean the appointment hasn't been
    // completed/cancelled yet
    );

    /**
     * Runs periodically (e.g., every hour) to check for appointments
     * whose start time has passed but whose status is still pending,
     * and updates their status to EXPIRED.
     * Cron expression: second, minute, hour, day of month, month, day(s) of week
     * "0 0 * * * *" = Run at the beginning of every hour
     */
    @Scheduled(cron = "0 0 * * * *") // Run every hour at minute 0
    // Alternatively, use fixedRate or fixedDelay:
    // @Scheduled(fixedRate = 3600000) // Run every 3600000 ms (1 hour)
    @Transactional // Ensure the operation is atomic
    public void expirePastPendingAppointments() {
        OffsetDateTime now = OffsetDateTime.now();
        log.info("Running scheduled task to expire appointments at {}", now);

        List<Appointment> appointmentsToExpire = appointmentRepository.findPastPendingAppointments(now,
                PENDING_STATUSES);

        if (appointmentsToExpire.isEmpty()) {
            log.info("No pending past appointments found to expire.");
            return;
        }

        log.info("Found {} appointments to mark as EXPIRED.", appointmentsToExpire.size());

        for (Appointment appointment : appointmentsToExpire) {
            log.debug("Expiring appointment ID: {}, Patient: {}, Doctor: {}, FromDate: {}",
                    appointment.getId(),
                    appointment.getPatient().getId(), // Assuming Patient has getId()
                    appointment.getDoctor().getId(), // Assuming Staff has getId()
                    appointment.getFromDate());
            appointment.setAppointmentStatus(AppointmentStatus.EXPIRED);
        }

        // Save all updated appointments in batch
        appointmentRepository.saveAll(appointmentsToExpire);

        log.info("Successfully marked {} appointments as EXPIRED.", appointmentsToExpire.size());
    }
}