-- Ensure the core table exists to avoid runtime errors during first inserts
CREATE TABLE IF NOT EXISTS public.users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  role VARCHAR(255),
  specialty VARCHAR(255),
  registration_number VARCHAR(255),
  formation TEXT,
  experience TEXT,
  photo_url VARCHAR(1024),
  lgpd_consent BOOLEAN NOT NULL DEFAULT FALSE,
  active BOOLEAN NOT NULL DEFAULT TRUE
);

-- Ensure relation table exists
CREATE TABLE IF NOT EXISTS public.user_professionals (
  student_id UUID NOT NULL REFERENCES public.users(id),
  professional_id UUID NOT NULL REFERENCES public.users(id),
  PRIMARY KEY (student_id, professional_id)
);

-- Ensure exercises table exists for the worker
CREATE TABLE IF NOT EXISTS public.exercises (
    id UUID PRIMARY KEY,
    external_id VARCHAR(255),
    name VARCHAR(255) NOT NULL,
    primary_muscles VARCHAR(255),
    equipment VARCHAR(255),
    image_url TEXT,
    video_url TEXT,
    last_synced_at TIMESTAMP,
    updated_at TIMESTAMP,
    source VARCHAR(255) DEFAULT 'ascendapi'
);
