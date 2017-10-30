CREATE TABLE uk_statistics (
id INT NOT NULL AUTO_INCREMENT,
statistic_id INT NOT NULL, 
value VARCHAR(30) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE uk_sex (
id INT NOT NULL AUTO_INCREMENT,
sex_id INT NOT NULL, 
value VARCHAR(30) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE uk_region (
id INT NOT NULL AUTO_INCREMENT,
region_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE uk_country (
id INT NOT NULL AUTO_INCREMENT,
country_id INT NOT NULL, 
value VARCHAR(100) NOT NULL, 
PRIMARY KEY(id)
)  ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE uk_messages (id INT NOT NULL AUTO_INCREMENT,
message_id INT NOT NULL, 
label VARCHAR(500), 
PRIMARY KEY(id))  ENGINE = InnoDB DEFAULT CHARSET=utf8;

INSERT INTO uk_messages (message_id, label) VALUES 
((SELECT id FROM messages WHERE message = "MW_MAIN_TITLE"), "Статистичний Аналіз Даних ВООЗ"), 
((SELECT id FROM messages WHERE message = "LP_SUBTITLE"), "Вхід"), 
((SELECT id FROM messages WHERE message = "LP_USERNAME_LABEL"), "Реєстраційне Ім'я"),
((SELECT id FROM messages WHERE message = "LP_PASSWORD_LABEL"), "Пароль"),
((SELECT id FROM messages WHERE message = "LP_LOGIN_BUTTON_LABEL"), "Увійти"), 
((SELECT id FROM messages WHERE message = "LP_RESET_BUTTON_LABEL"), "Скинути"), 
((SELECT id FROM messages WHERE message = "WS_SUBTITLE"), "Робочий Кабінат"), 
((SELECT id FROM messages WHERE message = "WS_STATISTIC_BTN_LABEL"), "Показати Статистику"), 
((SELECT id FROM messages WHERE message = "WS_CHART_LEGEND_LABEL"), "Вибір Статистик"), 
((SELECT id FROM messages WHERE message = "WS_FIRST_CHART_LABEL"), "Статистика першого ряду"), 
((SELECT id FROM messages WHERE message = "WS_SECOND_CHART_LABEL"), "Статистика другого ряду"), 
((SELECT id FROM messages WHERE message = "WS_GEO_DIM_LABEL"), "Географічні виміри запиту"), 
((SELECT id FROM messages WHERE message = "WS_REGION_LABEL"), "Географічний регіон"), 
((SELECT id FROM messages WHERE message = "WS_COUNTRY_LABEL"), "Країна"), 
((SELECT id FROM messages WHERE message = "WS_SEX_LEGEND"), "Стать"), 
((SELECT id FROM messages WHERE message = "WS_YEAR_LABEL"), "Рік"), 
((SELECT id FROM messages WHERE message = "WS_CORRELATION_LABEL"), "Кореляція"), 
((SELECT id FROM messages WHERE message = "WS_YEARS_LEGEND"), "Часовий діапазон в роках"), 
((SELECT id FROM messages WHERE message = "WS_STAT_TITLE_LABEL"), "Графік Статистик"),
((SELECT id FROM messages WHERE message = "WS_STAT_LOSUNG_LABEL"), "Показники Статистик"),
((SELECT id FROM messages WHERE message = "WS_LEGEND_TITLE_LABEL"), "Умовні позначення"),
((SELECT id FROM messages WHERE message = "FP_AUTHOR_SIGN"), "&copy; Павло Мриглоцький, студент Національного Університету 'Львівська Політехніка', 2017"), 
((SELECT id FROM messages WHERE message = "FP_AUTHOR_CONTACT"), "Контактна інформація");
