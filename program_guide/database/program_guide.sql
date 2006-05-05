-- MySQL dump 10.10
--
-- Host: localhost    Database: program_guide
-- ------------------------------------------------------
-- Server version	5.0.17-standard

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

--
-- Table structure for table `episode`
--

DROP TABLE IF EXISTS `episode`;
CREATE TABLE `episode` (
  `program_id` int(11) unsigned NOT NULL default '0',
  `season` char(2) NOT NULL default '',
  `number` smallint(6) unsigned NOT NULL default '0',
  `production_code` varchar(32) default NULL,
  `original_air_date` date default NULL,
  `title` tinytext,
  `serial_number` smallint(5) unsigned NOT NULL default '0',
  `summary_url` tinytext,
  PRIMARY KEY  (`program_id`,`season`,`number`),
  FULLTEXT KEY `title` (`title`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` int(11) NOT NULL auto_increment,
  `source` tinytext NOT NULL,
  `create_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `content` mediumtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
CREATE TABLE `program` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `name` tinytext NOT NULL,
  `url` tinytext NOT NULL,
  `last_update` datetime default NULL,
  `do_update` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `user_id` int(11) NOT NULL default '0',
  `program_id` int(11) unsigned NOT NULL default '0',
  `season` char(2) NOT NULL default '0',
  `episode_number` int(11) unsigned NOT NULL default '0',
  `status` enum('queued','viewed') NOT NULL default 'queued',
  `create_date` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`user_id`,`program_id`,`season`,`episode_number`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `subscribed`
--

DROP TABLE IF EXISTS `subscribed`;
CREATE TABLE `subscribed` (
  `user_id` int(11) NOT NULL default '0',
  `program_id` int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (`user_id`,`program_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `torrent_site`
--

DROP TABLE IF EXISTS `torrent_site`;
CREATE TABLE `torrent_site` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `name` tinytext NOT NULL,
  `url` tinytext NOT NULL,
  `search_string` tinytext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL auto_increment,
  `username` varchar(16) NOT NULL default '',
  `password` varchar(32) character set latin1 collate latin1_bin NOT NULL default '',
  `last_login_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `registration_date` datetime NOT NULL default '0000-00-00 00:00:00',
  `permissions` int(11) NOT NULL default '1',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

