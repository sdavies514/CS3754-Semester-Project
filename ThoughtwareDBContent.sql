-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: ThoughtwareDB
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;



DROP TABLE IF EXISTS UserFile, UserPhoto, Message, Activity, Milestone, ProjectFile, UserProjectAssociation, Project, User;


--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `hashed_password` varchar(64) NOT NULL,
  `first_name` varchar(32) NOT NULL,
  `middle_name` varchar(32) DEFAULT NULL,
  `last_name` varchar(32) NOT NULL,
  `address1` varchar(128) NOT NULL,
  `address2` varchar(128) DEFAULT NULL,
  `city` varchar(64) NOT NULL,
  `state` varchar(2) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `security_question` int(11) NOT NULL,
  `security_answer` varchar(128) NOT NULL,
  `email` varchar(128) NOT NULL,
  `google_image_url` varchar(128) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (2,'Butenhoff','$2a$10$79.dWamGFcOVvx.FsmhSzeV0a9d4TM4WolEVansRfBr1uBjWWjD6q','Casey','','Butenhoff','1 Fake Address','','Blacksburg','VA','24060',0,'Blacksburg','eolxe@vt.edu','N/A'),(3,'Fire30','$2a$10$dbbFaoKE3HzcdYPu/EaW5ONsGjZbfEiKemOi3hG4kXFnekeWci7Ly','asdasd','d','asdf','asd','','asdf','AK','23123',0,'asdfasdf','test1r23@gmail.com','N/A'),(4,'KyleDyess','$2a$10$ni5/yJhUzJZmz6lZdaPcmu5jpgpPCC/p2K.mVqdylJJYG3vFTe0lq','Kyle','','Dyess','18 Thoughtware cir.','','Blacksburg','VA','24060',0,'Blacksburg','kdyess97@vt.edu','N/A'),(5,'TanDoan','$2a$10$hG/jc6sEPuYGxcx8cAXBaefB0QDr0W/g2w688D3xJm7sZGGQ9l9cS','Tomy','','Doan poNG','9999 Hello Lane','','Blacksburg','VA','24060',0,'Blacksburg','tomy@gmail.com','N/A'),(6,'Davies','$2a$10$lAlKaM31Ciccq/j4vVlwZeZJpxjuzMxZyzsgA1xj7mIomutK9J42C','Shane','','Davies','Test','','Test','AK','00000',0,'Test','shane514@vt.edu','N/A'),(7,'sdavies514@gmail.com','$2a$10$xIWSEe2Nq/SvU0vPhW8H8uYQwXp5NuAhHkOAoFMGL65TkodEVALva','Shane',NULL,'Davies','Google account, please update!',NULL,'Google account, please update!','AK','00000',0,'Google account, please update!','sdavies514@gmail.com','https://lh5.googleusercontent.com/-_kILPwreOCg/AAAAAAAAAAI/AAAAAAAAAAA/AFiYof0QLshiPkCt6BjnfteHDL86Edeh_Q/s96-c/photo.jpg'),(8,'Corley','$2a$10$EDj2hT/rH3QYdCYNdCLTU.6T2xdLGrkdgxT2/2mFzjNWEZQ4lWdvy','Thomas','','Corley','123 Fake Street','','Fakeland','AK','23466',0,'marshmellow','tjcorley@vt.edu','N/A'),(9,'tjcorley30@gmail.com','$2a$10$XO7ty526jcwlZcSn89Kn7ecadH5zBey38A5ZPNIcYeFfLMPC8l.Lq','TJ',NULL,'Corley','Google account, please update!',NULL,'Google account, please update!','AK','00000',0,'Google account, please update!','tjcorley30@gmail.com','https://lh4.googleusercontent.com/-9BUa6d9wec4/AAAAAAAAAAI/AAAAAAAAAZo/RfAZv_gEkdA/s96-c/photo.jpg'),(10,'tjcorley@vt.edu','$2a$10$wyzSUHyhcod2CHisbK72PenGm.nIlS.NWC.fYlVUeILyJpey3w6Qu','TJ',NULL,'Corley','Google account, please update!',NULL,'Google account, please update!','AK','00000',0,'Google account, please update!','tjcorley@vt.edu','https://lh3.googleusercontent.com/-hHR05wFBqoI/AAAAAAAAAAI/AAAAAAAAAAA/AFiYof31uNkfuaZF9whOaLASCJk4bDqKMg/s96-c/photo.jpg');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Project`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `hashed_password` varchar(64) NOT NULL,
  `rss_key` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Project`
--

LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (1,'Party Project','$2a$10$ndBv0pwrrygs1OJGxUzh/eQTn0wFFyhz1hQpJ.Hy5TKBJAhcBBWjO','jOttCFOJ2nhfaNZcSzADiYc219Zsq4ngEQiG'),(2,'FOR INTENTS AND PURPOSES','$2a$10$Y7fsYVe2feuFWMz8xvLuRukp7Kfa.uLrRM4QS6hLgyPW1eozsyxLm','AZoYN8KUaFt9gwg7yOsj7WgjJsqpM5NrWRky'),(3,'Android App','$2a$10$pD6b3n4/SZNn.rN0V4XZcumid7Ka1srmx/jgveqZ5kaegE/A7/FwK','inXbu2CL9XakwHCqRlnnUhWPKXk1UdMI9uge'),(4,'Jack and Jill','$2a$10$7nvaqrqlv4isTOhaV0B..uO2n.7mViww2ORpIBuctGD7cxWFUo3JG','KriTFtezfmbpGiN65fO1Qn5s20naN3wHAleW');
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `project_after_insert`

    
    AFTER INSERT

    
    ON `Project`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_PROJECT',
                concat('Created project ', NEW.name),
                NEW.id
        );
    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `project_after_update`

    
    AFTER UPDATE

    
    ON `Project`

    
    FOR EACH ROW BEGIN

        
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
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `project_after_delete`

    
    AFTER DELETE

    
    ON `Project`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_PROJECT',
                concat('Deleted project ', OLD.name),
                OLD.id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `UserProjectAssociation`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserProjectAssociation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `project_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `UserProjectAssociation_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `UserProjectAssociation_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserProjectAssociation`
--

LOCK TABLES `UserProjectAssociation` WRITE;
/*!40000 ALTER TABLE `UserProjectAssociation` DISABLE KEYS */;
INSERT INTO `UserProjectAssociation` VALUES (1,1,3),(2,1,5),(3,1,4),(4,1,2),(5,1,6),(6,3,2),(7,2,2),(8,1,8),(9,4,5),(10,1,10);
/*!40000 ALTER TABLE `UserProjectAssociation` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `user_project_association_after_insert`

    
    AFTER INSERT

    
    ON `UserProjectAssociation`

    
    FOR EACH ROW BEGIN

        
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

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `user_project_association_after_update`

    
    AFTER UPDATE

    
    ON `UserProjectAssociation`

    
    FOR EACH ROW BEGIN

        
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

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `user_project_association_after_delete`

    
    AFTER DELETE

    
    ON `UserProjectAssociation`

    
    FOR EACH ROW BEGIN

        
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

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

--
-- Table structure for table `ProjectFile`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProjectFile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `file_location` varchar(256) NOT NULL,
  `file_size` varchar(32) NOT NULL,
  `project_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `ProjectFile_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProjectFile`
--

LOCK TABLES `ProjectFile` WRITE;
/*!40000 ALTER TABLE `ProjectFile` DISABLE KEYS */;
INSERT INTO `ProjectFile` VALUES (2,'1_Experiment_1.png','113 KB',1),(4,'1_Experiment_3.png','112 KB',1),(5,'4_bck.png','451 KB',4);
/*!40000 ALTER TABLE `ProjectFile` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
 CREATE TRIGGER

    
    `project_file_after_insert`

    
    AFTER INSERT

    
    ON `ProjectFile`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_PROJECT_FILE',
                concat('Created project file at ', NEW.file_location),
                NEW.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `project_file_after_update`

    
    AFTER UPDATE

    
    ON `ProjectFile`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'UPDATE_PROJECT_FILE',
                concat('Updated project file at ', NEW.file_location),
                NEW.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `project_file_after_delete`

    
    AFTER DELETE

    
    ON `ProjectFile`

    
    FOR EACH ROW BEGIN
        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_PROJECT_FILE',
                concat('Deleted project file at ', OLD.file_location),
                OLD.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Milestone`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Milestone` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `start_date` datetime NOT NULL,
  `completed_date` datetime DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `description` varchar(256) NOT NULL,
  `project_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `Milestone_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Milestone`
--

LOCK TABLES `Milestone` WRITE;
/*!40000 ALTER TABLE `Milestone` DISABLE KEYS */;
INSERT INTO `Milestone` VALUES (1,'2017-12-14 00:00:00','2017-12-14 00:00:00','2017-12-15 00:00:00','For intents and purposes',1),(2,'2017-11-01 00:00:00','2017-12-15 23:59:59','2017-12-15 13:20:00','Finish Thoughtware Project',1),(3,'2017-12-13 00:00:00',NULL,'2017-12-16 00:00:00','First Draft',1),(4,'2017-12-15 00:00:00',NULL,'2017-12-15 00:00:00','First Draft',3),(5,'2017-12-16 00:00:00',NULL,'2017-12-16 00:00:00','Second Draft',3),(6,'2017-12-17 00:00:00',NULL,'2017-12-17 00:00:00','Final Draft',3),(7,'2017-12-19 00:00:00',NULL,'2017-12-21 00:00:00','Second Draft',1);
/*!40000 ALTER TABLE `Milestone` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `milestone_after_insert`

    
    AFTER INSERT

    
    ON `Milestone`

    
    FOR EACH ROW BEGIN

        
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

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `milestone_after_update`

    
    AFTER UPDATE

    
    ON `Milestone`

    
    FOR EACH ROW BEGIN

        
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

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `milestone_after_delete`

    
    AFTER DELETE

    
    ON `Milestone`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_MILESTONE',
                concat('Deleted milestone: ', OLD.description),
                OLD.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;


--
-- Table structure for table `Activity`
--
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Activity` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(256) NOT NULL,
  `message` varchar(256) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `project_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `Activity_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Activity`
--

LOCK TABLES `Activity` WRITE;
/*!40000 ALTER TABLE `Activity` DISABLE KEYS */;
INSERT INTO `Activity` VALUES (1,'INSERT_PROJECT','Created project Party Project','2017-12-14 18:46:18',1),(2,'INSERT_USER_PROJECT_ASSOCIATION','User Fire30 joined project','2017-12-14 18:46:22',1),(3,'INSERT_USER_PROJECT_ASSOCIATION','User TanDoan joined project','2017-12-14 18:48:09',1),(4,'INSERT_USER_PROJECT_ASSOCIATION','User KyleDyess joined project','2017-12-14 18:48:10',1),(5,'INSERT_MESSAGE','Hello hello','2017-12-14 18:48:22',1),(6,'INSERT_MESSAGE','HI','2017-12-14 18:48:29',1),(7,'INSERT_MESSAGE','HIEFLJKSDFSL','2017-12-14 18:48:41',1),(8,'INSERT_MILESTONE','Created milestone at 14 12 2017: For intents and purposes','2017-12-14 18:50:03',1),(9,'UPDATE_MILESTONE','Updated milestone: For intents and purposes','2017-12-14 18:50:37',1),(10,'INSERT_USER_PROJECT_ASSOCIATION','User Butenhoff joined project','2017-12-14 18:51:00',1),(11,'INSERT_PROJECT','Created project FOR INTENTS AND PURPOSES','2017-12-14 18:51:39',2),(12,'INSERT_MILESTONE','Created milestone at 01 11 2017: Finish Thoughtware Project','2017-12-14 18:51:53',1),(13,'INSERT_MESSAGE','HI','2017-12-14 18:51:54',1),(14,'INSERT_MESSAGE','hey','2017-12-14 18:51:57',1),(15,'INSERT_MESSAGE','Hey guys','2017-12-14 18:52:14',1),(16,'INSERT_MESSAGE','TEST','2017-12-14 18:52:26',1),(17,'INSERT_MESSAGE','TEST','2017-12-14 18:52:27',1),(18,'INSERT_MESSAGE','TEST','2017-12-14 18:52:28',1),(19,'INSERT_MESSAGE','TEST','2017-12-14 18:52:29',1),(20,'INSERT_MESSAGE','LOL','2017-12-14 18:52:30',1),(21,'INSERT_MESSAGE','DFSDF','2017-12-14 18:52:32',1),(22,'INSERT_MESSAGE','asdf','2017-12-14 18:52:33',1),(23,'INSERT_MESSAGE','SDFSDF','2017-12-14 18:52:34',1),(24,'INSERT_MESSAGE','TEST','2017-12-14 18:52:38',1),(25,'INSERT_USER_PROJECT_ASSOCIATION','User Davies joined project','2017-12-14 18:52:46',1),(26,'INSERT_MESSAGE','hay','2017-12-14 18:52:48',1),(27,'INSERT_MESSAGE','Hi','2017-12-14 18:53:16',1),(28,'INSERT_MESSAGE','asdfg','2017-12-14 18:53:42',1),(29,'INSERT_MESSAGE','Test','2017-12-14 18:54:09',1),(30,'INSERT_MESSAGE','hello','2017-12-14 18:55:00',1),(31,'INSERT_MESSAGE','hey','2017-12-14 18:55:07',1),(32,'INSERT_MESSAGE','hello','2017-12-14 18:55:32',1),(33,'INSERT_PROJECT_FILE','Created project file at 1_Experiment_1.png','2017-12-14 18:58:58',1),(34,'DELETE_PROJECT_FILE','Deleted project file at 1_Experiment_1.png','2017-12-14 18:59:09',1),(35,'INSERT_MESSAGE','wert','2017-12-14 18:59:16',1),(36,'INSERT_PROJECT_FILE','Created project file at 1_Experiment_1.png','2017-12-14 18:59:17',1),(37,'UPDATE_MILESTONE','Updated milestone: Finish Thoughtware Project','2017-12-14 19:01:07',1),(38,'INSERT_PROJECT','Created project RecipeProject','2017-12-04 17:14:05',1),(39,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-04 17:14:05',1),(40,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-04 17:14:05',1),(41,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-06 17:14:05',1),(42,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-06 17:14:05',1),(43,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-07 17:14:05',1),(44,'INSERT_PROJECT','Created project RecipeProject','2017-12-04 17:14:05',1),(45,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-04 17:14:05',1),(46,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-04 17:14:05',1),(47,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-06 17:14:05',1),(48,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-06 17:14:05',1),(49,'INSERT_PROJECT_FILE','Created project file Something.xml','2017-12-07 17:14:05',1),(50,'INSERT_MESSAGE','TEst','2017-12-14 19:05:58',1),(51,'INSERT_MESSAGE','test','2017-12-14 19:06:13',1),(52,'INSERT_MESSAGE','test','2017-12-14 19:06:14',1),(53,'INSERT_MESSAGE','ststeset','2017-12-14 19:06:16',1),(54,'INSERT_MESSAGE','Hello','2017-12-14 19:06:16',1),(55,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:17',1),(56,'INSERT_MESSAGE','asdfasdfasdf','2017-12-14 19:06:17',1),(57,'INSERT_MESSAGE','asdfasdfa','2017-12-14 19:06:18',1),(58,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:19',1),(59,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:20',1),(60,'INSERT_MESSAGE','asdf','2017-12-14 19:06:21',1),(61,'INSERT_MESSAGE','asdfasfd','2017-12-14 19:06:21',1),(62,'INSERT_MESSAGE','LOL','2017-12-14 19:06:41',1),(63,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:42',1),(64,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:43',1),(65,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:44',1),(66,'INSERT_MESSAGE','asdf','2017-12-14 19:06:45',1),(67,'INSERT_MESSAGE','Yep','2017-12-14 19:06:46',1),(68,'INSERT_MESSAGE','asdfasdf','2017-12-14 19:06:46',1),(69,'INSERT_MESSAGE','asdfasdfasdf','2017-12-14 19:06:47',1),(70,'INSERT_MESSAGE','asdfasdfasdf','2017-12-14 19:06:48',1),(71,'INSERT_MILESTONE','Created milestone at 13 12 2017: New Milestone','2017-12-14 19:12:11',1),(72,'UPDATE_MILESTONE','Updated milestone: First Draft','2017-12-14 19:12:54',1),(73,'INSERT_MESSAGE','LOL','2017-12-14 19:13:26',1),(74,'INSERT_MESSAGE','LOL','2017-12-14 19:14:53',1),(75,'INSERT_MESSAGE','TEST','2017-12-14 19:33:43',1),(76,'INSERT_PROJECT','Created project Android App','2017-12-14 19:54:12',3),(77,'INSERT_USER_PROJECT_ASSOCIATION','User Butenhoff joined project','2017-12-14 19:54:17',3),(78,'INSERT_MILESTONE','Created milestone at 15 12 2017: First Draft','2017-12-14 19:54:42',3),(79,'INSERT_PROJECT_FILE','Created project file at 1_items.txt','2017-12-14 19:54:45',1),(80,'INSERT_MILESTONE','Created milestone at 16 12 2017: Second Draft','2017-12-14 19:54:55',3),(81,'INSERT_MILESTONE','Created milestone at 17 12 2017: Final Draft','2017-12-14 19:55:09',3),(82,'INSERT_MESSAGE','asdf','2017-12-14 19:55:36',1),(83,'INSERT_USER_PROJECT_ASSOCIATION','User Butenhoff joined project','2017-12-14 20:32:40',2),(84,'INSERT_MESSAGE','hi','2017-12-14 20:35:05',1),(85,'INSERT_MILESTONE','Created milestone at 19 12 2017: Second Draft','2017-12-14 20:37:10',1),(86,'DELETE_PROJECT_FILE','Deleted project file at 1_items.txt','2017-12-14 20:38:06',1),(87,'INSERT_PROJECT_FILE','Created project file at 1_Experiment_3.png','2017-12-14 20:38:20',1),(88,'INSERT_USER_PROJECT_ASSOCIATION','User Corley joined project','2017-12-15 06:11:09',1),(89,'INSERT_MESSAGE','HI MAN','2017-12-15 06:11:15',1),(90,'INSERT_PROJECT','Created project Jack and Jill','2017-12-15 14:37:16',4),(91,'INSERT_USER_PROJECT_ASSOCIATION','User TanDoan joined project','2017-12-15 14:37:21',4),(92,'INSERT_PROJECT_FILE','Created project file at 4_bck.png','2017-12-15 14:38:13',4),(93,'INSERT_USER_PROJECT_ASSOCIATION','User tjcorley@vt.edu joined project','2017-12-15 16:07:05',1),(94,'INSERT_MESSAGE','HEY MAN','2017-12-15 16:07:11',1);
/*!40000 ALTER TABLE `Activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Message`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Message` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `message_text` varchar(10000) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `project_id` int(10) unsigned DEFAULT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Message_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `Project` (`id`) ON DELETE CASCADE,
  CONSTRAINT `Message_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Message`
--

LOCK TABLES `Message` WRITE;
/*!40000 ALTER TABLE `Message` DISABLE KEYS */;
INSERT INTO `Message` VALUES (1,'Hello hello','2017-12-14 18:48:22',1,5),(2,'HI','2017-12-14 18:48:29',1,5),(3,'HIEFLJKSDFSL','2017-12-14 18:48:42',1,5),(4,'HI','2017-12-14 18:51:55',1,5),(5,'hey','2017-12-14 18:51:58',1,2),(6,'Hey guys','2017-12-14 18:52:15',1,4),(7,'TEST','2017-12-14 18:52:26',1,3),(8,'TEST','2017-12-14 18:52:28',1,3),(9,'TEST','2017-12-14 18:52:29',1,3),(10,'TEST','2017-12-14 18:52:30',1,3),(11,'LOL','2017-12-14 18:52:31',1,3),(12,'DFSDF','2017-12-14 18:52:32',1,3),(13,'asdf','2017-12-14 18:52:33',1,2),(14,'SDFSDF','2017-12-14 18:52:34',1,3),(15,'TEST','2017-12-14 18:52:38',1,5),(16,'hay','2017-12-14 18:52:49',1,2),(17,'Hi','2017-12-14 18:53:16',1,3),(18,'asdfg','2017-12-14 18:53:43',1,2),(19,'Test','2017-12-14 18:54:09',1,3),(20,'hello','2017-12-14 18:55:00',1,2),(21,'hey','2017-12-14 18:55:08',1,2),(22,'hello','2017-12-14 18:55:32',1,5),(23,'wert','2017-12-14 18:59:17',1,3),(24,'TEst','2017-12-14 19:05:59',1,3),(25,'test','2017-12-14 19:06:14',1,3),(26,'test','2017-12-14 19:06:15',1,3),(27,'ststeset','2017-12-14 19:06:16',1,3),(28,'Hello','2017-12-14 19:06:17',1,5),(29,'asdfasdf','2017-12-14 19:06:17',1,3),(30,'asdfasdfasdf','2017-12-14 19:06:18',1,3),(31,'asdfasdfa','2017-12-14 19:06:19',1,3),(32,'asdfasdf','2017-12-14 19:06:19',1,3),(33,'asdfasdf','2017-12-14 19:06:20',1,3),(34,'asdf','2017-12-14 19:06:21',1,3),(35,'asdfasfd','2017-12-14 19:06:22',1,3),(36,'LOL','2017-12-14 19:06:41',1,3),(37,'asdfasdf','2017-12-14 19:06:43',1,3),(38,'asdfasdf','2017-12-14 19:06:44',1,3),(39,'asdfasdf','2017-12-14 19:06:44',1,3),(40,'asdf','2017-12-14 19:06:46',1,3),(41,'Yep','2017-12-14 19:06:46',1,5),(42,'asdfasdf','2017-12-14 19:06:46',1,3),(43,'asdfasdfasdf','2017-12-14 19:06:48',1,3),(44,'asdfasdfasdf','2017-12-14 19:06:49',1,3),(45,'LOL','2017-12-14 19:13:27',1,3),(46,'LOL','2017-12-14 19:14:53',1,3),(47,'TEST','2017-12-14 19:33:44',1,5),(48,'asdf','2017-12-14 19:55:36',1,5),(49,'hi','2017-12-14 20:35:06',1,2),(50,'HI MAN','2017-12-15 06:11:16',1,8),(51,'HEY MAN','2017-12-15 16:07:12',1,10);
/*!40000 ALTER TABLE `Message` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `message_after_insert`

    
    AFTER INSERT

    
    ON `Message`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'INSERT_MESSAGE',
                NEW.message_text,
                NEW.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `message_after_update`

    
    AFTER UPDATE

    
    ON `Message`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'UPDATE_MESSAGE',
                NEW.message_text,
                NEW.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER $$
CREATE TRIGGER

    
    `message_after_delete`

    
    AFTER DELETE

    
    ON `Message`

    
    FOR EACH ROW BEGIN

        
        INSERT INTO `Activity` (
                type,
                message,
                project_id
        ) VALUES (
                'DELETE_MESSAGE',
                OLD.message_text,
                OLD.project_id
        );

    
    END $$
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;


--
-- Table structure for table `UserFile`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserFile` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filename` varchar(256) NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `UserFile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserFile`
--

LOCK TABLES `UserFile` WRITE;
/*!40000 ALTER TABLE `UserFile` DISABLE KEYS */;
/*!40000 ALTER TABLE `UserFile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserPhoto`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserPhoto` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `extension` enum('jpeg','jpg','png','gif') NOT NULL,
  `user_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `UserPhoto_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserPhoto`
--

LOCK TABLES `UserPhoto` WRITE;
/*!40000 ALTER TABLE `UserPhoto` DISABLE KEYS */;
INSERT INTO `UserPhoto` VALUES (7,'png',4),(10,'png',5);
/*!40000 ALTER TABLE `UserPhoto` ENABLE KEYS */;
UNLOCK TABLES;

-- Dump completed on 2017-12-15 11:09:33
