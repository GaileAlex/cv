create
database "test-db"
    with owner cv;

DROP ROLE IF EXISTS cv;

CREATE
USER cv WITH ENCRYPTED PASSWORD 'pass';
