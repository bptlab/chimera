-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: May 13, 2015 at 03:42 PM
-- Server version: 5.5.43
-- PHP Version: 5.4.39-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `JEngineV2`
--

CREATE DATABASE IF NOT EXISTS JEngineV2;
USE JEngineV2;


CREATE TABLE IF NOT EXISTS `version` (
  `version` tinyint(1) NOT NULL,
  PRIMARY KEY (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
-- --------------------------------------------------------

--
-- Table structure for table `activityinstance`
--

CREATE TABLE IF NOT EXISTS `activityinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(512) NOT NULL,
  `automaticexecution` tinyint(1) NOT NULL DEFAULT '0',
  `canTerminate` tinyint(1) NOT NULL DEFAULT '0',
  `role_id` int(11) NOT NULL,
  `activity_state` varchar(512) NOT NULL,
  `workitem_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `activitystate`
--

CREATE TABLE IF NOT EXISTS `activitystate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `aggregation`
--

CREATE TABLE IF NOT EXISTS `aggregation` (
  `dataclass_id1` int(11) NOT NULL,
  `dataclass_id2` int(11) NOT NULL,
  `multiplicity` int(11) NOT NULL,
  PRIMARY KEY (`dataclass_id1`,`dataclass_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `boundaryeventref`
CREATE TABLE IF NOT EXISTS `boundaryeventref` (
  `controlnode_id` INTEGER NOT NULL,
  `attachedtoref` INTEGER NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ;


--
-- Table structure for table `configuration`
--

CREATE TABLE IF NOT EXISTS `configuration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `behaviourdata` varchar(1024) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `controlflow`
--

CREATE TABLE IF NOT EXISTS `controlflow` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  `condition` varchar(512) NOT NULL,
  PRIMARY KEY (`controlnode_id1`,`controlnode_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL,
  `modelid` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataattribute`
--

CREATE TABLE IF NOT EXISTS `dataattribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `default` varchar(1024) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataattributeinstance`
--

CREATE TABLE IF NOT EXISTS `dataattributeinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(1024) NOT NULL,
  `dataattribute_id` int(11) NOT NULL,
  `dataobjectinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `is_event` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataflow`
--

CREATE TABLE IF NOT EXISTS `dataflow` (
  `controlnode_id` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL,
  PRIMARY KEY (`controlnode_id`,`dataset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `datanode`
--

CREATE TABLE IF NOT EXISTS `datanode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenario_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `model_id` VARCHAR(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataobject`
--

CREATE TABLE IF NOT EXISTS `dataobject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `start_state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `dataobjectinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `datasetconsistsofdatanode`
--

CREATE TABLE IF NOT EXISTS `datasetconsistsofdatanode` (
  `dataset_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL,
  PRIMARY KEY (`dataset_id`,`datanode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `emailconfiguration`
--

CREATE TABLE IF NOT EXISTS `emailconfiguration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `receivermailaddress` varchar(1024) NOT NULL,
  `sendmailaddress` varchar(1024) NOT NULL,
  `subject` varchar(2048) NOT NULL,
  `message` text NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
-- --
-- Table structure for table `event`
--
CREATE TABLE IF NOT EXISTS `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `event_type` VARCHAR(256),
  `query` varchar(512) NOT NULL,
  `fragment_id` INTEGER NOT NULL,
  `model_id` varchar(512) NOT NULL,
  `controlnode_id` INTEGER NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

-- --
-- Table structure for table `eventinstance`
--

CREATE TABLE IF NOT EXISTS `eventinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(512) NOT NULL,
  `event_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `eventlog`
--

CREATE TABLE IF NOT EXISTS `eventlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `controlnodeinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `eventtypeattribute`
--

CREATE TABLE IF NOT EXISTS `eventtypeattribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `eventtype_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `ExclusiveEvents`
--

CREATE TABLE IF NOT EXISTS `ExclusiveEvents` (
  `MappingKey` varchar(256) NOT NULL,
  `FragmentInstanceId` int(11) NOT NULL,
  `ScenarioInstanceId` int(11) NOT NULL,
  `EventControlNodeId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------


--
-- Table structure for table `fragment`
--

CREATE TABLE IF NOT EXISTS `fragment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `modelid` VARCHAR(256) NOT NULL DEFAULT '-1',
  `modelversion` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `eventmapping`
--

CREATE TABLE IF NOT EXISTS `eventmapping` (
  `fragmentInstanceId` int(11) NOT NULL,
  `eventControlNodeId` int(11) NOT NULL,
  `eventKey` VARCHAR(512) NOT NULL,
  `notificationRuleId` VARCHAR(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `gatewayandeventstate`
--

CREATE TABLE IF NOT EXISTS `gatewayandeventstate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `gatewayinstance`
--

CREATE TABLE IF NOT EXISTS `gatewayinstance` (
  `id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `gateway_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `historyactivityinstance`
--

CREATE TABLE IF NOT EXISTS `historyactivityinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activityinstance_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `oldstate` varchar(256) DEFAULT NULL,
  `newstate` varchar(256) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------


-- --------------------------------------------------------

--
-- Table structure for table `historydataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `historydataobjectinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `controlnode_id` int(11) DEFAULT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dataobjectinstance_id` int(11) NOT NULL,
  `oldstate_id` int(11) DEFAULT NULL,
  `newstate_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------

--
-- Table structure for table `historydataattributeinstance`
--

CREATE TABLE IF NOT EXISTS `historydataattributeinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `controlnodeinstance_id` int(11) DEFAULT NULL,
  `dataattributeinstance_id` int(11) NOT NULL,
  `oldvalue` varchar(256) DEFAULT NULL,
  `newvalue` varchar(256) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------


--
-- Table structure for table `historyeventinstance`
--

CREATE TABLE IF NOT EXISTS `historyeventinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventid` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `state` varchar(256) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
--
-- Table structure for table `janalyticsresults`
--

CREATE TABLE IF NOT EXISTS `janalyticsresults` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service` varchar(256) NOT NULL,
  `json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `pathmapping`
--

CREATE TABLE IF NOT EXISTS `pathmapping` (
  -- controlnode-id of the node receiving a JSON object,
  -- e.g. WebServiceTask or MessageEvent
  `controlnode_id` int(11) NOT NULL,
  `dataattribute_id` int(11) NOT NULL,
  `jsonpath` varchar(256)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `reference`
--

CREATE TABLE IF NOT EXISTS `reference` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  PRIMARY KEY (`controlnode_id1`,`controlnode_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `right`
--

CREATE TABLE IF NOT EXISTS `right` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `rolehasright`
--

CREATE TABLE IF NOT EXISTS `rolehasright` (
  `role_id` int(11) NOT NULL,
  `right_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `scenario`
--

CREATE TABLE IF NOT EXISTS `scenario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `modelid` VARCHAR(256) DEFAULT '-1',
  `modelversion` int(11) NOT NULL DEFAULT '-1',
  `datamodelid` varchar(256) DEFAULT NULL,
  `datamodelversion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `state`
--

CREATE TABLE IF NOT EXISTS `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `statetransition`
--

CREATE TABLE IF NOT EXISTS `statetransition` (
  `state_id1` int(11) NOT NULL,
  `state_id2` int(11) NOT NULL,
  PRIMARY KEY (`state_id1`,`state_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `successor`
--

CREATE TABLE IF NOT EXISTS `successor` (
  `controlnodeinstance_id1` int(11) NOT NULL,
  `controlnodeinstance_id2` int(11) NOT NULL,
  PRIMARY KEY (`controlnodeinstance_id1`,`controlnodeinstance_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `terminationcondition`
--

CREATE TABLE IF NOT EXISTS `terminationcondition` (
  `conditionset_id` VARCHAR(512) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`conditionset_id`,`dataobject_id`,`state_id`),
  KEY `dataobject_id` (`dataobject_id`),
  KEY `state_id` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `email` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
--
-- Table structure for table `timerevent`
--

CREATE TABLE IF NOT EXISTS `timerevent` (
  `controlNodeDatabaseId` int(11) NOT NULL,
  `timerDefinition` varchar(512) NOT NULL,
  `fragmentId` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `userismemberofrole`
--

CREATE TABLE IF NOT EXISTS `userismemberofrole` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `webservicetask`
--

CREATE TABLE IF NOT EXISTS `webservicetask` (
  `controlnode_id` int(11) NOT NULL DEFAULT '0',
  `url` varchar(2048) NOT NULL,
  `method` varchar(64) NOT NULL DEFAULT 'GET',
  `body` text,
  PRIMARY KEY (`controlnode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- --------------------------------------------------------

--
-- Table structure for table `workitemstate`
--

CREATE TABLE IF NOT EXISTS `workitemstate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
