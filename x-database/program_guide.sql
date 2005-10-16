-- MySQL dump 9.11
--
-- Host: localhost    Database: program_guide
-- ------------------------------------------------------
-- Server version	4.0.26

--
-- Table structure for table `episode`
--

CREATE TABLE episode (
  program_id int(11) unsigned NOT NULL default '0',
  season char(1) NOT NULL default '',
  number smallint(6) unsigned NOT NULL default '0',
  production_code varchar(32) default NULL,
  original_air_date date default NULL,
  title tinytext,
  PRIMARY KEY  (program_id,season,number)
) TYPE=MyISAM;

--
-- Table structure for table `program`
--

CREATE TABLE program (
  id int(11) unsigned NOT NULL auto_increment,
  name tinytext NOT NULL,
  PRIMARY KEY  (id)
) TYPE=MyISAM;

--
-- Table structure for table `queued`
--

CREATE TABLE queued (
  user_id int(11) NOT NULL default '0',
  program_id int(11) unsigned NOT NULL default '0',
  season char(1) NOT NULL default '0',
  episode_number int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (user_id,program_id,season,episode_number)
) TYPE=MyISAM;

--
-- Table structure for table `subscribed`
--

CREATE TABLE subscribed (
  user_id int(11) NOT NULL default '0',
  program_id int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (user_id,program_id)
) TYPE=MyISAM;

--
-- Table structure for table `user`
--

CREATE TABLE user (
  id int(11) unsigned NOT NULL auto_increment,
  username varchar(16) NOT NULL default '',
  password varchar(32) binary NOT NULL default '',
  last_login_date datetime NOT NULL default '0000-00-00 00:00:00',
  registration_date datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (id),
  UNIQUE KEY username (username)
) TYPE=MyISAM;

--
-- Table structure for table `viewed`
--

CREATE TABLE viewed (
  user_id int(11) NOT NULL default '0',
  program_id int(11) unsigned NOT NULL default '0',
  season char(1) NOT NULL default '',
  episode_number int(11) unsigned NOT NULL default '0',
  PRIMARY KEY  (user_id,program_id,season,episode_number)
) TYPE=MyISAM;

