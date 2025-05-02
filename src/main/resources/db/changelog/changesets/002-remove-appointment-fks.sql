-- liquibase formatted sql

-- changeset yourname:2 dbms:postgresql
-- comment: Remove foreign key constraints from appointment table
ALTER TABLE "appointment"
DROP CONSTRAINT IF EXISTS fk_appointment_doctor;

ALTER TABLE "appointment"
DROP CONSTRAINT IF EXISTS fk_appointment_patient;