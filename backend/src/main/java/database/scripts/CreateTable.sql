CREATE DATABASE `PandemicPalRDB` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `PandemicPalRDB`;

CREATE TABLE `Employees` (
    `user_id` varchar(72) NOT NULL,
    `first_name` varchar(32) NOT NULL,
    `last_name` varchar(32) NOT NULL,
    `dept` varchar(32) DEFAULT NULL,
    `is_admin` tinyint(1) NOT NULL DEFAULT '0',
    `is_system_admin` tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Buildings` (
  `building_id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(64) NOT NULL,
  `building_name` varchar(32) NOT NULL,
  PRIMARY KEY (`building_id`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Floors` (
  `floor_id` int NOT NULL AUTO_INCREMENT,
  `building_id` int NOT NULL,
  `storey` int NOT NULL,
  `floor_plan` text,
  PRIMARY KEY (`floor_id`),
  KEY `building_id` (`building_id`),
  CONSTRAINT `Floors_ibfk_1` FOREIGN KEY (`building_id`) REFERENCES `Buildings` (`building_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Hubs` (
  `hub_id` int NOT NULL AUTO_INCREMENT,
  `floor_id` int NOT NULL,
  `location` point NOT NULL,
  PRIMARY KEY (`hub_id`),
  KEY `Hubs_ibfk_1` (`floor_id`),
  CONSTRAINT `Hubs_ibfk_1` FOREIGN KEY (`floor_id`) REFERENCES `Floors` (`floor_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=501 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Desks` (
  `desk_id` int NOT NULL AUTO_INCREMENT,
  `desk_no` int DEFAULT NULL,
  `hub_id` int NOT NULL,
  PRIMARY KEY (`desk_id`),
  KEY `hub_id` (`hub_id`),
  CONSTRAINT `Desks_ibfk_3` FOREIGN KEY (`hub_id`) REFERENCES `Hubs` (`hub_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=687 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Reservations` (
    `user_id` varchar(72) NOT NULL,
    `desk_id` int NOT NULL,
    `from_date` timestamp NOT NULL,
    `to_date` timestamp NOT NULL,
    PRIMARY KEY (`user_id`,`desk_id`,`from_date`,`to_date`),
    KEY `Reservations_ibfk_1_idx` (`desk_id`),
    KEY `Reservations_ibfk_1_idx1` (`user_id`),
    CONSTRAINT `Reservations_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `Reservations_ibfk_2` FOREIGN KEY (`desk_id`) REFERENCES `Desks` (`desk_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_title` varchar(45) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Posts` (
    `post_id` int NOT NULL AUTO_INCREMENT,
    `user_id` varchar(72) NOT NULL,
    `category_id` int DEFAULT NULL,
    `post_title` varchar(45) NOT NULL,
    `post_date` timestamp NULL DEFAULT NULL,
    `post_value` varchar(300) DEFAULT NULL,
    PRIMARY KEY (`post_id`),
    KEY `Posts_ibfk_2_idx` (`category_id`),
    KEY `Posts_ibfk_1_idx` (`user_id`),
    CONSTRAINT `Posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `Posts_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `Categories` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Comments` (
    `comment_id` int NOT NULL AUTO_INCREMENT,
    `user_id` varchar(72) NOT NULL,
    `post_id` int NOT NULL,
    `comment_date` timestamp NOT NULL,
    `comment_value` varchar(300) NOT NULL,
    PRIMARY KEY (`comment_id`),
    KEY `Comments_ibfk_2_idx` (`post_id`),
    KEY `Comments_ibfk_1_idx` (`user_id`),
    CONSTRAINT `Comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `Comments_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `Posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Mail` (
    `mail_id` int NOT NULL AUTO_INCREMENT,
    `user_id` varchar(72) NOT NULL,
    `sender` varchar(45) NOT NULL,
    `return_address` varchar(100) NOT NULL,
    `arrival_date` varchar(45) NOT NULL,
    `was_actioned` tinyint NOT NULL DEFAULT '0',
    PRIMARY KEY (`mail_id`),
    KEY `Mail_ibfk_1_idx` (`user_id`),
    CONSTRAINT `Mail_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=357 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `MailRequests` (
    `request_id` int NOT NULL AUTO_INCREMENT,
    `mail_id` int NOT NULL,
    `user_id` varchar(72) NOT NULL,
    `contact` varchar(45) NOT NULL,
    `location` varchar(45) DEFAULT NULL,
    `request_status` varchar(45) NOT NULL DEFAULT 'newRequest',
    `request_date` timestamp NOT NULL,
    `request_type` varchar(32) NOT NULL,
    PRIMARY KEY (`request_id`),
    KEY `MailRequest_ibfk_1_idx` (`mail_id`),
    KEY `MailReqeust_ibfk_2_idx` (`user_id`),
    CONSTRAINT `MailReqeust_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `MailRequest_ibfk_1` FOREIGN KEY (`mail_id`) REFERENCES `Mail` (`mail_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2755 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `MailComments` (
    `comment_id` int NOT NULL AUTO_INCREMENT,
    `request_id` int NOT NULL,
    `user_id` varchar(72) NOT NULL,
    `comment_value` varchar(250) NOT NULL,
    `comment_date` timestamp NOT NULL,
    PRIMARY KEY (`comment_id`),
    KEY `MailComments_ibfk_1` (`request_id`),
    KEY `MailComments_ibfk_2_idx` (`user_id`),
    CONSTRAINT `MailComments_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `MailRequests` (`request_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `MailComments_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2888 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

