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

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_after_insert`

    /*
    An INSERT trigger is only executed when a new row is inserted. We can choose
    to have it executed either BEFORE or AFTER the insert operation occurs,
    which would be an important distinction to make if the action the trigger
    took involved foreign keys or something since a trigger is not allowed to
    violate database integrity.
    */
    AFTER INSERT

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Project`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an INSERT trigger, only NEW.col_name can be used.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_PROJECT',
                concat('Created project ', NEW.name),
                NEW.id
        );
    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_after_update`

    /*
    An UPDATE trigger is only executed when an existing row is changed. We can
    choose to have it executed either BEFORE or AFTER the update operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER UPDATE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Project`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an UPDATE trigger, you can use OLD.col_name to refer to the columns
        of a row before it is updated and NEW.col_name to refer to the columns
        of the row after it is updated.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'UPDATE_PROJECT',
                concat('Updated project ', NEW.name),
                NEW.id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_after_delete`

    /*
    A DELETE trigger is only executed when an existing row is deleted. We can
    choose to have it executed either BEFORE or AFTER the delete operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER DELETE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Project`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In a DELETE trigger, only OLD.col_name can be used; there is no new row.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_PROJECT',
                concat('Deleted project ', OLD.name),
                OLD.id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

CREATE TABLE UserProjectAssociation
(
    id INT UNSIGNED PRIMARY KEY AUTO_INCREMENT NOT NULL,
    project_id INT UNSIGNED,
    FOREIGN KEY (project_id) REFERENCES Project(id) ON DELETE CASCADE,
    user_id INT UNSIGNED,
    FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE
);

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `user_project_association_after_insert`

    /*
    An INSERT trigger is only executed when a new row is inserted. We can choose
    to have it executed either BEFORE or AFTER the insert operation occurs,
    which would be an important distinction to make if the action the trigger
    took involved foreign keys or something since a trigger is not allowed to
    violate database integrity.
    */
    AFTER INSERT

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `UserProjectAssociation`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an INSERT trigger, only NEW.col_name can be used.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.

        Also, we want to insert the actual username into this message, but all
        we have is the user_id, so we must use a SELECT command to get the
        username field of the row whose id is NEW.user_id. We place this value
        into the variable @username, where the @ symbol indicates that it's a
        user-defined variable so as to not cause conflicts.
        */
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

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `user_project_association_after_update`

    /*
    An UPDATE trigger is only executed when an existing row is changed. We can
    choose to have it executed either BEFORE or AFTER the update operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER UPDATE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `UserProjectAssociation`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an UPDATE trigger, you can use OLD.col_name to refer to the columns
        of a row before it is updated and NEW.col_name to refer to the columns
        of the row after it is updated.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.

        Also, we want to insert the actual username into this message, but all
        we have is the user_id, so we must use a SELECT command to get the
        username field of the row whose id is NEW.user_id. We place this value
        into the variable @username, where the @ symbol indicates that it's a
        user-defined variable so as to not cause conflicts.
        */
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

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `user_project_association_after_delete`

    /*
    A DELETE trigger is only executed when an existing row is deleted. We can
    choose to have it executed either BEFORE or AFTER the delete operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER DELETE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `UserProjectAssociation`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In a DELETE trigger, only OLD.col_name can be used; there is no new row.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.

        Also, we want to insert the actual username into this message, but all
        we have is the user_id, so we must use a SELECT command to get the
        username field of the row whose id is OLD.user_id. We place this value
        into the variable @username, where the @ symbol indicates that it's a
        user-defined variable so as to not cause conflicts.
        */
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

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
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

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_file_after_insert`

    /*
    An INSERT trigger is only executed when a new row is inserted. We can choose
    to have it executed either BEFORE or AFTER the insert operation occurs,
    which would be an important distinction to make if the action the trigger
    took involved foreign keys or something since a trigger is not allowed to
    violate database integrity.
    */
    AFTER INSERT

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `ProjectFile`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an INSERT trigger, only NEW.col_name can be used.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_PROJECT_FILE',
                concat('Created project file at ', NEW.file_location),
                NEW.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_file_after_update`

    /*
    An UPDATE trigger is only executed when an existing row is changed. We can
    choose to have it executed either BEFORE or AFTER the update operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER UPDATE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `ProjectFile`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an UPDATE trigger, you can use OLD.col_name to refer to the columns
        of a row before it is updated and NEW.col_name to refer to the columns
        of the row after it is updated.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'UPDATE_PROJECT_FILE',
                concat('Updated project file at ', NEW.file_location),
                NEW.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `project_file_after_delete`

    /*
    A DELETE trigger is only executed when an existing row is deleted. We can
    choose to have it executed either BEFORE or AFTER the delete operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER DELETE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `ProjectFile`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN
        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In a DELETE trigger, only OLD.col_name can be used; there is no new row.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_PROJECT_FILE',
                concat('Deleted project file at ', OLD.file_location),
                OLD.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
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

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `milestone_after_insert`

    /*
    An INSERT trigger is only executed when a new row is inserted. We can choose
    to have it executed either BEFORE or AFTER the insert operation occurs,
    which would be an important distinction to make if the action the trigger
    took involved foreign keys or something since a trigger is not allowed to
    violate database integrity.
    */
    AFTER INSERT

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Milestone`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an INSERT trigger, only NEW.col_name can be used.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.

        Also, we want to insert a formatted date into this message, so we must
        use a SELECT command and a format string to get the date stored in the
        table in the format we want to actually put in the message. We place
        this value into the variable @start_date, where the @ symbol indicates
        that it's a user-defined variable so as to not cause conflicts.
        */
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

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `milestone_after_update`

    /*
    An UPDATE trigger is only executed when an existing row is changed. We can
    choose to have it executed either BEFORE or AFTER the update operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER UPDATE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Milestone`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an UPDATE trigger, you can use OLD.col_name to refer to the columns
        of a row before it is updated and NEW.col_name to refer to the columns
        of the row after it is updated.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.

        Also, we want to insert a formatted date into this message, so we must
        use a SELECT command and a format string to get the date stored in the
        table in the format we want to actually put in the message. We place
        this value into the variable @start_date, where the @ symbol indicates
        that it's a user-defined variable so as to not cause conflicts.
        */
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

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `milestone_after_delete`

    /*
    A DELETE trigger is only executed when an existing row is deleted. We can
    choose to have it executed either BEFORE or AFTER the delete operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER DELETE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Milestone`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In a DELETE trigger, only OLD.col_name can be used; there is no new row.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_MILESTONE',
                concat('Deleted milestone: ', OLD.description),
                OLD.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
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

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `message_after_insert`

    /*
    An INSERT trigger is only executed when a new row is inserted. We can choose
    to have it executed either BEFORE or AFTER the insert operation occurs,
    which would be an important distinction to make if the action the trigger
    took involved foreign keys or something since a trigger is not allowed to
    violate database integrity.
    */
    AFTER INSERT

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Message`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an INSERT trigger, only NEW.col_name can be used.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_MESSAGE',
                NEW.message_text,
                NEW.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `message_after_update`

    /*
    An UPDATE trigger is only executed when an existing row is changed. We can
    choose to have it executed either BEFORE or AFTER the update operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER UPDATE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Message`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In an UPDATE trigger, you can use OLD.col_name to refer to the columns
        of a row before it is updated and NEW.col_name to refer to the columns
        of the row after it is updated.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'UPDATE_MESSAGE',
                NEW.message_text,
                NEW.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

/*
Since the triggers may contain semicolon characters, we must change the
delimiter to something else temporarily so that we can create the trigger
with the correct contents. We are using a double dollar sign delimiter here
since that is unlikely to conflict with anything else.
*/
DELIMITER $$

/*
Since this is the database creation script, we can assume that the trigger
doesn't already exist and simple create it.
*/
CREATE TRIGGER

    /*
    Each trigger must have a unique name, so we use a naming convention that
    informs us which table the trigger is associated with, whether the
    trigger is executed before or after the action, and which action the
    trigger is actually listening for. There shouldn't be multiple triggers
    on a particular table listening for the same thing so this convention
    guarantees that we have a unique name while also being informative.
    */
    `message_after_delete`

    /*
    A DELETE trigger is only executed when an existing row is deleted. We can
    choose to have it executed either BEFORE or AFTER the delete operation
    occurs, which would be an important distinction to make if the action the
    trigger took involved foreign keys or something since a trigger is not
    allowed to violate database integrity.
    */
    AFTER DELETE

    /*
    Define the name of the database table this trigger is attached to. This
    table must already exist when we run this CREATE TRIGGER command so this
    command should be below the CREATE TABLE Project command.
    */
    ON `Message`

    /*
    A database operation may have affected multiple rows, and our trigger
    receives all of them at once, so it must iterate over each one of them
    in turn.
    */
    FOR EACH ROW BEGIN

        /*
        MySQL triggers automatically create NEW and OLD row variables as
        appropriate that contain the actual row data from the table the
        trigger is associated with that was involved in whatever type of
        change the trigger was watching for.

        In a DELETE trigger, only OLD.col_name can be used; there is no new row.

        In this case, we are simply making a record of the change in the
        Activity table, which is essentially functioning like an audit
        log. This table is used as the data source for certain features
        like the RSS feed.
        */
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_MESSAGE',
                OLD.message_text,
                OLD.project_id
        );

    /*
    We must explicitly close the FOR EACH loop.
    */
    END

/*
Since we previously changed the delimiter to a double dollar sign, we must
terminate the CREATE TRIGGER command with a double dollar sign in order to
execute it.
*/
$$

/*
After we've created the trigger, we must reset the delimiter back to a
semicolon so that future commands are terminated and executed correctly.
*/
DELIMITER ;

