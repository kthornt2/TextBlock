CREATE TABLE `textblockdb`.`guardian` ( `guar_imei` INT(50) NOT NULL ,
`guar_email` VARCHAR(50) NOT NULL ,
`guar_password` VARCHAR(30) NOT NULL ,
`guar_first_name` TEXT NOT NULL ,
`guar_last_name` TEXT NOT NULL ,
`guar_street_address` VARCHAR(50) NOT NULL ,
`guar_zip` INT(10) NOT NULL ,
PRIMARY KEY (`guar_imei`),
UNIQUE (`guar_email`)) ENGINE = InnoDB;

CREATE TABLE `textblockdb`.`child` ( `child_imei` INT(50) NOT NULL ,
`guar_imei` INT(50) NOT NULL ,
`child_email` VARCHAR(50) NOT NULL ,
`child_first_name` TEXT NOT NULL ,
`child_last_name` TEXT NOT NULL ,
PRIMARY KEY (`child_imei`),
UNIQUE (`guar_imei`),
UNIQUE (`child_email`)) ENGINE = InnoDB;

CREATE TABLE `textblockdb`.`features` ( `features_id` INT(50) NOT NULL AUTO_INCREMENT ,
`guar_imei` INT(50) NOT NULL ,
`child_imei` INT NOT NULL ,
`features_speed` INT(2) NOT NULL COMMENT 'min speed app starts block' ,
`features_time` INT(5) NOT NULL COMMENT 'how long in seconds until app unblocks' ,
`features_autounlock` BOOLEAN NOT NULL ,
PRIMARY KEY (`features_id`),
UNIQUE (`guar_imei`),
UNIQUE (`child_imei`)) ENGINE = InnoDB;