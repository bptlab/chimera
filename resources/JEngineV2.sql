-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 17. Dez 2014 um 10:34
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=92 ;

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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
`id` int(11) NOT NULL,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=92 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataclass`
--

CREATE TABLE IF NOT EXISTS `dataclass` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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
  `name` varchar(512) NOT NULL,
  `scenario_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataclass_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobject`
--

CREATE TABLE IF NOT EXISTS `dataobject` (
`id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `datanode_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
`id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
`id` int(11) NOT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=84 ;

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

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
`id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=54 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `state`
--

CREATE TABLE IF NOT EXISTS `state` (
`id` int(11) NOT NULL,
  `name` varchar(512) NOT NULL,
  `olc_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=92;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=92;
--
-- AUTO_INCREMENT for table `dataclass`
--
ALTER TABLE `dataclass`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `datanode`
--
ALTER TABLE `datanode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `dataobject`
--
ALTER TABLE `dataobject`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `dataset`
--
ALTER TABLE `dataset`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=84;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=54;
--
-- AUTO_INCREMENT for table `state`
--
ALTER TABLE `state`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
