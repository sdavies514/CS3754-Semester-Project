# --------------------------------------------
# SQL script to create and populate the tables
# for the Thoughtware Database (ThoughtwareDB)
# Created by Casey Butenhoff
# --------------------------------------------

/*
Tables to be dropped must be listed in a logical order based on dependency.
UserFile and UserPhoto depend on User. Therefore, they must be dropped before User.
*/
DROP TABLE IF EXISTS UserFile, UserPhoto, User;

/* The User table contains attributes of interest of a User. */
CREATE TABLE User
(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    username VARCHAR (32) NOT NULL,
    password VARCHAR (32) NOT NULL,
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