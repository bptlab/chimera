-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 26. Nov 2014 um 15:02
-- Server Version: 5.6.14
-- PHP-Version: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `jengine`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `activityinstance`
--

CREATE TABLE IF NOT EXISTS `activityinstance` (
  `id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `role_id` int(11) NOT NULL,
  `activity_state` varchar(512) NOT NULL,
  `workitem_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `activitystate`
--

CREATE TABLE IF NOT EXISTS `activitystate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `configuration`
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
-- Tabellenstruktur für Tabelle `controlflow`
--

CREATE TABLE IF NOT EXISTS `controlflow` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  `condition` varchar(512) NOT NULL,
  PRIMARY KEY (`controlnode_id1`,`controlnode_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
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
-- Tabellenstruktur für Tabelle `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataflow`
--

CREATE TABLE IF NOT EXISTS `dataflow` (
  `controlnode_id` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL,
  PRIMARY KEY (`controlnode_id`,`dataset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datanode`
--

CREATE TABLE IF NOT EXISTS `datanode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobject`
--

CREATE TABLE IF NOT EXISTS `dataobject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datasetconsistsofdatanode`
--

CREATE TABLE IF NOT EXISTS `datasetconsistsofdatanode` (
  `dataset_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL,
  PRIMARY KEY (`dataset_id`,`datanode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventinstance`
--

CREATE TABLE IF NOT EXISTS `eventinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(512) NOT NULL,
  `event_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventlog`
--

CREATE TABLE IF NOT EXISTS `eventlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `controlnodeinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragment`
--

CREATE TABLE IF NOT EXISTS `fragment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gatewayandeventstate`
--

CREATE TABLE IF NOT EXISTS `gatewayandeventstate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gatewayinstance`
--

CREATE TABLE IF NOT EXISTS `gatewayinstance` (
  `id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `gateway_state` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `reference`
--

CREATE TABLE IF NOT EXISTS `reference` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  PRIMARY KEY (`controlnode_id1`,`controlnode_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `right`
--

CREATE TABLE IF NOT EXISTS `right` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rolehasright`
--

CREATE TABLE IF NOT EXISTS `rolehasright` (
  `role_id` int(11) NOT NULL,
  `right_id` int(11) NOT NULL,
  PRIMARY KEY (`role_id`,`right_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenario`
--

CREATE TABLE IF NOT EXISTS `scenario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `state`
--

CREATE TABLE IF NOT EXISTS `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `statetransition`
--

CREATE TABLE IF NOT EXISTS `statetransition` (
  `state_id1` int(11) NOT NULL,
  `state_id2` int(11) NOT NULL,
  PRIMARY KEY (`state_id1`,`state_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `successor`
--

CREATE TABLE IF NOT EXISTS `successor` (
  `controlnodeinstance_id1` int(11) NOT NULL,
  `controlnodeinstance_id2` int(11) NOT NULL,
  PRIMARY KEY (`controlnodeinstance_id1`,`controlnodeinstance_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `terminationcondition`
--

CREATE TABLE IF NOT EXISTS `terminationcondition` (
  `id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`,`dataclass_id`,`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `email` varchar(512) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userismemberofrole`
--

CREATE TABLE IF NOT EXISTS `userismemberofrole` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `workitemstate`
--

CREATE TABLE IF NOT EXISTS `workitemstate` (
  `state` varchar(512) NOT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
