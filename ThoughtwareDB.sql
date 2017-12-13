# --------------------------------------------
# SQL script to create and populate the tables
# for the Thoughtware Database (ThoughtwareDB)
# Created by Casey Butenhoff
# --------------------------------------------

/*
Tables to be dropped must be listed in a logical order based on dependency.
UserFile and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS UserFile, UserPhoto, Message, Activity, Milestone, ProjectFile, UserProjectAssociation, Project, User;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    hashed_password VARCHAR (64) NOT NULL,
    first_name VARCHAR (32) NOT NULL,
    middle_name VARCHAR (32),
    last_name VARCHAR (32) NOT NULL,
    address1 VARCHAR (128) NOT NULL,
    address2 VARCHAR (128),
    city VARCHAR (64) NOT NULL,
    state VARCHAR (2) NOT NULL,
    zipcode VARCHAR (10) NOT NULL, /* e.g., 24060-1804 */
    security_question INT NOT NULL, /* Refers to the number of the selected security question */
    security_answer VARCHAR (128) NOT NULL,
    email VARCHAR (128) NOT NULL,
    google_image_url VARCHAR (128) NOT NULL,
    PRIMARY KEY (id)
);

/* The UserPhoto table contains attributes of interest of a user's photo. */
CREATE TABLE UserPhoto
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       extension ENUM('jpeg', 'jpg', 'png', 'gif') NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/*
The UserFile table contains attributes of interest of a user's uploaded file.
Note: We cannot name the table as File since it is a reserved word in MySQL.
*/
CREATE TABLE UserFile
(
       id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
       filename VARCHAR (256) NOT NULL,
       user_id INT UNSIGNED,
       FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

CREATE TABLE Project
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR (256) NOT NULL,
    hashed_password VARCHAR (64) NOT NULL,
    rss_key VARCHAR (64) NOT NULL
);

DELIMITER $$
CREATE TRIGGER `project_after_insert` AFTER INSERT
	ON `Project`
	FOR EACH ROW BEGIN
                -- In an INSERT trigger, only NEW.col_name can be used
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'INSERT_PROJECT',
			concat('Created project ', NEW.name),
			NEW.id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `project_after_update` AFTER UPDATE
	ON `Project`
	FOR EACH ROW BEGIN
                -- In a UPDATE trigger, you can use OLD.col_name to refer to the columns of a row before it is updated and NEW.col_name to refer to the columns of the row after it is updated.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'UPDATE_PROJECT',
			concat('Updated project ', NEW.name),
			NEW.id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `project_after_delete` AFTER DELETE
	ON `Project`
	FOR EACH ROW BEGIN
                -- In a DELETE trigger, only OLD.col_name can be used; there is no new row.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'DELETE_PROJECT',
			concat('Deleted project ', OLD.name),
			OLD.id
		);
    END$$
DELIMITER ;

CREATE TABLE UserProjectAssociation
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER `user_project_association_after_insert` AFTER INSERT
	ON `UserProjectAssociation`
	FOR EACH ROW BEGIN
                -- In an INSERT trigger, only NEW.col_name can be used
                SELECT `username` FROM User WHERE id = NEW.user_id INTO @username;
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'INSERT_USER_PROJECT_ASSOCIATION',
			concat('User ', @username, ' joined project'),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `user_project_association_after_update` AFTER UPDATE
	ON `UserProjectAssociation`
	FOR EACH ROW BEGIN
                -- In a UPDATE trigger, you can use OLD.col_name to refer to the columns of a row before it is updated and NEW.col_name to refer to the columns of the row after it is updated.
                SELECT `username` FROM User WHERE id = NEW.user_id INTO @username;
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'UPDATE_USER_PROJECT_ASSOCIATION',
			concat('User ', @username, ' project membership changed'),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `user_project_association_after_delete` AFTER DELETE
	ON `UserProjectAssociation`
	FOR EACH ROW BEGIN
                -- In a DELETE trigger, only OLD.col_name can be used; there is no new row.
                SELECT `username` FROM User WHERE id = OLD.user_id INTO @username;
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'DELETE_USER_PROJECT_ASSOCIATION',
			concat('User ', @username, ' left project'),
			OLD.project_id
		);
    END$$
DELIMITER ;

Create Table ProjectFile
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    file_location VARCHAR (256) NOT NULL,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE
);

Create Table Activity
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    type VARCHAR (256) NOT NULL,
    message VARCHAR (256) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER `project_file_after_insert` AFTER INSERT
	ON `ProjectFile`
	FOR EACH ROW BEGIN
                -- In an INSERT trigger, only NEW.col_name can be used
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'INSERT_PROJECT_FILE',
			concat('Created project file at ', NEW.file_location),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `project_file_after_update` AFTER UPDATE
	ON `ProjectFile`
	FOR EACH ROW BEGIN
                -- In a UPDATE trigger, you can use OLD.col_name to refer to the columns of a row before it is updated and NEW.col_name to refer to the columns of the row after it is updated.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'UPDATE_PROJECT_FILE',
			concat('Updated project file at ', NEW.file_location),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `project_file_after_delete` AFTER DELETE
	ON `ProjectFile`
	FOR EACH ROW BEGIN
                -- In a DELETE trigger, only OLD.col_name can be used; there is no new row.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'DELETE_PROJECT_FILE',
			concat('Deleted project file at ', OLD.file_location),
			OLD.project_id
		);
    END$$
DELIMITER ;

Create Table Milestone
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    start_date DATETIME NOT NULL,
    completed_date DATETIME,
    due_date DATETIME,
    description VARCHAR (256) NOT NULL,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE
);

DELIMITER $$
CREATE TRIGGER `milestone_after_insert` AFTER INSERT
	ON `Milestone`
	FOR EACH ROW BEGIN
                -- In an INSERT trigger, only NEW.col_name can be used
                SELECT DATE_FORMAT(NEW.start_date, '%d %m %Y') INTO @start_date;
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'INSERT_MILESTONE',
			concat('Created milestone at ', @start_date, ': ', NEW.description),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `milestone_after_update` AFTER UPDATE
	ON `Milestone`
	FOR EACH ROW BEGIN
                -- In a UPDATE trigger, you can use OLD.col_name to refer to the columns of a row before it is updated and NEW.col_name to refer to the columns of the row after it is updated.
                SELECT DATE_FORMAT(NEW.start_date, '%d %m %Y') INTO @start_date;
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'UPDATE_MILESTONE',
			concat('Updated milestone: ', NEW.description),
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `milestone_after_delete` AFTER DELETE
	ON `Milestone`
	FOR EACH ROW BEGIN
                -- In a DELETE trigger, only OLD.col_name can be used; there is no new row.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'DELETE_MILESTONE',
			concat('Deleted milestone: ', OLD.description),
			OLD.project_id
		);
    END$$
DELIMITER ;

CREATE TABLE Message
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    message_text VARCHAR (10000) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id)
);

DELIMITER $$
CREATE TRIGGER `message_after_insert` AFTER INSERT
	ON `Message`
	FOR EACH ROW BEGIN
                -- In an INSERT trigger, only NEW.col_name can be used
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'INSERT_MESSAGE',
			NEW.message_text,
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `message_after_update` AFTER UPDATE
	ON `Message`
	FOR EACH ROW BEGIN
                -- In a UPDATE trigger, you can use OLD.col_name to refer to the columns of a row before it is updated and NEW.col_name to refer to the columns of the row after it is updated.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'UPDATE_MESSAGE',
			NEW.message_text,
			NEW.project_id
		);
    END$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `message_after_delete` AFTER DELETE
	ON `Message`
	FOR EACH ROW BEGIN
                -- In a DELETE trigger, only OLD.col_name can be used; there is no new row.
		INSERT INTO `Activity` (
			type,
			message,
			project_id
		) VALUES (
			'DELETE_MESSAGE',
			OLD.message_text,
			OLD.project_id
		);
    END$$
DELIMITER ;

