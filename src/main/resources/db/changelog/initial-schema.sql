--liquibase formatted sql

--changeset subscription-allocation-service:1
CREATE TABLE test (
  id INT IDENTITY,
  data VARBINARY(MAX)
);
