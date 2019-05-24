CREATE KEYSPACE IF NOT EXISTS testkeyspace
  WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1'};

use testkeyspace;

CREATE TABLE IF NOT EXISTS vehicles (
    Active text,
    Vehicle_License_Number text,
    Name text,
    License_Type text,
    Expiration_Date date,
    Permit_License_Number text,
    DMV_License_Plate_Number text,
    Vehicle_VIN_Number text,
    Wheelchair_Accessible text,
    Certification_Date date,
    Hack_Up_Date date,
    Vehicle_Year text,
    Base_Number text,
    Base_Name text,
    Base_Type text,
    VEH text,
    Base_Telephone_Number text,
    Website text,
    Base_Address text,
    Reason text,
    Order_Date date,
    Last_Date_Updated date,
    Last_Time_Updated date
    PRIMARY KEY (Vehicle_VIN_Number)
);

