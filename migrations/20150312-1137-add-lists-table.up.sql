CREATE SEQUENCE lid_serial;

CREATE TABLE lists (
       name varchar(50),
       id    integer PRIMARY KEY DEFAULT nextval('lid_serial'),
       updated_at timestamp DEFAULT now(),
       create_at timestamp DEFAULT now()
);
