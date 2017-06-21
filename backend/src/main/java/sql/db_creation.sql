DROP DATABASE IF EXISTS statistical_storage;
CREATE DATABASE statistical_storage;
USE statistical_storage;
CREATE TABLE year (id INT NOT NULL AUTO_INCREMENT, year INT, PRIMARY KEY (id));
CREATE TABLE region (id INT NOT NULL AUTO_INCREMENT, region_code VARCHAR(100) NOT NULL, PRIMARY KEY (id));
CREATE TABLE country (id INT NOT NULL AUTO_INCREMENT,
region_id INT NOT NULL,
country_code VARCHAR(100) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (region_id)
        REFERENCES region(id)
        ON DELETE CASCADE
);

CREATE TABLE sex (id INT NOT NULL AUTO_INCREMENT, sex VARCHAR(30), PRIMARY KEY (id));

CREATE TABLE life_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
sex_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (year_id)
        REFERENCES year(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (country_id)
        REFERENCES country(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (sex_id)
        REFERENCES sex(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE tobacco_usage_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (year_id)
        REFERENCES year(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (country_id)
        REFERENCES country(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE sickness_statistic (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (year_id)
        REFERENCES year(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (country_id)
        REFERENCES country(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE death_by_environment_statistics (id INT NOT NULL AUTO_INCREMENT,
year_id INT NOT NULL,
country_id INT NOT NULL,
measurenment DECIMAL(10, 5) NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (year_id)
        REFERENCES year(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (country_id)
        REFERENCES country(id)
        ON UPDATE CASCADE ON DELETE CASCADE
);