CREATE DATABASE IF NOT EXISTS mydb;

USE mydb;

CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON mydb.* TO 'user'@'%';
FLUSH PRIVILEGES;

CREATE TABLE IF NOT EXISTS test_entity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    column1 VARCHAR(255),
    column2 VARCHAR(255)
);

INSERT INTO test_entity (column1, column2) VALUES
  ('value1', 'value2'),
  ('value3', 'value4'),
  ('value5', 'value6');
