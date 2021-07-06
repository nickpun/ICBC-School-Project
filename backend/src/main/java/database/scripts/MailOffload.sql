USE `PandemicPalRDB`;

CREATE TABLE `ArchiveMail` (
  `mail_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varhcar(72) NOT NULL,
  `sender` varchar(45) NOT NULL,
  `return_address` varchar(100) NOT NULL,
  `arrival_date` varchar(45) NOT NULL,
  `was_actioned` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`mail_id`),
  KEY `Mail_ibfk_1_idx` (`user_id`),
  CONSTRAINT `Mail_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Employees` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO ArchiveMail 
	(SELECT M.mail_id, M.user_id, M.sender, M.return_address, M.arrival_date, M.was_actioned 
	FROM Mail M, MailRequests MR
	WHERE M.mail_id = MR.mail_id 
	AND M.was_actioned = 1 
	AND MR.request_status = 'closed');
	
DELETE FROM Mail WHERE (Select M.mail_id FROM Mail M, MailRequests MR WHERE M.mail_id = MR.mail_id AND M.was_actioned = 1 AND MR.request_status = 'closed');