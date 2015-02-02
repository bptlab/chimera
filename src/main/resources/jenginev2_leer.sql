-- phpMyAdmin SQL Dump
-- version 4.2.11
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 02. Feb 2015 um 14:50
-- Server Version: 5.6.21
-- PHP-Version: 5.6.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `jenginev2`
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `activitystate`
--

CREATE TABLE IF NOT EXISTS `activitystate` (
  `state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `configuration`
--

CREATE TABLE IF NOT EXISTS `configuration` (
`id` int(11) NOT NULL,
  `behaviourdata` varchar(1024) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlflow`
--

CREATE TABLE IF NOT EXISTS `controlflow` (
  `controlnode_id1` int(11) NOT NULL,
  `controlnode_id2` int(11) NOT NULL,
  `condition` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
`id` int(11) NOT NULL,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
`id` int(11) NOT NULL,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataattributeinstance`
--

CREATE TABLE IF NOT EXISTS `dataattributeinstance` (
`id` int(11) NOT NULL,
  `value` varchar(1024) NOT NULL,
  `dataattribute_id` int(11) NOT NULL,
  `dataobjectinstance_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `dataobjectinstance` (
`id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
`id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `datasetconsistsofdatanode`
--

CREATE TABLE IF NOT EXISTS `datasetconsistsofdatanode` (
  `dataset_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `emailconfiguration`
--

CREATE TABLE IF NOT EXISTS `emailconfiguration` (
`id` int(11) NOT NULL,
  `receivermailaddress` varchar(1024) NOT NULL,
  `subject` varchar(2048) NOT NULL,
  `message` text NOT NULL,
  `controlnode_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventinstance`
--

CREATE TABLE IF NOT EXISTS `eventinstance` (
`id` int(11) NOT NULL,
  `type` varchar(512) NOT NULL,
  `event_state` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `eventlog`
--

CREATE TABLE IF NOT EXISTS `eventlog` (
`id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `controlnodeinstance_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragment`
--

CREATE TABLE IF NOT EXISTS `fragment` (
`id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
`id` int(11) NOT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Tabellenstruktur für Tabelle `historyactivityinstance`
--

CREATE TABLE IF NOT EXISTS `historyactivityinstance` (
`ID` int(11) NOT NULL,
  `activityinstance_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `oldstate` varchar(512) NOT NULL,
  `newstate` varchar(512) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `comment` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `historycontrolflow`
--

CREATE TABLE IF NOT EXISTS `historycontrolflow` (
`id` int(11) NOT NULL,
  `controlnodeinstance_id1` int(11) NOT NULL,
  `controlnodeinstance_id2` int(11) NOT NULL
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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `role`
--

CREATE TABLE IF NOT EXISTS `role` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
`id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `state`
--

CREATE TABLE IF NOT EXISTS `state` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `activityinstance`
--
ALTER TABLE `activityinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `activitystate`
--
ALTER TABLE `activitystate`
 ADD PRIMARY KEY (`state`);

--
-- Indizes für die Tabelle `configuration`
--
ALTER TABLE `configuration`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `controlflow`
--
ALTER TABLE `controlflow`
 ADD PRIMARY KEY (`controlnode_id1`,`controlnode_id2`);

--
-- Indizes für die Tabelle `controlnode`
--
ALTER TABLE `controlnode`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataattribute`
--
ALTER TABLE `dataattribute`
 ADD PRIMARY KEY (`ID`);

--
-- Indizes für die Tabelle `dataattributeinstance`
--
ALTER TABLE `dataattributeinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataclass`
--
ALTER TABLE `dataclass`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataflow`
--
ALTER TABLE `dataflow`
 ADD PRIMARY KEY (`controlnode_id`,`dataset_id`);

--
-- Indizes für die Tabelle `datanode`
--
ALTER TABLE `datanode`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataobject`
--
ALTER TABLE `dataobject`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `dataset`
--
ALTER TABLE `dataset`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `datasetconsistsofdatanode`
--
ALTER TABLE `datasetconsistsofdatanode`
 ADD PRIMARY KEY (`dataset_id`,`datanode_id`);

--
-- Indizes für die Tabelle `emailconfiguration`
--
ALTER TABLE `emailconfiguration`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `eventinstance`
--
ALTER TABLE `eventinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `eventlog`
--
ALTER TABLE `eventlog`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `fragment`
--
ALTER TABLE `fragment`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `gatewayandeventstate`
--
ALTER TABLE `gatewayandeventstate`
 ADD PRIMARY KEY (`state`);

--
-- Indizes für die Tabelle `gatewayinstance`
--
ALTER TABLE `gatewayinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `historyactivityinstance`
--
ALTER TABLE `historyactivityinstance`
 ADD PRIMARY KEY (`ID`);

--
-- Indizes für die Tabelle `historycontrolflow`
--
ALTER TABLE `historycontrolflow`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `reference`
--
ALTER TABLE `reference`
 ADD PRIMARY KEY (`controlnode_id1`,`controlnode_id2`);

--
-- Indizes für die Tabelle `right`
--
ALTER TABLE `right`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `role`
--
ALTER TABLE `role`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `rolehasright`
--
ALTER TABLE `rolehasright`
 ADD PRIMARY KEY (`role_id`,`right_id`);

--
-- Indizes für die Tabelle `scenario`
--
ALTER TABLE `scenario`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `scenarioinstance`
--
ALTER TABLE `scenarioinstance`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `state`
--
ALTER TABLE `state`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `statetransition`
--
ALTER TABLE `statetransition`
 ADD PRIMARY KEY (`state_id1`,`state_id2`);

--
-- Indizes für die Tabelle `successor`
--
ALTER TABLE `successor`
 ADD PRIMARY KEY (`controlnodeinstance_id1`,`controlnodeinstance_id2`);

--
-- Indizes für die Tabelle `terminationcondition`
--
ALTER TABLE `terminationcondition`
 ADD PRIMARY KEY (`id`,`dataclass_id`,`state_id`);

--
-- Indizes für die Tabelle `user`
--
ALTER TABLE `user`
 ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `userismemberofrole`
--
ALTER TABLE `userismemberofrole`
 ADD PRIMARY KEY (`user_id`,`role_id`);

--
-- Indizes für die Tabelle `workitemstate`
--
ALTER TABLE `workitemstate`
 ADD PRIMARY KEY (`state`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `activityinstance`
--
ALTER TABLE `activityinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `configuration`
--
ALTER TABLE `configuration`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `controlnode`
--
ALTER TABLE `controlnode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataattribute`
--
ALTER TABLE `dataattribute`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataattributeinstance`
--
ALTER TABLE `dataattributeinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataclass`
--
ALTER TABLE `dataclass`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `datanode`
--
ALTER TABLE `datanode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataobject`
--
ALTER TABLE `dataobject`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `dataset`
--
ALTER TABLE `dataset`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `emailconfiguration`
--
ALTER TABLE `emailconfiguration`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `eventinstance`
--
ALTER TABLE `eventinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `eventlog`
--
ALTER TABLE `eventlog`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `fragment`
--
ALTER TABLE `fragment`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `historyactivityinstance`
--
ALTER TABLE `historyactivityinstance`
MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `historycontrolflow`
--
ALTER TABLE `historycontrolflow`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `right`
--
ALTER TABLE `right`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `role`
--
ALTER TABLE `role`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `scenario`
--
ALTER TABLE `scenario`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `scenarioinstance`
--
ALTER TABLE `scenarioinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `state`
--
ALTER TABLE `state`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
