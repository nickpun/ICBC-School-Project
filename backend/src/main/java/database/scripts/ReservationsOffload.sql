USE `PandemicPalRDB`;

CREATE TABLE `ArchiveReservations` (
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


INSERT INTO ArchiveReservations (SELECT * FROM Reservations WHERE to_date < CURRENT_TIMESTAMP);

DELETE FROM Reservations WHERE to_date < CURRENT_TIMESTAMP;