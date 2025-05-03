ALTER TABLE "appointment" DROP CONSTRAINT IF EXISTS fk_appointment_doctor;
ALTER TABLE "appointment" DROP CONSTRAINT IF EXISTS fk_appointment_patient;
ALTER TABLE "shift" DROP CONSTRAINT IF EXISTS shift_staff_id_fkey;
ALTER TABLE "patient"
ADD COLUMN "phone_number" VARCHAR(15);
ALTER TABLE "patient"
ADD COLUMN "created_at" timestamptz DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE "patient"
ADD COLUMN "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP;

--
-- "created_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
--     -- Recommended: Use timestamptz and add default
--     "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP,