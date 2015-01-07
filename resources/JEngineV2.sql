-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 05. Jan 2015 um 16:49
-- Server Version: 5.6.20
-- PHP-Version: 5.5.15

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
`id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `role_id` int(11) NOT NULL,
  `activity_state` varchar(512) NOT NULL,
  `workitem_state` varchar(512) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=116 ;

--
-- Daten für Tabelle `activityinstance`
--

INSERT INTO `activityinstance` (`id`, `type`, `role_id`, `activity_state`, `workitem_state`) VALUES
(76, 'HumanTask', 1, 'terminated', 'init'),
(77, 'HumanTask', 1, 'ready', 'init'),
(78, 'HumanTask', 1, 'ready', 'init'),
(79, 'HumanTask', 1, 'terminated', 'init'),
(80, 'HumanTask', 1, 'terminated', 'init'),
(81, 'HumanTask', 1, 'terminated', 'init'),
(82, 'HumanTask', 1, 'terminated', 'init'),
(83, 'HumanTask', 1, 'terminated', 'init'),
(84, 'HumanTask', 1, 'terminated', 'init'),
(85, 'HumanTask', 1, 'terminated', 'init'),
(86, 'HumanTask', 1, 'terminated', 'init'),
(87, 'HumanTask', 1, 'terminated', 'init'),
(88, 'HumanTask', 1, 'ready', 'init'),
(89, 'HumanTask', 1, 'terminated', 'init'),
(90, 'HumanTask', 1, 'terminated', 'init'),
(91, 'HumanTask', 1, 'ready', 'init'),
(92, 'HumanTask', 1, 'terminated', 'init'),
(93, 'HumanTask', 1, 'running', 'init'),
(94, 'HumanTask', 1, 'ready', 'init'),
(95, 'HumanTask', 1, 'ready', 'init'),
(96, 'HumanTask', 1, 'running', 'init'),
(97, 'HumanTask', 1, 'ready', 'init'),
(98, 'HumanTask', 1, 'running', 'init'),
(99, 'HumanTask', 1, 'ready', 'init'),
(100, 'HumanTask', 1, 'terminated', 'init'),
(101, 'HumanTask', 1, 'ready', 'init'),
(102, 'HumanTask', 1, 'terminated', 'init'),
(103, 'HumanTask', 1, 'terminated', 'init'),
(104, 'HumanTask', 1, 'ready', 'init'),
(105, 'HumanTask', 1, 'ready', 'init'),
(106, 'HumanTask', 1, 'ready', 'init'),
(107, 'HumanTask', 1, 'ready', 'init'),
(108, 'HumanTask', 1, 'ready', 'init'),
(109, 'HumanTask', 1, 'ready', 'init'),
(110, 'HumanTask', 1, 'ready', 'init'),
(111, 'HumanTask', 1, 'ready', 'init'),
(112, 'HumanTask', 1, 'ready', 'init'),
(113, 'HumanTask', 1, 'ready', 'init'),
(114, 'HumanTask', 1, 'ready', 'init'),
(115, 'HumanTask', 1, 'ready', 'init');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `activitystate`
--

CREATE TABLE IF NOT EXISTS `activitystate` (
  `state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `activitystate`
--

INSERT INTO `activitystate` (`state`) VALUES
('init'),
('ready'),
('ready(ControlFlow)'),
('ready(Data)'),
('running'),
('skipped'),
('terminated');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `configuration`
--

CREATE TABLE IF NOT EXISTS `configuration` (
`id` int(11) NOT NULL,
  `behaviourdata` varchar(1024) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlflow`
--

CREATE TABLE IF NOT EXISTS `controlflow` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  `condition` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `controlflow`
--

INSERT INTO `controlflow` (`controlnode_id1`, `controlnode_id2`, `condition`) VALUES
(1, 2, ''),
(2, 5, ''),
(3, 4, ''),
(4, 8, ''),
(5, 6, ''),
(6, 7, '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
`id` int(11) NOT NULL,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `controlnode`
--

INSERT INTO `controlnode` (`id`, `label`, `type`, `fragment_id`) VALUES
(1, 'StartEventFragment1', 'Startevent', 1),
(2, 'Activity1Fragment1', 'Activity', 1),
(3, 'StartEventFragment2', 'Startevent', 2),
(4, 'Activity1Fragment2', 'Activity', 2),
(5, 'Activity2Fragment1', 'Activity', 1),
(6, 'Activity2Fragment1', 'Activity', 1),
(7, 'Endevent1Fragment1', 'Endevent', 1),
(8, 'Endevent1Fragment2', 'Endevent', 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
`id` int(11) NOT NULL,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=116 ;

--
-- Daten für Tabelle `controlnodeinstance`
--

INSERT INTO `controlnodeinstance` (`id`, `Type`, `controlnode_id`, `fragmentinstance_id`) VALUES
(76, 'Activity', 2, 78),
(77, 'Activity', 4, 79),
(78, 'Activity', 5, 78),
(79, 'Activity', 76, 78),
(80, 'Activity', 78, 78),
(81, 'Activity', 77, 79),
(82, 'Activity', 79, 78),
(83, 'Activity', 80, 78),
(84, 'Activity', 81, 79),
(85, 'Activity', 2, 80),
(86, 'Activity', 4, 81),
(87, 'Activity', 5, 80),
(88, 'Activity', 4, 82),
(89, 'Activity', 6, 80),
(90, 'Activity', 2, 83),
(91, 'Activity', 5, 83),
(92, 'Activity', 2, 84),
(93, 'Activity', 4, 85),
(94, 'Activity', 5, 84),
(95, 'Activity', 2, 86),
(96, 'Activity', 4, 87),
(97, 'Activity', 2, 88),
(98, 'Activity', 4, 89),
(99, 'Activity', 2, 90),
(100, 'Activity', 4, 91),
(101, 'Activity', 4, 92),
(102, 'Activity', 2, 93),
(103, 'Activity', 4, 94),
(104, 'Activity', 4, 95),
(105, 'Activity', 5, 93),
(106, 'Activity', 2, 96),
(107, 'Activity', 4, 97),
(108, 'Activity', 2, 98),
(109, 'Activity', 4, 99),
(110, 'Activity', 2, 100),
(111, 'Activity', 4, 101),
(112, 'Activity', 2, 102),
(113, 'Activity', 4, 103),
(114, 'Activity', 2, 104),
(115, 'Activity', 4, 105);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataattribute`
--

CREATE TABLE IF NOT EXISTS `dataattribute` (
`ID` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `type` varchar(256) NOT NULL,
  `default` varchar(1024) NOT NULL,
  `dataclass_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Daten für Tabelle `dataclass`
--

INSERT INTO `dataclass` (`id`, `name`) VALUES
(1, 'obejct1'),
(2, 'object2');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataflow`
--

CREATE TABLE IF NOT EXISTS `dataflow` (
  `controlnode_id` int(11) NOT NULL,
  `dataset_id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datanode`
--

CREATE TABLE IF NOT EXISTS `datanode` (
`id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `datanode`
--

INSERT INTO `datanode` (`id`, `scenario_id`, `state_id`, `dataclass_id`, `dataobject_id`) VALUES
(1, 1, 1, 1, 1),
(2, 1, 2, 1, 1),
(3, 1, 2, 1, 1),
(4, 1, 3, 1, 1),
(5, 1, 3, 1, 1),
(6, 1, 4, 1, 1),
(7, 1, 5, 2, 2),
(8, 1, 6, 2, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobject`
--

CREATE TABLE IF NOT EXISTS `dataobject` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `start_state_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Daten für Tabelle `dataobject`
--

INSERT INTO `dataobject` (`id`, `name`, `dataclass_id`, `scenario_id`, `start_state_id`) VALUES
(1, 'object1', 1, 1, 1),
(2, 'object2', 2, 1, 5);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `dataobjectinstance` (
`id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Daten für Tabelle `dataobjectinstance`
--

INSERT INTO `dataobjectinstance` (`id`, `scenarioinstance_id`, `state_id`, `dataobject_id`) VALUES
(7, 62, 1, 1),
(8, 62, 5, 2),
(9, 63, 1, 1),
(10, 63, 5, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
`id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `dataset`
--

INSERT INTO `dataset` (`id`, `input`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 1),
(8, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datasetconsistsofdatanode`
--

CREATE TABLE IF NOT EXISTS `datasetconsistsofdatanode` (
  `dataset_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `datasetconsistsofdatanode`
--

INSERT INTO `datasetconsistsofdatanode` (`dataset_id`, `datanode_id`) VALUES
(1, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventinstance`
--

CREATE TABLE IF NOT EXISTS `eventinstance` (
`id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `event_state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventlog`
--

CREATE TABLE IF NOT EXISTS `eventlog` (
`id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `controlnodeinstance_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragment`
--

CREATE TABLE IF NOT EXISTS `fragment` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Daten für Tabelle `fragment`
--

INSERT INTO `fragment` (`id`, `name`, `scenario_id`) VALUES
(1, 'fragment1', 1),
(2, 'fragment2', 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
`id` int(11) NOT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=106 ;

--
-- Daten für Tabelle `fragmentinstance`
--

INSERT INTO `fragmentinstance` (`id`, `terminated`, `fragment_id`, `scenarioinstance_id`) VALUES
(78, 0, 1, 47),
(79, 0, 2, 47),
(80, 1, 1, 53),
(81, 1, 2, 53),
(82, 0, 2, 53),
(83, 0, 1, 53),
(84, 0, 1, 54),
(85, 0, 2, 54),
(86, 0, 1, 55),
(87, 0, 2, 55),
(88, 0, 1, 56),
(89, 0, 2, 56),
(90, 0, 1, 57),
(91, 1, 2, 57),
(92, 0, 2, 57),
(93, 0, 1, 58),
(94, 1, 2, 58),
(95, 0, 2, 58),
(96, 0, 1, 59),
(97, 0, 2, 59),
(98, 0, 1, 60),
(99, 0, 2, 60),
(100, 0, 1, 61),
(101, 0, 2, 61),
(102, 0, 1, 62),
(103, 0, 2, 62),
(104, 0, 1, 63),
(105, 0, 2, 63);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gatewayandeventstate`
--

CREATE TABLE IF NOT EXISTS `gatewayandeventstate` (
  `state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `gatewayinstance`
--

CREATE TABLE IF NOT EXISTS `gatewayinstance` (
  `id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `gateway_state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `reference`
--

CREATE TABLE IF NOT EXISTS `reference` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `right`
--

CREATE TABLE IF NOT EXISTS `right` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `role`
--

CREATE TABLE IF NOT EXISTS `role` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rolehasright`
--

CREATE TABLE IF NOT EXISTS `rolehasright` (
  `role_id` int(11) NOT NULL,
  `right_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenario`
--

CREATE TABLE IF NOT EXISTS `scenario` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Daten für Tabelle `scenario`
--

INSERT INTO `scenario` (`id`, `name`) VALUES
(1, 'HELLOWORLD');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
`id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=64 ;

--
-- Daten für Tabelle `scenarioinstance`
--

INSERT INTO `scenarioinstance` (`id`, `scenario_id`) VALUES
(47, 1),
(48, 1),
(49, 1),
(50, 1),
(51, 1),
(52, 1),
(53, 1),
(54, 1),
(55, 1),
(56, 1),
(57, 1),
(58, 1),
(59, 1),
(60, 1),
(61, 1),
(62, 1),
(63, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `state`
--

CREATE TABLE IF NOT EXISTS `state` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=7 ;

--
-- Daten für Tabelle `state`
--

INSERT INTO `state` (`id`, `name`, `olc_id`) VALUES
(1, 'init', 1),
(2, 'bearbeitet', 1),
(3, 'geprüft', 1),
(4, 'abgeschlossen', 1),
(5, 'init', 2),
(6, 'fertig', 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `statetransition`
--

CREATE TABLE IF NOT EXISTS `statetransition` (
  `state_id1` int(11) NOT NULL,
  `state_id2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `successor`
--

CREATE TABLE IF NOT EXISTS `successor` (
  `controlnodeinstance_id1` int(11) NOT NULL,
  `controlnodeinstance_id2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `terminationcondition`
--

CREATE TABLE IF NOT EXISTS `terminationcondition` (
  `id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `user`
--

CREATE TABLE IF NOT EXISTS `user` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL,
  `email` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userismemberofrole`
--

CREATE TABLE IF NOT EXISTS `userismemberofrole` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `workitemstate`
--

CREATE TABLE IF NOT EXISTS `workitemstate` (
  `state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `activityinstance`
--
ALTER TABLE `activityinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `activitystate`
--
ALTER TABLE `activitystate`
 ADD PRIMARY KEY (`state`);

--
-- Indexes for table `configuration`
--
ALTER TABLE `configuration`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `controlflow`
--
ALTER TABLE `controlflow`
 ADD PRIMARY KEY (`controlnode_id1`,`controlnode_id2`);

--
-- Indexes for table `controlnode`
--
ALTER TABLE `controlnode`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dataattribute`
--
ALTER TABLE `dataattribute`
 ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `dataclass`
--
ALTER TABLE `dataclass`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dataflow`
--
ALTER TABLE `dataflow`
 ADD PRIMARY KEY (`controlnode_id`,`dataset_id`);

--
-- Indexes for table `datanode`
--
ALTER TABLE `datanode`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dataobject`
--
ALTER TABLE `dataobject`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `dataset`
--
ALTER TABLE `dataset`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `datasetconsistsofdatanode`
--
ALTER TABLE `datasetconsistsofdatanode`
 ADD PRIMARY KEY (`dataset_id`,`datanode_id`);

--
-- Indexes for table `eventinstance`
--
ALTER TABLE `eventinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `eventlog`
--
ALTER TABLE `eventlog`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fragment`
--
ALTER TABLE `fragment`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `gatewayandeventstate`
--
ALTER TABLE `gatewayandeventstate`
 ADD PRIMARY KEY (`state`);

--
-- Indexes for table `gatewayinstance`
--
ALTER TABLE `gatewayinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `reference`
--
ALTER TABLE `reference`
 ADD PRIMARY KEY (`controlnode_id1`,`controlnode_id2`);

--
-- Indexes for table `right`
--
ALTER TABLE `right`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rolehasright`
--
ALTER TABLE `rolehasright`
 ADD PRIMARY KEY (`role_id`,`right_id`);

--
-- Indexes for table `scenario`
--
ALTER TABLE `scenario`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `scenarioinstance`
--
ALTER TABLE `scenarioinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `state`
--
ALTER TABLE `state`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `statetransition`
--
ALTER TABLE `statetransition`
 ADD PRIMARY KEY (`state_id1`,`state_id2`);

--
-- Indexes for table `successor`
--
ALTER TABLE `successor`
 ADD PRIMARY KEY (`controlnodeinstance_id1`,`controlnodeinstance_id2`);

--
-- Indexes for table `terminationcondition`
--
ALTER TABLE `terminationcondition`
 ADD PRIMARY KEY (`id`,`dataclass_id`,`state_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- Indexes for table `userismemberofrole`
--
ALTER TABLE `userismemberofrole`
 ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- Indexes for table `workitemstate`
--
ALTER TABLE `workitemstate`
 ADD PRIMARY KEY (`state`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `activityinstance`
--
ALTER TABLE `activityinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=116;
--
-- AUTO_INCREMENT for table `configuration`
--
ALTER TABLE `configuration`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `controlnode`
--
ALTER TABLE `controlnode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=116;
--
-- AUTO_INCREMENT for table `dataattribute`
--
ALTER TABLE `dataattribute`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `dataclass`
--
ALTER TABLE `dataclass`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `datanode`
--
ALTER TABLE `datanode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `dataobject`
--
ALTER TABLE `dataobject`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `dataset`
--
ALTER TABLE `dataset`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `eventinstance`
--
ALTER TABLE `eventinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `eventlog`
--
ALTER TABLE `eventlog`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `fragment`
--
ALTER TABLE `fragment`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=106;
--
-- AUTO_INCREMENT for table `right`
--
ALTER TABLE `right`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `scenario`
--
ALTER TABLE `scenario`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `scenarioinstance`
--
ALTER TABLE `scenarioinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=64;
--
-- AUTO_INCREMENT for table `state`
--
ALTER TABLE `state`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
