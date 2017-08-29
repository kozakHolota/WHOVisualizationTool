DROP DATABASE IF EXISTS statistic_app_account_db;
CREATE DATABASE statistic_app_account_db;
USE statistic_app_account_db;

CREATE TABLE user (id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(255) NOT NULL,
password VARCHAR(255),
is_admin BOOLEAN DEFAULT 0,
PRIMARY KEY(id),
INDEX(id));

CREATE TABLE hash_table (id INT NOT NULL AUTO_INCREMENT,
user_id INT NOT NULL,
cred_hash VARCHAR(255), 
due_date DATETIME(5), 
PRIMARY KEY(id),
FOREIGN KEY (user_id)
        REFERENCES user (id) ON UPDATE CASCADE ON DELETE CASCADE);

INSERT INTO user (username, password, is_admin) VALUES ("admin", PASSWORD("!l!k$c$v$l0p$"), 1);
INSERT INTO user (username, password, is_admin) VALUES ("user", PASSWORD("!l!k$c$v$l0p$"), 0);