CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS patient (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    date_of_birth DATE NOT NULL,
    registered_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data if the table is empty
INSERT INTO patient (id, email, name, address, date_of_birth, registered_date, created_at, updated_at) VALUES
  (uuid_generate_v4(), 'john.doe@example.com', 'John Doe', '123 Main St, New York, NY', '1990-05-15', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'jane.smith@example.com', 'Jane Smith', '456 Elm St, Los Angeles, CA', '1985-08-22', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'michael.johnson@example.com', 'Michael Johnson', '789 Oak St, Chicago, IL', '1992-03-10', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'emily.williams@example.com', 'Emily Williams', '321 Pine St, Houston, TX', '1988-12-30', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'david.brown@example.com', 'David Brown', '654 Cedar St, Miami, FL', '1995-07-18', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'sarah.miller@example.com', 'Sarah Miller', '987 Maple St, Seattle, WA', '1983-11-25', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  (uuid_generate_v4(), 'chris.wilson@example.com', 'Chris Wilson', '741 Birch St, Denver, CO', '1998-06-05', CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);