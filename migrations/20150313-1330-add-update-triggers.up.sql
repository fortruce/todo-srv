CREATE FUNCTION set_updated_at()
       RETURNS TRIGGER AS $$
       BEGIN
        NEW.updated_at := now();
        RETURN NEW;
       END;
       $$ LANGUAGE PLPGSQL;

CREATE TRIGGER lists_updated_at
       BEFORE UPDATE ON
           lists
       FOR EACH ROW EXECUTE PROCEDURE
           set_updated_at();

CREATE TRIGGER todos_updated_at
       BEFORE UPDATE ON
           todos
       FOR EACH ROW EXECUTE PROCEDURE
           set_updated_at();
