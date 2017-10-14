DROP DATABASE IF EXISTS statistical_storage;
CREATE DATABASE statistical_storage;
USE statistical_storage;
CREATE TABLE year (id INT NOT NULL AUTO_INCREMENT, year INT, PRIMARY KEY (id), INDEX year_ind(id))  ENGINE = InnoDB;
CREATE TABLE region (id INT NOT NULL AUTO_INCREMENT, 
region_code VARCHAR(100) NOT NULL, PRIMARY KEY (id), 
INDEX region_ind(id))  ENGINE = InnoDB;
CREATE TABLE country (id INT NOT NULL AUTO_INCREMENT,
region_id INT NOT NULL,
country_code VARCHAR(100) NOT NULL,
PRIMARY KEY (id), 
INDEX country_ind(id)
)  ENGINE = InnoDB;

CREATE TABLE sex (id INT NOT NULL AUTO_INCREMENT, sex VARCHAR(30), PRIMARY KEY (id), INDEX sex_ind(id))  ENGINE = InnoDB;

CREATE TABLE life_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
sex_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id)
)  ENGINE = InnoDB;

CREATE UNIQUE INDEX life_statistic_uq ON life_statistic (year_id, country_id, sex_id, measurenment);

CREATE TABLE tobacco_usage_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id)
)  ENGINE = InnoDB;

CREATE UNIQUE INDEX tobacco_usage_statistic_uq ON tobacco_usage_statistic (year_id, country_id, measurenment);

CREATE TABLE sickness_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id)
)  ENGINE = InnoDB;

CREATE UNIQUE INDEX sickness_statistic_uq ON sickness_statistic (year_id, country_id, measurenment);

CREATE TABLE death_by_environment_statistics (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id)
)  ENGINE = InnoDB;

CREATE UNIQUE INDEX death_by_environment_statistics_uq ON death_by_environment_statistics (year_id, country_id, measurenment);

CREATE TABLE statistics (
id INT NOT NULL AUTO_INCREMENT,
table_name VARCHAR(100) NOT NULL, 
statistic_summary VARCHAR(255) NOT NULL, 
PRIMARY KEY (id)
)  ENGINE = InnoDB;

CREATE UNIQUE INDEX statistics_uq ON statistics (table_name, statistic_summary);
