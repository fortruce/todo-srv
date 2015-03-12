CREATE SEQUENCE lid_serial;

CREATE TABLE lists (
       id    integer PRIMARY KEY DEFAULT nextval('lid_serial'),
       name varchar(50)
);
