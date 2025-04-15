CREATE DATABASE joobyTest;

-- This really should not be here, but for this workshop it makes things easy
CREATE USER 'jooby' IDENTIFIED BY 'yrgoP4ssword';

GRANT CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES ON joobyTest.* TO 'jooby';
