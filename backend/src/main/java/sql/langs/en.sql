CREATE TABLE en_statistics (
id INT NOT NULL AUTO_INCREMENT,
statistic_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE en_sex (
id INT NOT NULL AUTO_INCREMENT,
sex_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE en_region (
id INT NOT NULL AUTO_INCREMENT,
region_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE en_country (
id INT NOT NULL AUTO_INCREMENT,
country_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB;

CREATE TABLE en_messages (id INT NOT NULL AUTO_INCREMENT,
message_id INT NOT NULL, 
label VARCHAR(500), 
PRIMARY KEY(id))  ENGINE = InnoDB;

INSERT INTO en_messages (message_id, label) VALUES 
((SELECT id FROM messages WHERE message = "MW_MAIN_TITLE"), "Statistical analysis of WHO data"), 
((SELECT id FROM messages WHERE message = "LP_SUBTITLE"), "Login"), 
((SELECT id FROM messages WHERE message = "LP_USERNAME_LABEL"), "User Name"),
((SELECT id FROM messages WHERE message = "LP_PASSWORD_LABEL"), "Password"),
((SELECT id FROM messages WHERE message = "LP_LOGIN_BUTTON_LABEL"), "Login"), 
((SELECT id FROM messages WHERE message = "LP_RESET_BUTTON_LABEL"), "Reset"), 
((SELECT id FROM messages WHERE message = "WS_SUBTITLE"), "Workspace"), 
((SELECT id FROM messages WHERE message = "WS_STATISTIC_BTN_LABEL"), "Show Statistics"), 
((SELECT id FROM messages WHERE message = "WS_CHART_LEGEND_LABEL"), "Statistic Topics"), 
((SELECT id FROM messages WHERE message = "WS_FIRST_CHART_LABEL"), "Statistic of the first row"), 
((SELECT id FROM messages WHERE message = "WS_SECOND_CHART_LABEL"), "Statistic of the second row"), 
((SELECT id FROM messages WHERE message = "WS_GEO_DIM_LABEL"), "Geographic Dimensions"), 
((SELECT id FROM messages WHERE message = "WS_REGION_LABEL"), "Geographic Region"), 
((SELECT id FROM messages WHERE message = "WS_COUNTRY_LABEL"), "Country"), 
((SELECT id FROM messages WHERE message = "WS_SEX_LEGEND"), "Sex"), 
((SELECT id FROM messages WHERE message = "WS_YEARS_LEGEND"), "Years Range"), 
((SELECT id FROM messages WHERE message = "WS_YEAR_LABEL"), "Year"), 
((SELECT id FROM messages WHERE message = "WS_CORRELATION_LABEL"), "Correlation"), 
((SELECT id FROM messages WHERE message = "WS_STAT_TITLE_LABEL"), "Statistical Chart"), 
((SELECT id FROM messages WHERE message = "WS_STAT_LOSUNG_LABEL"), "Statistical Labels"), 
((SELECT id FROM messages WHERE message = "WS_LEGEND_TITLE_LABEL"), "Legend"),
((SELECT id FROM messages WHERE message = "FP_AUTHOR_SIGN"), "&copy; Pavlo Mryhlotskyi, student of Ukrainian National University 'Lviv Polytechnic', Lviv, 2017"), 
((SELECT id FROM messages WHERE message = "FP_AUTHOR_CONTACT"), "Contact Information");
