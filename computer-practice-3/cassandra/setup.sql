CREATE KEYSPACE IF NOT EXISTS testkeyspace
  WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1'};

use testkeyspace;

CREATE TABLE IF NOT EXISTS vehicles (
  active text,
    vehicle_license_number text,
    name text,
    license_type text,
    expiration_date text,
    permit_license_number text,
    dmv_license_plate_number text,
    vehicle_vin_number text,
    wheelchair_accessible text,
    certification_date text,
    hack_up_date text,
    vehicle_year text,
    base_number text,
    base_name text,
    base_type text,
    veh text,
    base_telephone_number text,
    website text,
    base_address text,
    reason text,
    order_date text,
    last_date_updated text,
    last_time_updated text,
    PRIMARY KEY (vehicle_vin_number)
);

