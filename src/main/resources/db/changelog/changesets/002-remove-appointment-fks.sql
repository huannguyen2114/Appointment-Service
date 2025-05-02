ALTER TABLE "appointment" DROP CONSTRAINT IF EXISTS fk_appointment_doctor;
ALTER TABLE "appointment" DROP CONSTRAINT IF EXISTS fk_appointment_patient;
ALTER TABLE "shift" DROP CONSTRAINT IF EXISTS shift_staff_id_fkey;
ALTER TABLE "patient"
ADD COLUMN "phone_number" VARCHAR(15);