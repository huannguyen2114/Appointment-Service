CREATE TABLE "appointment" (
    "id" uuid PRIMARY KEY,
    "patient_id" uuid,
    "doctor_id" uuid,
    "appointment_status" varchar(50),
    "description" text,
    "appointment_type" varchar(50),
    "from_date" timestamptz,
    -- Recommended: Use timestamptz
    "to_date" timestamptz,
    -- Recommended: Use timestamptz
    "ordinal_number" serial,
    "created_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
    -- Recommended: Use timestamptz and add default
    "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP -- Recommended: Use timestamptz and add default
);
-- Create staff table BEFORE shift
CREATE TABLE "staff" (
    "id" uuid PRIMARY KEY,
    "first_name" varchar(100),
    "last_name" varchar(100),
    "role" varchar(50),
    "dob" date,
    -- Recommended: Use date for DOB
    "certification_id" varchar(20) UNIQUE,
    "sex" varchar(50),
    "phone_number" varchar(15) UNIQUE,
    -- Recommended: Increase length
    "email" varchar(100) UNIQUE,
    "citizen_id" varchar(12) UNIQUE,
    "created_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
    -- Recommended: Use timestamptz and add default
    "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP -- Recommended: Use timestamptz and add default
);
CREATE TABLE "shift" (
    "id" uuid PRIMARY KEY,
    "shift_name" varchar(50),
    -- Recommended: Use varchar(50) instead of text
    "from_time" time,
    -- Recommended: Use time type and rename
    "to_time" time,
    -- Recommended: Use time type and rename
    "staff_id" uuid,
    "created_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
    -- Recommended: Use timestamptz and add default
    "updated_at" timestamptz DEFAULT CURRENT_TIMESTAMP,
    -- Recommended: Use timestamptz and add default
    -- Define the correct foreign key here
    FOREIGN KEY ("staff_id") REFERENCES "staff" ("id")
);
CREATE TABLE "patient" (
    "id" uuid PRIMARY KEY,
    "first_name" varchar(100),
    "last_name" varchar(100), -- Consider adding created_at/updated_at
    "phone_number" varchar(15) UNIQUE,
);
-- Foreign keys for appointment table
ALTER TABLE "appointment"
ADD CONSTRAINT fk_appointment_doctor FOREIGN KEY ("doctor_id") REFERENCES "staff" ("id");
-- Added constraint name
ALTER TABLE "appointment"
ADD CONSTRAINT fk_appointment_patient FOREIGN KEY ("patient_id") REFERENCES "patient" ("id");
-- Added constraint name
-- REMOVE the incorrect ALTER TABLE statement for staff referencing shift:
-- ALTER TABLE "staff"
-- ADD FOREIGN KEY ("id") REFERENCES "shift" ("staff_id");