DROP DATABASE IF EXISTS ui_local_storage;

CREATE DATABASE ui_local_storage;

USE ui_local_storage;

CREATE TABLE user_prefs (
id INT NOT NULL AUTO_INCREMENT, 
user_id INT NOT NULL, 
lang VARCHAR(5) NOT NULL DEFAULT "en", 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE saved_configurations (
id INT NOT NULL AUTO_INCREMENT, 
user_id INT NOT NULL, 
region_id INT, 
country_id INT, 
start_year_id INT,
finish_year_id INT, 
first_line_statistic_id INT NOT NULL, 
second_line_statistic_id INT NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE messages (id INT NOT NULL AUTO_INCREMENT,
message VARCHAR(25), 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

INSERT INTO messages (message) VALUES 
("MW_MAIN_TITLE"), 
("LP_SUBTITLE"), 
("LP_USERNAME_LABEL"), 
("LP_PASSWORD_LABEL"), 
("LP_LOGIN_BUTTON_LABEL"), 
("LP_RESET_BUTTON_LABEL"), 
("FP_AUTHOR_SIGN"), 
("FP_AUTHOR_CONTACT");

SOURCE langs/en.sql;
SOURCE langs/uk.sql;
