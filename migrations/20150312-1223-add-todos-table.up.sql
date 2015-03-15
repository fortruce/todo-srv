CREATE SEQUENCE tid_serial;

CREATE TABLE todos (
       name varchar(50),
       id integer PRIMARY KEY DEFAULT nextval('tid_serial'),
       lid integer REFERENCES lists(id),
       updated_at timestamp DEFAULT now(),
       created_at timestamp DEFAULT now()
);
