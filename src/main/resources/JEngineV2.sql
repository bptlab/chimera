-- phpMyAdmin SQL Dump
-- version 4.0.9
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 28. Apr 2015 um 13:29
-- Server Version: 5.6.14
-- PHP-Version: 5.5.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `JEngineV2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `activityinstance`
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Daten für Tabelle `activityinstance`
--

INSERT INTO `activityinstance` (`id`, `type`, `automaticexecution`, `canTerminate`, `role_id`, `activity_state`, `workitem_state`) VALUES
(1, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(3, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(5, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(7, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(8, 'HumanTask', 1, 1, 1, 'terminated', 'init'),
(10, 'HumanTask', 0, 0, 1, 'ready(ControlFlow)', 'init'),
(11, 'HumanTask', 0, 0, 1, 'ready', 'init'),
(12, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(14, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(16, 'HumanTask', 0, 1, 1, 'terminated', 'init'),
(18, 'HumanTask', 0, 0, 1, 'ready(ControlFlow)', 'init');

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
-- Tabellenstruktur für Tabelle `aggregation`
--

CREATE TABLE IF NOT EXISTS `aggregation` (
  `dataclass_id1` int(11) NOT NULL,
  `dataclass_id2` int(11) NOT NULL,
  `multiplicity` int(11) NOT NULL,
  PRIMARY KEY (`dataclass_id1`,`dataclass_id2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `aggregation`
--

INSERT INTO `aggregation` (`dataclass_id1`, `dataclass_id2`, `multiplicity`) VALUES
(2, 1, 2147483647),
(4, 3, 2147483647),
(6, 5, 2147483647),
(8, 7, 2147483647);

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

--
-- Daten für Tabelle `controlflow`
--

INSERT INTO `controlflow` (`controlnode_id1`, `controlnode_id2`, `condition`) VALUES
(1, 8, ''),
(3, 9, ''),
(4, 9, ''),
(5, 1, ''),
(6, 9, ''),
(7, 6, ''),
(8, 3, 'DEFAULT'),
(8, 4, '$Fenster.Schmutzgrad<1'),
(8, 7, '$Fenster.Schmutzgrad>10 & $Wischeimer.Fuellstand>5'),
(9, 2, ''),
(10, 17, ''),
(12, 18, ''),
(13, 18, ''),
(14, 10, ''),
(15, 18, ''),
(16, 15, ''),
(17, 12, 'DEFAULT'),
(17, 13, '$Fenster.Schmutzgrad<1'),
(17, 16, '$Fenster.Schmutzgrad>10 & $Wischeimer.Fuellstand>5'),
(18, 11, ''),
(19, 26, ''),
(21, 27, ''),
(22, 27, ''),
(23, 19, ''),
(24, 27, ''),
(25, 24, ''),
(26, 21, 'DEFAULT'),
(26, 22, '$Fenster.Schmutzgrad<1'),
(26, 25, '$Fenster.Schmutzgrad>10 & $Wischeimer.Fuellstand>5'),
(27, 20, ''),
(28, 35, ''),
(30, 36, ''),
(31, 36, ''),
(32, 28, ''),
(33, 36, ''),
(34, 33, ''),
(35, 30, 'DEFAULT'),
(35, 31, '$Fenster.Schmutzgrad<1'),
(35, 34, '$Fenster.Schmutzgrad>10 & $Wischeimer.Fuellstand>5'),
(36, 29, '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL,
  `modelid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=37 ;

--
-- Daten für Tabelle `controlnode`
--

INSERT INTO `controlnode` (`id`, `label`, `type`, `fragment_id`, `modelid`) VALUES
(1, 'Schmutzgrad pruefen', 'Activity', 1, 902758671),
(2, '', 'Endevent', 1, 944791324),
(3, 'Wischeimer auffuellen', 'Activity', 1, 271828732),
(4, 'Sonne geniessen', 'Activity', 1, 1372382499),
(5, '', 'Startevent', 1, 1444024119),
(6, 'Bestaetigung versenden', 'EmailTask', 1, 1173635380),
(7, 'Fenster putzen', 'Activity', 1, 580330049),
(8, '', 'XOR', 1, 411240190),
(9, '', 'XOR', 1, 987862532),
(10, 'Schmutzgrad pruefen', 'Activity', 2, 902758671),
(11, '', 'Endevent', 2, 944791324),
(12, 'Wischeimer auffuellen', 'Activity', 2, 271828732),
(13, 'Sonne geniessen', 'Activity', 2, 1372382499),
(14, '', 'Startevent', 2, 1444024119),
(15, 'Bestaetigung versenden', 'EmailTask', 2, 1173635380),
(16, 'Fenster putzen', 'Activity', 2, 580330049),
(17, '', 'XOR', 2, 411240190),
(18, '', 'XOR', 2, 987862532),
(19, 'Schmutzgrad pruefen', 'Activity', 3, 902758671),
(20, '', 'Endevent', 3, 944791324),
(21, 'Wischeimer auffuellen', 'Activity', 3, 271828732),
(22, 'Sonne geniessen', 'Activity', 3, 1372382499),
(23, '', 'Startevent', 3, 1444024119),
(24, 'Bestaetigung versenden', 'EmailTask', 3, 1173635380),
(25, 'Fenster putzen', 'Activity', 3, 580330049),
(26, '', 'XOR', 3, 411240190),
(27, '', 'XOR', 3, 987862532),
(28, 'Schmutzgrad pruefen', 'Activity', 4, 902758671),
(29, '', 'Endevent', 4, 944791324),
(30, 'Wischeimer auffuellen', 'Activity', 4, 271828732),
(31, 'Sonne geniessen', 'Activity', 4, 1372382499),
(32, '', 'Startevent', 4, 1444024119),
(33, 'Bestaetigung versenden', 'EmailTask', 4, 1173635380),
(34, 'Fenster putzen', 'Activity', 4, 580330049),
(35, '', 'XOR', 4, 411240190),
(36, '', 'XOR', 4, 987862532);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

--
-- Daten für Tabelle `controlnodeinstance`
--

INSERT INTO `controlnodeinstance` (`id`, `Type`, `controlnode_id`, `fragmentinstance_id`) VALUES
(1, 'Activity', 10, 1),
(2, 'XOR', 17, 1),
(3, 'Activity', 12, 1),
(4, 'XOR', 18, 1),
(5, 'Activity', 10, 2),
(6, 'XOR', 17, 2),
(7, 'Activity', 16, 2),
(8, 'Activity', 15, 2),
(9, 'XOR', 18, 2),
(10, 'Activity', 10, 3),
(11, 'Activity', 10, 4),
(12, 'Activity', 28, 5),
(13, 'XOR', 35, 5),
(14, 'Activity', 28, 6),
(15, 'XOR', 35, 6),
(16, 'Activity', 30, 6),
(17, 'XOR', 36, 6),
(18, 'Activity', 28, 7);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataattribute`
--

CREATE TABLE IF NOT EXISTS `dataattribute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `default` varchar(1024) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Daten für Tabelle `dataattribute`
--

INSERT INTO `dataattribute` (`id`, `name`, `type`, `default`, `dataclass_id`) VALUES
(1, 'Fuellstand', '', '', 1),
(2, 'Schmutzgrad', '', '', 2),
(3, 'Raum', '', '', 2),
(4, 'Fuellstand', '', '', 3),
(5, 'Schmutzgrad', '', '', 4),
(6, 'Raum', '', '', 4),
(7, 'Fuellstand', '', '', 5),
(8, 'Farbe', '', '', 5),
(9, 'Schmutzgrad', '', '', 6),
(10, 'Raum', '', '', 6),
(11, 'Fuellstand', '', '5', 7),
(12, 'Farbe', '', '5', 7),
(13, 'Schmutzgrad', '', '5', 8),
(14, 'Raum', '', '5', 8);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataattributeinstance`
--

CREATE TABLE IF NOT EXISTS `dataattributeinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` varchar(1024) NOT NULL,
  `dataattribute_id` int(11) NOT NULL,
  `dataobjectinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Daten für Tabelle `dataattributeinstance`
--

INSERT INTO `dataattributeinstance` (`id`, `value`, `dataattribute_id`, `dataobjectinstance_id`) VALUES
(1, '11', 5, 1),
(2, 'C2-4', 6, 1),
(3, '11', 4, 2),
(4, '', 5, 3),
(5, '', 6, 3),
(6, '', 4, 4),
(7, '', 13, 5),
(8, '5', 14, 5),
(9, '5', 11, 6),
(10, '5', 12, 6),
(11, '5,', 13, 7),
(12, '5', 14, 7),
(13, '5', 11, 8),
(14, '5', 12, 8);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `rootnode` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `dataclass`
--

INSERT INTO `dataclass` (`id`, `name`, `rootnode`) VALUES
(1, 'Wischeimer', 0),
(2, 'Fenster', 1),
(3, 'Wischeimer', 0),
(4, 'Fenster', 1),
(5, 'Wischeimer', 0),
(6, 'Fenster', 1),
(7, 'Wischeimer', 0),
(8, 'Fenster', 1);

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

--
-- Daten für Tabelle `dataflow`
--

INSERT INTO `dataflow` (`controlnode_id`, `dataset_id`, `input`) VALUES
(1, 1, 1),
(6, 2, 0),
(10, 3, 1),
(15, 4, 0),
(19, 5, 1),
(24, 6, 0),
(28, 7, 1),
(30, 8, 0),
(33, 9, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datanode`
--

CREATE TABLE IF NOT EXISTS `datanode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenario_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `modelid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Daten für Tabelle `datanode`
--

INSERT INTO `datanode` (`id`, `scenario_id`, `state_id`, `dataclass_id`, `dataobject_id`, `modelid`) VALUES
(1, 1, 1, 2, 1, 246829410),
(2, 1, 3, 2, 1, 2062292466),
(3, 1, 2, 1, 2, 1163631230),
(4, 2, 4, 4, 3, 246829410),
(5, 2, 6, 4, 3, 2062292466),
(6, 2, 5, 3, 4, 1163631230),
(7, 3, 10, 6, 5, 246829410),
(8, 3, 12, 6, 5, 2062292466),
(9, 3, 11, 5, 6, 1163631230),
(10, 4, 13, 8, 7, 246829410),
(11, 4, 16, 8, 7, 2062292466),
(12, 4, 14, 7, 8, 1589530268),
(13, 4, 15, 7, 8, 1163631230);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobject`
--

CREATE TABLE IF NOT EXISTS `dataobject` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `start_state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `dataobject`
--

INSERT INTO `dataobject` (`id`, `name`, `dataclass_id`, `scenario_id`, `start_state_id`) VALUES
(1, 'Fenster', 2, 1, 3),
(2, 'Wischeimer', 1, 1, 2),
(3, 'Fenster', 4, 2, 6),
(4, 'Wischeimer', 3, 2, 5),
(5, 'Fenster', 6, 3, 12),
(6, 'Wischeimer', 5, 3, 11),
(7, 'Fenster', 8, 4, 16),
(8, 'Wischeimer', 7, 4, 15);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `dataobjectinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `onchange` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `dataobjectinstance`
--

INSERT INTO `dataobjectinstance` (`id`, `scenarioinstance_id`, `state_id`, `dataobject_id`, `onchange`) VALUES
(1, 1, 4, 3, 0),
(2, 1, 5, 4, 0),
(3, 2, 6, 3, 0),
(4, 2, 5, 4, 0),
(5, 3, 16, 7, 0),
(6, 3, 15, 8, 0),
(7, 4, 16, 7, 0),
(8, 4, 14, 8, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `input` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Daten für Tabelle `dataset`
--

INSERT INTO `dataset` (`id`, `input`) VALUES
(1, 1),
(2, 0),
(3, 1),
(4, 0),
(5, 1),
(6, 0),
(7, 1),
(8, 0),
(9, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datasetconsistsofdatanode`
--

CREATE TABLE IF NOT EXISTS `datasetconsistsofdatanode` (
  `dataset_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL,
  PRIMARY KEY (`dataset_id`,`datanode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `datasetconsistsofdatanode`
--

INSERT INTO `datasetconsistsofdatanode` (`dataset_id`, `datanode_id`) VALUES
(1, 2),
(1, 3),
(2, 1),
(3, 5),
(3, 6),
(4, 4),
(5, 8),
(5, 9),
(6, 7),
(7, 11),
(7, 13),
(8, 12),
(9, 10);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `emailconfiguration`
--

CREATE TABLE IF NOT EXISTS `emailconfiguration` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `receivermailaddress` varchar(1024) NOT NULL,
  `sendmailaddress` varchar(1024) NOT NULL,
  `subject` varchar(2048) NOT NULL,
  `message` text NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Daten für Tabelle `emailconfiguration`
--

INSERT INTO `emailconfiguration` (`id`, `receivermailaddress`, `sendmailaddress`, `subject`, `message`, `controlnode_id`) VALUES
(-1, 'bp2014w1@byom.de', 'bp2014w01@framsteg.org', 'Fensterputz', 'Das Fenster in Buero #Fenster.Raum wurde geputzt.', 0),
(6, 'bp2014w1@byom.de', 'bp2014w01@framsteg.org', 'Fensterputz', 'Das Fenster in Buero #Fenster.Raum wurde geputzt.', 6),
(7, 'frank@frapu.de', 'bp2014w01@framsteg.org', 'Fensterputz', 'Das Fenster in Buero #Fenster.Raum wurde geputzt.', 15),
(8, 'bp2014w1@byom.de', 'bp2014w01@framsteg.org', 'Fensterputz', 'Das Fenster in Buero #Fenster.Raum wurde geputzt.', 24),
(9, 'bp2014w1@byom.de', 'bp2014w01@framsteg.org', 'Fensterputz', 'Das Fenster in Buero #Fenster.Raum wurde geputzt.', 33);

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
  `modelid` bigint(11) NOT NULL DEFAULT '-1',
  `modelversion` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `fragment`
--

INSERT INTO `fragment` (`id`, `name`, `scenario_id`, `modelid`, `modelversion`) VALUES
(1, 'Fenster putzen', 1, 426983565, 0),
(2, 'Fenster putzen', 2, 426983565, 0),
(3, 'Fenster putzen', 3, 426983565, 0),
(4, 'Fenster putzen', 4, 426983565, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Daten für Tabelle `fragmentinstance`
--

INSERT INTO `fragmentinstance` (`id`, `terminated`, `fragment_id`, `scenarioinstance_id`) VALUES
(1, 1, 2, 1),
(2, 1, 2, 1),
(3, 0, 2, 1),
(4, 0, 2, 2),
(5, 0, 4, 3),
(6, 1, 4, 4),
(7, 0, 4, 4);

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

--
-- Daten für Tabelle `gatewayinstance`
--

INSERT INTO `gatewayinstance` (`id`, `type`, `gateway_state`) VALUES
(2, 'XOR', 'init'),
(4, 'XOR', 'executing'),
(6, 'XOR', 'init'),
(9, 'XOR', 'executing'),
(13, 'XOR', 'init'),
(15, 'XOR', 'init'),
(17, 'XOR', 'executing');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `historyactivityinstance`
--

CREATE TABLE IF NOT EXISTS `historyactivityinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activityinstance_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `oldstate` varchar(256) DEFAULT NULL,
  `newstate` varchar(256) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=48 ;

--
-- Daten für Tabelle `historyactivityinstance`
--

INSERT INTO `historyactivityinstance` (`id`, `activityinstance_id`, `timestamp`, `oldstate`, `newstate`, `scenarioinstance_id`) VALUES
(1, 1, '2015-04-27 13:50:22', NULL, 'init', 1),
(2, 1, '2015-04-27 13:50:22', 'init', 'ready(ControlFlow)', 1),
(3, 1, '2015-04-27 13:50:22', 'ready(ControlFlow)', 'ready', 1),
(4, 1, '2015-04-27 13:54:58', 'ready', 'running', 1),
(5, 1, '2015-04-27 13:55:02', 'running', 'terminated', 1),
(6, 3, '2015-04-27 13:55:02', NULL, 'init', 1),
(7, 3, '2015-04-27 13:55:02', 'init', 'ready(ControlFlow)', 1),
(8, 3, '2015-04-27 13:55:02', 'ready(ControlFlow)', 'ready', 1),
(9, 3, '2015-04-27 13:55:32', 'ready', 'running', 1),
(10, 3, '2015-04-27 13:56:21', 'running', 'terminated', 1),
(11, 5, '2015-04-27 13:56:21', NULL, 'init', 1),
(12, 5, '2015-04-27 13:56:21', 'init', 'ready(ControlFlow)', 1),
(13, 5, '2015-04-27 13:56:21', 'ready(ControlFlow)', 'ready', 1),
(14, 5, '2015-04-27 13:56:24', 'ready', 'running', 1),
(15, 5, '2015-04-27 13:56:27', 'running', 'terminated', 1),
(16, 7, '2015-04-27 13:56:27', NULL, 'init', 1),
(17, 7, '2015-04-27 13:56:27', 'init', 'ready(ControlFlow)', 1),
(18, 7, '2015-04-27 13:56:27', 'ready(ControlFlow)', 'ready', 1),
(19, 7, '2015-04-27 13:59:00', 'ready', 'running', 1),
(20, 7, '2015-04-27 13:59:04', 'running', 'terminated', 1),
(21, 8, '2015-04-27 13:59:04', NULL, 'init', 1),
(22, 8, '2015-04-27 13:59:04', 'init', 'ready(ControlFlow)', 1),
(23, 8, '2015-04-27 13:59:04', 'ready(ControlFlow)', 'ready', 1),
(24, 8, '2015-04-27 13:59:04', 'ready', 'running', 1),
(25, 8, '2015-04-27 13:59:05', 'running', 'terminated', 1),
(26, 10, '2015-04-27 13:59:05', NULL, 'init', 1),
(27, 10, '2015-04-27 13:59:05', 'init', 'ready(ControlFlow)', 1),
(28, 11, '2015-04-27 14:07:14', NULL, 'init', 2),
(29, 11, '2015-04-27 14:07:15', 'init', 'ready(ControlFlow)', 2),
(30, 11, '2015-04-27 14:07:15', 'ready(ControlFlow)', 'ready', 2),
(31, 12, '2015-04-27 14:16:33', NULL, 'init', 3),
(32, 12, '2015-04-27 14:16:33', 'init', 'ready(ControlFlow)', 3),
(33, 12, '2015-04-27 14:16:33', 'ready(ControlFlow)', 'ready', 3),
(34, 12, '2015-04-27 14:17:12', 'ready', 'running', 3),
(35, 12, '2015-04-27 14:17:15', 'running', 'terminated', 3),
(36, 14, '2015-04-27 14:18:48', NULL, 'init', 4),
(37, 14, '2015-04-27 14:18:48', 'init', 'ready(ControlFlow)', 4),
(38, 14, '2015-04-27 14:18:48', 'ready(ControlFlow)', 'ready', 4),
(39, 14, '2015-04-27 14:18:51', 'ready', 'running', 4),
(40, 14, '2015-04-27 14:18:54', 'running', 'terminated', 4),
(41, 16, '2015-04-27 14:18:55', NULL, 'init', 4),
(42, 16, '2015-04-27 14:18:55', 'init', 'ready(ControlFlow)', 4),
(43, 16, '2015-04-27 14:18:55', 'ready(ControlFlow)', 'ready', 4),
(44, 16, '2015-04-27 14:19:01', 'ready', 'running', 4),
(45, 16, '2015-04-27 14:23:05', 'running', 'terminated', 4),
(46, 18, '2015-04-27 14:23:05', NULL, 'init', 4),
(47, 18, '2015-04-27 14:23:05', 'init', 'ready(ControlFlow)', 4);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `historydataattributeinstance`
--

CREATE TABLE IF NOT EXISTS `historydataattributeinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataattributeinstance_id` int(11) NOT NULL,
  `oldvalue` varchar(256) DEFAULT NULL,
  `newvalue` varchar(256) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scenarioinstance_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `historydataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `historydataobjectinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scenarioinstance_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `dataobjectinstance_id` int(11) NOT NULL,
  `oldstate_id` int(11) DEFAULT NULL,
  `newstate_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Daten für Tabelle `historydataobjectinstance`
--

INSERT INTO `historydataobjectinstance` (`id`, `scenarioinstance_id`, `timestamp`, `dataobjectinstance_id`, `oldstate_id`, `newstate_id`) VALUES
(1, 1, '2015-04-27 13:50:22', 1, NULL, 6),
(2, 1, '2015-04-27 13:50:22', 2, NULL, 5),
(3, 1, '2015-04-27 13:59:05', 1, 6, 4),
(4, 2, '2015-04-27 14:07:14', 3, NULL, 6),
(5, 2, '2015-04-27 14:07:14', 4, NULL, 5),
(6, 3, '2015-04-27 14:16:33', 5, NULL, 16),
(7, 3, '2015-04-27 14:16:33', 6, NULL, 15),
(8, 4, '2015-04-27 14:18:48', 7, NULL, 16),
(9, 4, '2015-04-27 14:18:48', 8, NULL, 15),
(10, 4, '2015-04-27 14:23:05', 8, 15, 14);

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
  `deleted` tinyint(1) NOT NULL DEFAULT '0',
  `modelid` bigint(11) DEFAULT '-1',
  `modelversion` int(11) NOT NULL DEFAULT '-1',
  `datamodelid` bigint(11) DEFAULT NULL,
  `datamodelversion` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `scenario`
--

INSERT INTO `scenario` (`id`, `name`, `deleted`, `modelid`, `modelversion`, `datamodelid`, `datamodelversion`) VALUES
(1, 'Fensterputz', 1, 291164789, 1, 1541659030, 0),
(2, 'Fensterputz', 0, 291164789, 1, 1541659030, 0),
(3, 'Fensterputz', 0, 291164789, 1, 1541659030, 1),
(4, 'Fensterputz', 0, 291164789, 1, 1541659030, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) DEFAULT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `scenarioinstance`
--

INSERT INTO `scenarioinstance` (`id`, `name`, `terminated`, `scenario_id`) VALUES
(1, 'Fensterputz', 1, 2),
(2, 'Fensterputz', 0, 2),
(3, 'Fensterputz', 0, 4),
(4, 'Fensterputz', 0, 4);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `state`
--

CREATE TABLE IF NOT EXISTS `state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Daten für Tabelle `state`
--

INSERT INTO `state` (`id`, `name`, `olc_id`) VALUES
(1, 'geputzt', 2),
(2, 'init', 1),
(3, 'init', 2),
(4, 'geputzt', 4),
(5, 'init', 3),
(6, 'init', 4),
(7, 'geputzt', -1),
(8, 'init', -1),
(9, 'init', -1),
(10, 'geputzt', 6),
(11, 'init', 5),
(12, 'init', 6),
(13, 'geputzt', 8),
(14, 'gefaerbt', 7),
(15, 'init', 7),
(16, 'init', 8);

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
  `conditionset_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  PRIMARY KEY (`conditionset_id`,`dataobject_id`,`state_id`),
  KEY `dataobject_id` (`dataobject_id`),
  KEY `state_id` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `terminationcondition`
--

INSERT INTO `terminationcondition` (`conditionset_id`, `dataobject_id`, `state_id`, `scenario_id`) VALUES
(1, 1, 1, 1),
(1, 3, 4, 2),
(1, 5, 10, 3),
(1, 7, 13, 4);

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
