CREATE DATABASE IF NOT EXISTS test;

USE test;

DROP TABLE IF EXISTS part;
CREATE TABLE `test`.`part` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `need` TINYINT NOT NULL,
  `count` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB,
DEFAULT CHARACTER SET = utf8,
COLLATE = utf8_unicode_ci;

INSERT INTO `test`.`part` (`name`, `need`, `count`) VALUES
	('материнская плата', 1, 5),
	('звуковая карта', 0, 7),
	('процессор', 1, 2),
	('подсветка корпуса', 0, 6),
	('HDD диск', 1, 3),
	('корпус', 1, 10),
	('оперативная память', 1, 13),
	('SSD диск', 0, 7),
	('куллер на процессор', 1, 35),
	('водяное охлаждение', 0, 1),
	('ТВ-тюнер', 0, 0),
	('карта видеозахвата', 0, 0),
	('монитор', 1, 17),
	('клавиатура', 1, 51),
	('мышка', 1, 47),
	('джойстик', 0, 2),
	('колонки', 0, 5),
	('наушники', 0, 7),
	('салазки для SSD/HDD', 0, 10),
	('роутер', 0, 8)
;
