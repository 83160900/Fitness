-- Ensure the core table exists to avoid runtime errors during first inserts
CREATE TABLE IF NOT EXISTS users (
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
