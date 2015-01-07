-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 07. Jan 2015 um 17:05
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=244 ;

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
(115, 'HumanTask', 1, 'ready', 'init'),
(116, 'HumanTask', 1, 'ready', 'init'),
(117, 'HumanTask', 1, 'terminated', 'init'),
(118, 'HumanTask', 1, 'ready', 'init'),
(119, 'HumanTask', 1, 'terminated', 'init'),
(120, 'HumanTask', 1, 'terminated', 'init'),
(121, 'HumanTask', 1, 'ready', 'init'),
(122, 'HumanTask', 1, 'ready', 'init'),
(123, 'HumanTask', 1, 'terminated', 'init'),
(124, 'HumanTask', 1, 'ready', 'init'),
(125, 'HumanTask', 1, 'ready', 'init'),
(126, 'HumanTask', 1, 'terminated', 'init'),
(127, 'HumanTask', 1, 'terminated', 'init'),
(128, 'HumanTask', 1, 'ready', 'init'),
(129, 'HumanTask', 1, 'ready', 'init'),
(130, 'HumanTask', 1, 'terminated', 'init'),
(131, 'HumanTask', 1, 'terminated', 'init'),
(132, 'HumanTask', 1, 'terminated', 'init'),
(133, 'HumanTask', 1, 'terminated', 'init'),
(134, 'HumanTask', 1, 'terminated', 'init'),
(135, 'HumanTask', 1, 'terminated', 'init'),
(136, 'HumanTask', 1, 'terminated', 'init'),
(137, 'HumanTask', 1, 'terminated', 'init'),
(138, 'HumanTask', 1, 'terminated', 'init'),
(139, 'HumanTask', 1, 'terminated', 'init'),
(140, 'HumanTask', 1, 'terminated', 'init'),
(141, 'HumanTask', 1, 'terminated', 'init'),
(142, 'HumanTask', 1, 'terminated', 'init'),
(143, 'HumanTask', 1, 'terminated', 'init'),
(144, 'HumanTask', 1, 'terminated', 'init'),
(145, 'HumanTask', 1, 'terminated', 'init'),
(146, 'HumanTask', 1, 'terminated', 'init'),
(147, 'HumanTask', 1, 'terminated', 'init'),
(148, 'HumanTask', 1, 'terminated', 'init'),
(149, 'HumanTask', 1, 'terminated', 'init'),
(150, 'HumanTask', 1, 'terminated', 'init'),
(151, 'HumanTask', 1, 'terminated', 'init'),
(152, 'HumanTask', 1, 'terminated', 'init'),
(153, 'HumanTask', 1, 'terminated', 'init'),
(154, 'HumanTask', 1, 'terminated', 'init'),
(155, 'HumanTask', 1, 'terminated', 'init'),
(156, 'HumanTask', 1, 'terminated', 'init'),
(157, 'HumanTask', 1, 'ready', 'init'),
(158, 'HumanTask', 1, 'ready', 'init'),
(159, 'HumanTask', 1, 'terminated', 'init'),
(160, 'HumanTask', 1, 'terminated', 'init'),
(161, 'HumanTask', 1, 'terminated', 'init'),
(162, 'HumanTask', 1, 'terminated', 'init'),
(163, 'HumanTask', 1, 'terminated', 'init'),
(164, 'HumanTask', 1, 'terminated', 'init'),
(165, 'HumanTask', 1, 'terminated', 'init'),
(166, 'HumanTask', 1, 'terminated', 'init'),
(167, 'HumanTask', 1, 'ready', 'init'),
(168, 'HumanTask', 1, 'ready', 'init'),
(169, 'HumanTask', 1, 'terminated', 'init'),
(170, 'HumanTask', 1, 'terminated', 'init'),
(171, 'HumanTask', 1, 'terminated', 'init'),
(172, 'HumanTask', 1, 'terminated', 'init'),
(173, 'HumanTask', 1, 'terminated', 'init'),
(174, 'HumanTask', 1, 'terminated', 'init'),
(175, 'HumanTask', 1, 'terminated', 'init'),
(176, 'HumanTask', 1, 'terminated', 'init'),
(177, 'HumanTask', 1, 'terminated', 'init'),
(178, 'HumanTask', 1, 'terminated', 'init'),
(179, 'HumanTask', 1, 'ready', 'init'),
(180, 'HumanTask', 1, 'ready', 'init'),
(181, 'HumanTask', 1, 'terminated', 'init'),
(182, 'HumanTask', 1, 'terminated', 'init'),
(183, 'HumanTask', 1, 'terminated', 'init'),
(184, 'HumanTask', 1, 'terminated', 'init'),
(185, 'HumanTask', 1, 'ready', 'init'),
(186, 'HumanTask', 1, 'ready', 'init'),
(187, 'HumanTask', 1, 'terminated', 'init'),
(188, 'HumanTask', 1, 'terminated', 'init'),
(189, 'HumanTask', 1, 'ready', 'init'),
(190, 'HumanTask', 1, 'terminated', 'init'),
(191, 'HumanTask', 1, 'terminated', 'init'),
(192, 'HumanTask', 1, 'terminated', 'init'),
(193, 'HumanTask', 1, 'terminated', 'init'),
(194, 'HumanTask', 1, 'ready', 'init'),
(195, 'HumanTask', 1, 'terminated', 'init'),
(196, 'HumanTask', 1, 'terminated', 'init'),
(197, 'HumanTask', 1, 'terminated', 'init'),
(198, 'HumanTask', 1, 'terminated', 'init'),
(199, 'HumanTask', 1, 'terminated', 'init'),
(200, 'HumanTask', 1, 'terminated', 'init'),
(201, 'HumanTask', 1, 'ready', 'init'),
(202, 'HumanTask', 1, 'ready', 'init'),
(203, 'HumanTask', 1, 'ready', 'init'),
(204, 'HumanTask', 1, 'ready', 'init'),
(205, 'HumanTask', 1, 'ready', 'init'),
(206, 'HumanTask', 1, 'terminated', 'init'),
(207, 'HumanTask', 1, 'terminated', 'init'),
(208, 'HumanTask', 1, 'ready', 'init'),
(209, 'HumanTask', 1, 'ready', 'init'),
(210, 'HumanTask', 1, 'ready', 'init'),
(211, 'HumanTask', 1, 'terminated', 'init'),
(212, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(213, 'HumanTask', 1, 'terminated', 'init'),
(214, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(215, 'HumanTask', 1, 'terminated', 'init'),
(216, 'HumanTask', 1, 'terminated', 'init'),
(217, 'HumanTask', 1, 'terminated', 'init'),
(218, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(219, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(220, 'HumanTask', 1, 'terminated', 'init'),
(221, 'HumanTask', 1, 'terminated', 'init'),
(222, 'HumanTask', 1, 'terminated', 'init'),
(223, 'HumanTask', 1, 'terminated', 'init'),
(224, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(225, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(226, 'HumanTask', 1, 'terminated', 'init'),
(227, 'HumanTask', 1, 'terminated', 'init'),
(228, 'HumanTask', 1, 'terminated', 'init'),
(229, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(230, 'HumanTask', 1, 'terminated', 'init'),
(231, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(232, 'HumanTask', 1, 'ready', 'init'),
(233, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(234, 'HumanTask', 1, 'terminated', 'init'),
(235, 'HumanTask', 1, 'terminated', 'init'),
(236, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(237, 'HumanTask', 1, 'ready', 'init'),
(238, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(239, 'HumanTask', 1, 'terminated', 'init'),
(240, 'HumanTask', 1, 'ready', 'init'),
(241, 'HumanTask', 1, 'ready', 'init'),
(242, 'HumanTask', 1, 'terminated', 'init'),
(243, 'HumanTask', 1, 'ready(ControlFlow)', 'init');

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
(6, 7, ''),
(9, 10, ''),
(10, 11, '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
`id` int(11) NOT NULL,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

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
(8, 'Endevent1Fragment2', 'Endevent', 2),
(9, 'StartEventFragment3', 'Startevent', 3),
(10, 'ActivityFragment3', 'Activity', 3),
(11, 'EndEventFragment3', 'Endevent', 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
`id` int(11) NOT NULL,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=244 ;

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
(115, 'Activity', 4, 105),
(116, 'Activity', 2, 106),
(117, 'Activity', 4, 107),
(118, 'Activity', 4, 108),
(119, 'Activity', 2, 109),
(120, 'Activity', 4, 110),
(121, 'Activity', 4, 111),
(122, 'Activity', 2, 112),
(123, 'Activity', 4, 113),
(124, 'Activity', 4, 114),
(125, 'Activity', 2, 115),
(126, 'Activity', 4, 116),
(127, 'Activity', 4, 117),
(128, 'Activity', 4, 118),
(129, 'Activity', 2, 119),
(130, 'Activity', 4, 120),
(131, 'Activity', 4, 121),
(132, 'Activity', 4, 122),
(133, 'Activity', 4, 123),
(134, 'Activity', 4, 124),
(135, 'Activity', 4, 125),
(136, 'Activity', 4, 126),
(137, 'Activity', 4, 127),
(138, 'Activity', 4, 128),
(139, 'Activity', 4, 129),
(140, 'Activity', 4, 130),
(141, 'Activity', 4, 131),
(142, 'Activity', 4, 132),
(143, 'Activity', 4, 133),
(144, 'Activity', 4, 134),
(145, 'Activity', 4, 135),
(146, 'Activity', 4, 136),
(147, 'Activity', 4, 137),
(148, 'Activity', 4, 138),
(149, 'Activity', 4, 139),
(150, 'Activity', 4, 140),
(151, 'Activity', 4, 141),
(152, 'Activity', 4, 142),
(153, 'Activity', 4, 143),
(154, 'Activity', 4, 144),
(155, 'Activity', 4, 145),
(156, 'Activity', 4, 146),
(157, 'Activity', 4, 147),
(158, 'Activity', 2, 148),
(159, 'Activity', 4, 149),
(160, 'Activity', 4, 150),
(161, 'Activity', 4, 151),
(162, 'Activity', 4, 152),
(163, 'Activity', 4, 153),
(164, 'Activity', 4, 154),
(165, 'Activity', 4, 155),
(166, 'Activity', 4, 156),
(167, 'Activity', 4, 157),
(168, 'Activity', 2, 158),
(169, 'Activity', 4, 159),
(170, 'Activity', 4, 160),
(171, 'Activity', 4, 161),
(172, 'Activity', 4, 162),
(173, 'Activity', 4, 163),
(174, 'Activity', 4, 164),
(175, 'Activity', 4, 165),
(176, 'Activity', 4, 166),
(177, 'Activity', 4, 167),
(178, 'Activity', 4, 168),
(179, 'Activity', 4, 169),
(180, 'Activity', 2, 170),
(181, 'Activity', 4, 171),
(182, 'Activity', 4, 172),
(183, 'Activity', 4, 173),
(184, 'Activity', 4, 174),
(185, 'Activity', 4, 175),
(186, 'Activity', 2, 176),
(187, 'Activity', 4, 177),
(188, 'Activity', 4, 178),
(189, 'Activity', 4, 179),
(190, 'Activity', 2, 180),
(191, 'Activity', 4, 181),
(192, 'Activity', 5, 180),
(193, 'Activity', 6, 180),
(194, 'Activity', 2, 182),
(195, 'Activity', 4, 183),
(196, 'Activity', 4, 184),
(197, 'Activity', 4, 185),
(198, 'Activity', 4, 186),
(199, 'Activity', 4, 187),
(200, 'Activity', 4, 188),
(201, 'Activity', 4, 189),
(202, 'Activity', 2, 190),
(203, 'Activity', 4, 191),
(204, 'Activity', 2, 192),
(205, 'Activity', 4, 193),
(206, 'Activity', 5, 109),
(207, 'Activity', 6, 109),
(208, 'Activity', 2, 194),
(209, 'Activity', 2, 195),
(210, 'Activity', 4, 196),
(211, 'Activity', 2, 197),
(212, 'Activity', 4, 198),
(213, 'Activity', 5, 197),
(214, 'Activity', 6, 197),
(215, 'Activity', 2, 199),
(216, 'Activity', 4, 200),
(217, 'Activity', 5, 199),
(218, 'Activity', 6, 199),
(219, 'Activity', 4, 201),
(220, 'Activity', 2, 202),
(221, 'Activity', 4, 203),
(222, 'Activity', 5, 202),
(223, 'Activity', 6, 202),
(224, 'Activity', 4, 204),
(225, 'Activity', 2, 205),
(226, 'Activity', 2, 206),
(227, 'Activity', 4, 207),
(228, 'Activity', 5, 206),
(229, 'Activity', 4, 208),
(230, 'Activity', 6, 206),
(231, 'Activity', 2, 209),
(232, 'Activity', 2, 210),
(233, 'Activity', 4, 211),
(234, 'Activity', 2, 213),
(235, 'Activity', 4, 214),
(236, 'Activity', 10, 215),
(237, 'Activity', 5, 213),
(238, 'Activity', 4, 216),
(239, 'Activity', 2, 217),
(240, 'Activity', 4, 218),
(241, 'Activity', 10, 219),
(242, 'Activity', 5, 217),
(243, 'Activity', 6, 217);

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

--
-- Daten für Tabelle `dataflow`
--

INSERT INTO `dataflow` (`controlnode_id`, `dataset_id`, `input`) VALUES
(2, 1, 1),
(2, 5, 0),
(4, 2, 1),
(4, 6, 0),
(5, 8, 0),
(6, 3, 1),
(6, 7, 0),
(7, 4, 1),
(10, 9, 1);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

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
(8, 1, 6, 2, 2),
(9, 1, 2, 1, 1),
(10, 1, 6, 2, 2);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=51 ;

--
-- Daten für Tabelle `dataobjectinstance`
--

INSERT INTO `dataobjectinstance` (`id`, `scenarioinstance_id`, `state_id`, `dataobject_id`) VALUES
(7, 62, 1, 1),
(8, 62, 5, 2),
(9, 63, 1, 1),
(10, 63, 5, 2),
(11, 64, 1, 1),
(12, 64, 5, 2),
(13, 65, 1, 1),
(14, 65, 5, 2),
(15, 66, 1, 1),
(16, 66, 5, 2),
(17, 67, 1, 1),
(18, 67, 5, 2),
(19, 68, 1, 1),
(20, 68, 5, 2),
(21, 69, 1, 1),
(22, 69, 5, 2),
(23, 70, 1, 1),
(24, 70, 5, 2),
(25, 71, 1, 1),
(26, 71, 5, 2),
(27, 72, 1, 1),
(28, 72, 5, 2),
(29, 73, 1, 1),
(30, 73, 5, 2),
(31, 74, 1, 1),
(32, 74, 5, 2),
(33, 75, 1, 1),
(34, 75, 5, 2),
(35, 76, 1, 1),
(36, 76, 5, 2),
(37, 77, 1, 1),
(38, 77, 5, 2),
(39, 78, 3, 1),
(40, 78, 6, 2),
(41, 79, 4, 1),
(42, 79, 6, 2),
(43, 80, 4, 1),
(44, 80, 6, 2),
(45, 81, 1, 1),
(46, 81, 5, 2),
(47, 82, 3, 1),
(48, 82, 5, 2),
(49, 83, 2, 1),
(50, 83, 6, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
`id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Daten für Tabelle `dataset`
--

INSERT INTO `dataset` (`id`, `input`) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 0),
(6, 0),
(7, 0),
(8, 0),
(9, 1);

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
(1, 1),
(2, 3),
(3, 5),
(4, 7),
(5, 2),
(6, 4),
(7, 6),
(8, 8),
(9, 9),
(9, 10);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Daten für Tabelle `fragment`
--

INSERT INTO `fragment` (`id`, `name`, `scenario_id`) VALUES
(1, 'fragment1', 1),
(2, 'fragment2', 1),
(3, 'fragment3', 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
`id` int(11) NOT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=220 ;

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
(105, 0, 2, 63),
(106, 0, 1, 64),
(107, 1, 2, 64),
(108, 0, 2, 64),
(109, 1, 1, 65),
(110, 1, 2, 65),
(111, 0, 2, 65),
(112, 0, 1, 66),
(113, 1, 2, 66),
(114, 0, 2, 66),
(115, 0, 1, 67),
(116, 1, 2, 67),
(117, 1, 2, 67),
(118, 0, 2, 67),
(119, 0, 1, 68),
(120, 1, 2, 68),
(121, 1, 2, 68),
(122, 1, 2, 68),
(123, 1, 2, 68),
(124, 1, 2, 68),
(125, 1, 2, 68),
(126, 1, 2, 68),
(127, 1, 2, 68),
(128, 1, 2, 68),
(129, 1, 2, 68),
(130, 1, 2, 68),
(131, 1, 2, 68),
(132, 1, 2, 68),
(133, 1, 2, 68),
(134, 1, 2, 68),
(135, 1, 2, 68),
(136, 1, 2, 68),
(137, 1, 2, 68),
(138, 1, 2, 68),
(139, 1, 2, 68),
(140, 1, 2, 68),
(141, 1, 2, 68),
(142, 1, 2, 68),
(143, 1, 2, 68),
(144, 1, 2, 68),
(145, 1, 2, 68),
(146, 1, 2, 68),
(147, 0, 2, 68),
(148, 0, 1, 69),
(149, 1, 2, 69),
(150, 1, 2, 69),
(151, 1, 2, 69),
(152, 1, 2, 69),
(153, 1, 2, 69),
(154, 1, 2, 69),
(155, 1, 2, 69),
(156, 1, 2, 69),
(157, 0, 2, 69),
(158, 0, 1, 70),
(159, 1, 2, 70),
(160, 1, 2, 70),
(161, 1, 2, 70),
(162, 1, 2, 70),
(163, 1, 2, 70),
(164, 1, 2, 70),
(165, 1, 2, 70),
(166, 1, 2, 70),
(167, 1, 2, 70),
(168, 1, 2, 70),
(169, 0, 2, 70),
(170, 0, 1, 71),
(171, 1, 2, 71),
(172, 1, 2, 71),
(173, 1, 2, 71),
(174, 1, 2, 71),
(175, 0, 2, 71),
(176, 0, 1, 72),
(177, 1, 2, 72),
(178, 1, 2, 72),
(179, 0, 2, 72),
(180, 1, 1, 73),
(181, 1, 2, 73),
(182, 0, 1, 73),
(183, 1, 2, 73),
(184, 1, 2, 73),
(185, 1, 2, 73),
(186, 1, 2, 73),
(187, 1, 2, 73),
(188, 1, 2, 73),
(189, 0, 2, 73),
(190, 0, 1, 74),
(191, 0, 2, 74),
(192, 0, 1, 75),
(193, 0, 2, 75),
(194, 0, 1, 65),
(195, 0, 1, 76),
(196, 0, 2, 76),
(197, 0, 1, 77),
(198, 0, 2, 77),
(199, 0, 1, 78),
(200, 1, 2, 78),
(201, 0, 2, 78),
(202, 1, 1, 79),
(203, 1, 2, 79),
(204, 0, 2, 79),
(205, 0, 1, 79),
(206, 1, 1, 80),
(207, 1, 2, 80),
(208, 0, 2, 80),
(209, 0, 1, 80),
(210, 0, 1, 81),
(211, 0, 2, 81),
(212, 0, 3, 81),
(213, 0, 1, 82),
(214, 1, 2, 82),
(215, 0, 3, 82),
(216, 0, 2, 82),
(217, 0, 1, 83),
(218, 0, 2, 83),
(219, 0, 3, 83);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=84 ;

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
(63, 1),
(64, 1),
(65, 1),
(66, 1),
(67, 1),
(68, 1),
(69, 1),
(70, 1),
(71, 1),
(72, 1),
(73, 1),
(74, 1),
(75, 1),
(76, 1),
(77, 1),
(78, 1),
(79, 1),
(80, 1),
(81, 1),
(82, 1),
(83, 1);

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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=244;
--
-- AUTO_INCREMENT for table `configuration`
--
ALTER TABLE `configuration`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `controlnode`
--
ALTER TABLE `controlnode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT for table `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=244;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `dataobject`
--
ALTER TABLE `dataobject`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=51;
--
-- AUTO_INCREMENT for table `dataset`
--
ALTER TABLE `dataset`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=11;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=220;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=84;
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
