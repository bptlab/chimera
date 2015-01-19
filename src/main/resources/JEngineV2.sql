-- phpMyAdmin SQL Dump
-- version 4.2.7.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 19. Jan 2015 um 16:46
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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=659 ;

--
-- Daten für Tabelle `activityinstance`
--

INSERT INTO `activityinstance` (`id`, `type`, `role_id`, `activity_state`, `workitem_state`) VALUES
(0, 'HumanTask', 1, 'ready', 'init'),
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
(89, 'HumanTask', 1, 'terminated', 'init'),
(90, 'HumanTask', 1, 'terminated', 'init'),
(91, 'HumanTask', 1, 'terminated', 'init'),
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
(243, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(244, 'HumanTask', 1, 'terminated', 'init'),
(245, 'HumanTask', 1, 'ready', 'init'),
(246, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(247, 'HumanTask', 1, 'ready', 'init'),
(248, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(249, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(250, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(251, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(252, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(253, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(254, 'HumanTask', 1, 'terminated', 'init'),
(255, 'HumanTask', 1, 'ready', 'init'),
(256, 'HumanTask', 1, 'ready', 'init'),
(257, 'HumanTask', 1, 'terminated', 'init'),
(258, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(259, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(260, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(261, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(262, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(263, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(264, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(265, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(266, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(267, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(268, 'HumanTask', 1, 'ready', 'init'),
(269, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(270, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(271, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(272, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(273, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(274, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(275, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(276, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(277, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(278, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(279, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(280, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(281, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(282, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(283, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(284, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(285, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(286, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(287, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(288, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(289, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(290, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(291, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(292, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(293, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(294, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(295, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(296, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(297, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(298, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(299, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(300, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(301, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(302, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(303, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(304, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(305, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(306, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(307, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(308, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(309, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(310, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(311, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(312, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(313, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(314, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(315, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(316, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(317, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(318, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(319, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(320, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(321, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(322, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(323, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(324, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(325, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(326, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(327, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(328, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(329, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(330, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(331, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(332, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(333, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(334, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(335, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(336, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(337, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(338, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(339, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(340, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(341, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(342, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(343, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(344, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(345, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(346, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(347, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(348, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(349, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(350, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(351, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(352, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(353, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(354, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(355, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(356, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(357, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(358, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(359, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(360, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(361, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(362, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(363, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(364, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(365, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(366, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(367, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(368, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(369, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(370, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(371, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(372, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(373, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(374, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(375, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(376, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(377, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(378, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(379, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(380, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(381, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(382, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(383, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(384, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(385, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(386, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(387, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(388, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(389, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(390, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(391, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(392, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(393, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(394, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(395, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(396, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(397, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(398, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(399, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(400, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(401, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(402, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(403, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(404, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(405, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(406, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(407, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(408, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(409, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(410, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(411, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(412, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(413, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(414, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(415, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(416, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(417, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(418, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(419, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(420, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(421, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(422, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(423, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(424, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(425, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(426, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(427, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(428, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(429, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(430, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(431, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(432, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(433, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(434, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(435, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(436, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(437, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(438, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(439, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(440, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(441, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(442, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(443, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(444, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(445, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(446, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(447, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(448, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(449, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(450, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(451, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(452, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(453, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(454, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(455, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(456, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(457, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(458, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(459, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(460, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(461, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(462, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(463, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(464, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(465, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(466, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(467, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(468, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(469, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(470, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(471, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(472, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(473, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(474, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(475, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(476, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(477, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(478, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(479, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(480, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(481, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(482, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(483, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(484, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(485, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(486, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(487, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(488, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(489, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(490, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(491, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(492, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(493, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(494, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(495, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(496, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(497, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(498, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(499, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(500, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(501, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(502, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(503, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(504, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(505, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(506, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(507, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(508, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(509, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(510, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(511, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(512, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(513, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(514, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(515, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(516, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(517, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(518, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(519, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(520, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(521, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(522, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(523, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(524, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(525, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(526, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(527, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(528, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(529, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(530, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(531, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(532, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(533, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(534, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(535, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(536, 'HumanTask', 1, 'init', 'init'),
(537, 'HumanTask', 1, 'ready', 'init'),
(538, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(539, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(540, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(541, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(542, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(543, 'HumanTask', 1, 'ready', 'init'),
(544, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(545, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(546, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(547, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(548, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(549, 'HumanTask', 1, 'ready', 'init'),
(550, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(551, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(552, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(553, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(554, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(555, 'HumanTask', 1, 'ready', 'init'),
(556, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(557, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(558, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(559, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(560, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(561, 'HumanTask', 1, 'ready', 'init'),
(562, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(563, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(564, 'HumanTask', 1, 'ready', 'init'),
(565, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(566, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(567, 'HumanTask', 1, 'ready', 'init'),
(568, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(569, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(570, 'HumanTask', 1, 'ready', 'init'),
(571, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(572, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(573, 'HumanTask', 1, 'ready', 'init'),
(574, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(575, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(576, 'HumanTask', 1, 'terminated', 'init'),
(577, 'HumanTask', 1, 'ready', 'init'),
(578, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(579, 'HumanTask', 1, 'ready', 'init'),
(580, 'HumanTask', 1, 'ready', 'init'),
(581, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(582, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(583, 'HumanTask', 1, 'ready', 'init'),
(584, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(585, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(586, 'HumanTask', 1, 'ready', 'init'),
(587, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(588, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(589, 'HumanTask', 1, 'ready', 'init'),
(590, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(591, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(592, 'HumanTask', 1, 'ready', 'init'),
(593, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(594, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(595, 'HumanTask', 1, 'ready', 'init'),
(596, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(597, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(598, 'HumanTask', 1, 'ready', 'init'),
(599, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(600, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(601, 'HumanTask', 1, 'ready', 'init'),
(602, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(603, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(604, 'HumanTask', 1, 'ready', 'init'),
(605, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(606, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(607, 'HumanTask', 1, 'ready', 'init'),
(608, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(609, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(610, 'HumanTask', 1, 'ready', 'init'),
(611, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(612, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(613, 'HumanTask', 1, 'ready', 'init'),
(614, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(615, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(616, 'HumanTask', 1, 'ready', 'init'),
(617, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(618, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(619, 'HumanTask', 1, 'ready', 'init'),
(620, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(621, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(622, 'HumanTask', 1, 'terminated', 'init'),
(623, 'HumanTask', 1, 'ready', 'init'),
(624, 'HumanTask', 1, 'ready', 'init'),
(625, 'HumanTask', 1, 'terminated', 'init'),
(626, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(627, 'HumanTask', 1, 'ready', 'init'),
(628, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(629, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(630, 'HumanTask', 1, 'ready', 'init'),
(631, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(632, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(633, 'HumanTask', 1, 'ready', 'init'),
(634, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(635, 'HumanTask', 1, 'ready(ControlFlow)', 'init'),
(637, 'HumanTask', 1, 'ready', 'init'),
(638, 'HumanTask', 1, 'ready', 'init'),
(640, 'HumanTask', 1, 'ready', 'init'),
(641, 'HumanTask', 1, 'ready', 'init'),
(643, 'HumanTask', 1, 'ready', 'init'),
(644, 'HumanTask', 1, 'ready', 'init'),
(646, 'HumanTask', 1, 'ready', 'init'),
(647, 'HumanTask', 1, 'ready', 'init'),
(649, 'HumanTask', 1, 'terminated', 'init'),
(650, 'HumanTask', 1, 'ready', 'init'),
(653, 'HumanTask', 1, 'terminated', 'init'),
(654, 'HumanTask', 1, 'terminated', 'init'),
(657, 'HumanTask', 1, 'terminated', 'init'),
(658, 'HumanTask', 1, 'terminated', 'init');

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
(10, 11, ''),
(12, 13, ''),
(12, 14, ''),
(101, 102, ''),
(102, 103, ''),
(102, 104, ''),
(103, 105, ''),
(104, 105, ''),
(105, 106, '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnode`
--

CREATE TABLE IF NOT EXISTS `controlnode` (
`id` int(11) NOT NULL,
  `label` varchar(512) NOT NULL,
  `type` varchar(512) NOT NULL,
  `fragment_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=107 ;

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
(11, 'EndEventFragment3', 'Endevent', 3),
(12, 'test1', 'Activity', 100),
(13, 'test2', 'Activity', 100),
(14, 'test3', 'Activity', 100),
(100, 'TestAND', 'AND', 100),
(101, 'Startevent', 'Startevent', 101),
(102, 'AND', 'AND', 101),
(103, 'Activity1', 'Activity', 101),
(104, 'Activity2', 'Activity', 101),
(105, 'AND Join', 'AND', 101),
(106, 'Endevent', 'Endevent', 101);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `controlnodeinstance`
--

CREATE TABLE IF NOT EXISTS `controlnodeinstance` (
`id` int(11) NOT NULL,
  `Type` varchar(512) NOT NULL,
  `controlnode_id` int(11) NOT NULL,
  `fragmentinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=660 ;

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
(243, 'Activity', 6, 217),
(244, 'Activity', 2, 220),
(245, 'Activity', 4, 221),
(246, 'Activity', 10, 222),
(247, 'Activity', 5, 220),
(248, 'Activity', 2, 223),
(249, 'Activity', 4, 224),
(250, 'Activity', 10, 225),
(251, 'Activity', 2, 226),
(252, 'Activity', 4, 227),
(253, 'Activity', 10, 228),
(254, 'Activity', 2, 229),
(255, 'Activity', 4, 230),
(256, 'Activity', 10, 231),
(257, 'Activity', 5, 229),
(258, 'Activity', 6, 229),
(259, 'Activity', 2, 232),
(260, 'Activity', 4, 233),
(261, 'Activity', 10, 234),
(262, 'Activity', 2, 235),
(263, 'Activity', 4, 236),
(264, 'Activity', 10, 237),
(265, 'Activity', 2, 238),
(266, 'Activity', 4, 239),
(267, 'Activity', 10, 240),
(268, 'Activity', 2, 241),
(269, 'Activity', 4, 242),
(270, 'Activity', 10, 243),
(271, 'Activity', 2, 244),
(272, 'Activity', 4, 245),
(273, 'Activity', 10, 246),
(274, 'Activity', 2, 247),
(275, 'Activity', 4, 248),
(276, 'Activity', 10, 249),
(277, 'Activity', 2, 250),
(278, 'Activity', 4, 251),
(279, 'Activity', 10, 252),
(280, 'Activity', 2, 253),
(281, 'Activity', 4, 254),
(282, 'Activity', 10, 255),
(283, 'Activity', 2, 256),
(284, 'Activity', 4, 257),
(285, 'Activity', 10, 258),
(286, 'Activity', 2, 259),
(287, 'Activity', 4, 260),
(288, 'Activity', 10, 261),
(289, 'Activity', 2, 262),
(290, 'Activity', 4, 263),
(291, 'Activity', 10, 264),
(292, 'Activity', 2, 265),
(293, 'Activity', 4, 266),
(294, 'Activity', 10, 267),
(295, 'Activity', 2, 268),
(296, 'Activity', 4, 269),
(297, 'Activity', 10, 270),
(298, 'Activity', 2, 271),
(299, 'Activity', 4, 272),
(300, 'Activity', 10, 273),
(301, 'Activity', 2, 274),
(302, 'Activity', 4, 275),
(303, 'Activity', 10, 276),
(304, 'Activity', 2, 277),
(305, 'Activity', 4, 278),
(306, 'Activity', 10, 279),
(307, 'Activity', 2, 280),
(308, 'Activity', 4, 281),
(309, 'Activity', 10, 282),
(310, 'Activity', 2, 283),
(311, 'Activity', 4, 284),
(312, 'Activity', 10, 285),
(313, 'Activity', 2, 286),
(314, 'Activity', 4, 287),
(315, 'Activity', 10, 288),
(316, 'Activity', 2, 289),
(317, 'Activity', 4, 290),
(318, 'Activity', 10, 291),
(319, 'Activity', 2, 292),
(320, 'Activity', 4, 293),
(321, 'Activity', 10, 294),
(322, 'Activity', 2, 295),
(323, 'Activity', 4, 296),
(324, 'Activity', 10, 297),
(325, 'Activity', 2, 298),
(326, 'Activity', 4, 299),
(327, 'Activity', 10, 300),
(328, 'Activity', 2, 301),
(329, 'Activity', 4, 302),
(330, 'Activity', 10, 303),
(331, 'Activity', 2, 304),
(332, 'Activity', 4, 305),
(333, 'Activity', 10, 306),
(334, 'Activity', 2, 307),
(335, 'Activity', 4, 308),
(336, 'Activity', 10, 309),
(337, 'Activity', 2, 310),
(338, 'Activity', 4, 311),
(339, 'Activity', 10, 312),
(340, 'Activity', 2, 313),
(341, 'Activity', 4, 314),
(342, 'Activity', 10, 315),
(343, 'Activity', 2, 316),
(344, 'Activity', 4, 317),
(345, 'Activity', 10, 318),
(346, 'Activity', 2, 319),
(347, 'Activity', 4, 320),
(348, 'Activity', 10, 321),
(349, 'Activity', 2, 322),
(350, 'Activity', 4, 323),
(351, 'Activity', 10, 324),
(352, 'Activity', 2, 325),
(353, 'Activity', 4, 326),
(354, 'Activity', 10, 327),
(355, 'Activity', 2, 328),
(356, 'Activity', 4, 329),
(357, 'Activity', 10, 330),
(358, 'Activity', 2, 331),
(359, 'Activity', 4, 332),
(360, 'Activity', 10, 333),
(361, 'Activity', 2, 334),
(362, 'Activity', 4, 335),
(363, 'Activity', 10, 336),
(364, 'Activity', 2, 337),
(365, 'Activity', 4, 338),
(366, 'Activity', 10, 339),
(367, 'Activity', 2, 340),
(368, 'Activity', 4, 341),
(369, 'Activity', 10, 342),
(370, 'Activity', 2, 343),
(371, 'Activity', 4, 344),
(372, 'Activity', 10, 345),
(373, 'Activity', 2, 346),
(374, 'Activity', 4, 347),
(375, 'Activity', 10, 348),
(376, 'Activity', 2, 349),
(377, 'Activity', 4, 350),
(378, 'Activity', 10, 351),
(379, 'Activity', 2, 352),
(380, 'Activity', 4, 353),
(381, 'Activity', 10, 354),
(382, 'Activity', 2, 355),
(383, 'Activity', 4, 356),
(384, 'Activity', 10, 357),
(385, 'Activity', 2, 358),
(386, 'Activity', 4, 359),
(387, 'Activity', 10, 360),
(388, 'Activity', 2, 361),
(389, 'Activity', 4, 362),
(390, 'Activity', 10, 363),
(391, 'Activity', 2, 364),
(392, 'Activity', 4, 365),
(393, 'Activity', 10, 366),
(394, 'Activity', 2, 367),
(395, 'Activity', 4, 368),
(396, 'Activity', 10, 369),
(397, 'Activity', 2, 370),
(398, 'Activity', 4, 371),
(399, 'Activity', 10, 372),
(400, 'Activity', 2, 373),
(401, 'Activity', 4, 374),
(402, 'Activity', 10, 375),
(403, 'Activity', 2, 376),
(404, 'Activity', 4, 377),
(405, 'Activity', 10, 378),
(406, 'Activity', 2, 379),
(407, 'Activity', 4, 380),
(408, 'Activity', 10, 381),
(409, 'Activity', 2, 382),
(410, 'Activity', 4, 383),
(411, 'Activity', 10, 384),
(412, 'Activity', 2, 385),
(413, 'Activity', 4, 386),
(414, 'Activity', 10, 387),
(415, 'Activity', 2, 388),
(416, 'Activity', 4, 389),
(417, 'Activity', 10, 390),
(418, 'Activity', 2, 391),
(419, 'Activity', 4, 392),
(420, 'Activity', 10, 393),
(421, 'Activity', 2, 394),
(422, 'Activity', 4, 395),
(423, 'Activity', 10, 396),
(424, 'Activity', 2, 397),
(425, 'Activity', 4, 398),
(426, 'Activity', 10, 399),
(427, 'Activity', 2, 400),
(428, 'Activity', 4, 401),
(429, 'Activity', 10, 402),
(430, 'Activity', 2, 403),
(431, 'Activity', 4, 404),
(432, 'Activity', 10, 405),
(433, 'Activity', 2, 406),
(434, 'Activity', 4, 407),
(435, 'Activity', 10, 408),
(436, 'Activity', 2, 409),
(437, 'Activity', 4, 410),
(438, 'Activity', 10, 411),
(439, 'Activity', 2, 412),
(440, 'Activity', 4, 413),
(441, 'Activity', 10, 414),
(442, 'Activity', 2, 415),
(443, 'Activity', 4, 416),
(444, 'Activity', 10, 417),
(445, 'Activity', 2, 418),
(446, 'Activity', 4, 419),
(447, 'Activity', 10, 420),
(448, 'Activity', 2, 421),
(449, 'Activity', 4, 422),
(450, 'Activity', 10, 423),
(451, 'Activity', 2, 424),
(452, 'Activity', 4, 425),
(453, 'Activity', 10, 426),
(454, 'Activity', 2, 427),
(455, 'Activity', 4, 428),
(456, 'Activity', 10, 429),
(457, 'Activity', 2, 430),
(458, 'Activity', 4, 431),
(459, 'Activity', 10, 432),
(460, 'Activity', 2, 433),
(461, 'Activity', 4, 434),
(462, 'Activity', 10, 435),
(463, 'Activity', 2, 436),
(464, 'Activity', 4, 437),
(465, 'Activity', 10, 438),
(466, 'Activity', 2, 439),
(467, 'Activity', 4, 440),
(468, 'Activity', 10, 441),
(469, 'Activity', 2, 442),
(470, 'Activity', 4, 443),
(471, 'Activity', 10, 444),
(472, 'Activity', 2, 445),
(473, 'Activity', 4, 446),
(474, 'Activity', 10, 447),
(475, 'Activity', 2, 448),
(476, 'Activity', 4, 449),
(477, 'Activity', 10, 450),
(478, 'Activity', 2, 451),
(479, 'Activity', 4, 452),
(480, 'Activity', 10, 453),
(481, 'Activity', 2, 454),
(482, 'Activity', 4, 455),
(483, 'Activity', 10, 456),
(484, 'Activity', 2, 457),
(485, 'Activity', 4, 458),
(486, 'Activity', 10, 459),
(487, 'Activity', 2, 460),
(488, 'Activity', 4, 461),
(489, 'Activity', 10, 462),
(490, 'Activity', 2, 463),
(491, 'Activity', 4, 464),
(492, 'Activity', 10, 465),
(493, 'Activity', 2, 466),
(494, 'Activity', 4, 467),
(495, 'Activity', 10, 468),
(496, 'Activity', 2, 469),
(497, 'Activity', 4, 470),
(498, 'Activity', 10, 471),
(499, 'Activity', 2, 472),
(500, 'Activity', 4, 473),
(501, 'Activity', 10, 474),
(502, 'Activity', 2, 475),
(503, 'Activity', 4, 476),
(504, 'Activity', 10, 477),
(505, 'Activity', 2, 478),
(506, 'Activity', 4, 479),
(507, 'Activity', 10, 480),
(508, 'Activity', 2, 481),
(509, 'Activity', 4, 482),
(510, 'Activity', 10, 483),
(511, 'Activity', 2, 484),
(512, 'Activity', 4, 485),
(513, 'Activity', 10, 486),
(514, 'Activity', 2, 487),
(515, 'Activity', 4, 488),
(516, 'Activity', 10, 489),
(517, 'Activity', 2, 490),
(518, 'Activity', 4, 491),
(519, 'Activity', 10, 492),
(520, 'Activity', 2, 493),
(521, 'Activity', 4, 494),
(522, 'Activity', 10, 495),
(523, 'Activity', 2, 496),
(524, 'Activity', 4, 497),
(525, 'Activity', 10, 498),
(526, 'Activity', 2, 499),
(527, 'Activity', 4, 500),
(528, 'Activity', 10, 501),
(529, 'Activity', 2, 502),
(530, 'Activity', 4, 503),
(531, 'Activity', 10, 504),
(532, 'Activity', 2, 505),
(533, 'Activity', 4, 506),
(534, 'Activity', 10, 507),
(535, 'Activity', 2, 508),
(536, 'Activity', 4, 509),
(537, 'Activity', 2, 510),
(538, 'Activity', 4, 511),
(539, 'Activity', 10, 512),
(540, 'Activity', 2, 513),
(541, 'Activity', 4, 514),
(542, 'Activity', 10, 515),
(543, 'Activity', 2, 516),
(544, 'Activity', 4, 517),
(545, 'Activity', 10, 518),
(546, 'Activity', 2, 519),
(547, 'Activity', 4, 520),
(548, 'Activity', 10, 521),
(549, 'Activity', 2, 522),
(550, 'Activity', 4, 523),
(551, 'Activity', 10, 524),
(552, 'Activity', 2, 525),
(553, 'Activity', 4, 526),
(554, 'Activity', 10, 527),
(555, 'Activity', 2, 528),
(556, 'Activity', 4, 529),
(557, 'Activity', 10, 530),
(558, 'Activity', 2, 531),
(559, 'Activity', 4, 532),
(560, 'Activity', 10, 533),
(561, 'Activity', 2, 534),
(562, 'Activity', 4, 535),
(563, 'Activity', 10, 536),
(564, 'Activity', 2, 537),
(565, 'Activity', 4, 538),
(566, 'Activity', 10, 539),
(567, 'Activity', 2, 540),
(568, 'Activity', 4, 541),
(569, 'Activity', 10, 542),
(570, 'Activity', 2, 543),
(571, 'Activity', 4, 544),
(572, 'Activity', 10, 545),
(573, 'Activity', 2, 546),
(574, 'Activity', 4, 547),
(575, 'Activity', 10, 548),
(576, 'Activity', 2, 549),
(577, 'Activity', 4, 550),
(578, 'Activity', 10, 551),
(579, 'Activity', 5, 549),
(580, 'Activity', 2, 552),
(581, 'Activity', 4, 553),
(582, 'Activity', 10, 554),
(583, 'Activity', 2, 555),
(584, 'Activity', 4, 556),
(585, 'Activity', 10, 557),
(586, 'Activity', 2, 558),
(587, 'Activity', 4, 559),
(588, 'Activity', 10, 560),
(589, 'Activity', 2, 561),
(590, 'Activity', 4, 562),
(591, 'Activity', 10, 563),
(592, 'Activity', 2, 564),
(593, 'Activity', 4, 565),
(594, 'Activity', 10, 566),
(595, 'Activity', 2, 567),
(596, 'Activity', 4, 568),
(597, 'Activity', 10, 569),
(598, 'Activity', 2, 570),
(599, 'Activity', 4, 571),
(600, 'Activity', 10, 572),
(601, 'Activity', 2, 573),
(602, 'Activity', 4, 574),
(603, 'Activity', 10, 575),
(604, 'Activity', 2, 576),
(605, 'Activity', 4, 577),
(606, 'Activity', 10, 578),
(607, 'Activity', 2, 579),
(608, 'Activity', 4, 580),
(609, 'Activity', 10, 581),
(610, 'Activity', 2, 582),
(611, 'Activity', 4, 583),
(612, 'Activity', 10, 584),
(613, 'Activity', 2, 585),
(614, 'Activity', 4, 586),
(615, 'Activity', 10, 587),
(616, 'Activity', 2, 588),
(617, 'Activity', 4, 589),
(618, 'Activity', 10, 590),
(619, 'Activity', 2, 591),
(620, 'Activity', 4, 592),
(621, 'Activity', 10, 593),
(622, 'Activity', 2, 594),
(623, 'Activity', 4, 595),
(624, 'Activity', 10, 596),
(625, 'Activity', 5, 594),
(626, 'Activity', 6, 594),
(627, 'Activity', 2, 597),
(628, 'Activity', 4, 598),
(629, 'Activity', 10, 599),
(630, 'Activity', 2, 600),
(631, 'Activity', 4, 601),
(632, 'Activity', 10, 602),
(633, 'Activity', 2, 603),
(634, 'Activity', 4, 604),
(635, 'Activity', 10, 605),
(636, 'AND', 102, 613),
(637, 'Activity', 103, 613),
(638, 'Activity', 104, 613),
(639, 'AND', 102, 614),
(640, 'Activity', 103, 614),
(641, 'Activity', 104, 614),
(642, 'AND', 102, 615),
(643, 'Activity', 103, 615),
(644, 'Activity', 104, 615),
(645, 'AND', 102, 616),
(646, 'Activity', 103, 616),
(647, 'Activity', 104, 616),
(648, 'AND', 102, 617),
(649, 'Activity', 103, 617),
(650, 'Activity', 104, 617),
(651, 'AND', 105, 617),
(652, 'AND', 102, 618),
(653, 'Activity', 103, 618),
(654, 'Activity', 104, 618),
(655, 'AND', 105, 618),
(656, 'AND', 102, 619),
(657, 'Activity', 103, 619),
(658, 'Activity', 104, 619),
(659, 'AND', 105, 619);

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
(10, 9, 1),
(12, 10, 1),
(12, 11, 1),
(13, 12, 0),
(13, 13, 0);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

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
(10, 1, 6, 2, 2),
(11, 101, 2, 1, 3),
(12, 101, 3, 2, 4),
(13, 101, 2, 1, 3);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `dataobject`
--

INSERT INTO `dataobject` (`id`, `name`, `dataclass_id`, `scenario_id`, `start_state_id`) VALUES
(1, 'object1', 1, 1, 1),
(2, 'object2', 2, 1, 5),
(3, 'test1', 1, 101, 1),
(4, 'test2', 2, 101, 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataobjectinstance`
--

CREATE TABLE IF NOT EXISTS `dataobjectinstance` (
`id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL,
  `state_id` int(11) NOT NULL,
  `dataobject_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=113 ;

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
(23, 70, 2, 1),
(24, 0, 5, 2),
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
(50, 83, 6, 2),
(51, 84, 2, 1),
(52, 84, 5, 2),
(53, 95, 2, 1),
(54, 95, 6, 2),
(55, 103, 1, 1),
(56, 103, 5, 2),
(57, 193, 1, 1),
(58, 193, 5, 2),
(59, 195, 1, 1),
(60, 195, 5, 2),
(61, 197, 1, 1),
(62, 197, 5, 2),
(63, 199, 1, 1),
(64, 199, 5, 2),
(65, 201, 1, 1),
(66, 201, 5, 2),
(67, 202, 1, 1),
(68, 202, 5, 2),
(69, 203, 1, 1),
(70, 203, 5, 2),
(71, 204, 1, 1),
(72, 204, 5, 2),
(73, 205, 1, 1),
(74, 205, 5, 2),
(75, 206, 2, 1),
(76, 206, 5, 2),
(77, 207, 1, 1),
(78, 207, 5, 2),
(79, 208, 1, 1),
(80, 208, 5, 2),
(81, 209, 1, 1),
(82, 209, 5, 2),
(83, 210, 1, 1),
(84, 210, 5, 2),
(85, 212, 1, 1),
(86, 212, 5, 2),
(87, 214, 1, 1),
(88, 214, 5, 2),
(89, 215, 1, 1),
(90, 215, 5, 2),
(91, 216, 1, 1),
(92, 216, 5, 2),
(93, 217, 1, 1),
(94, 217, 5, 2),
(95, 218, 1, 1),
(96, 218, 5, 2),
(97, 219, 1, 1),
(98, 219, 5, 2),
(99, 220, 1, 1),
(100, 220, 5, 2),
(101, 221, 1, 1),
(102, 221, 5, 2),
(103, 222, 1, 1),
(104, 222, 5, 2),
(105, 223, 2, 1),
(106, 223, 6, 2),
(107, 224, 1, 1),
(108, 224, 5, 2),
(109, 226, 1, 1),
(110, 226, 5, 2),
(111, 228, 1, 1),
(112, 228, 5, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `dataset`
--

CREATE TABLE IF NOT EXISTS `dataset` (
`id` int(11) NOT NULL,
  `input` tinyint(1) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

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
(9, 1),
(10, 1),
(11, 1),
(12, 0),
(13, 0);

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
(9, 10),
(11, 11),
(11, 12),
(13, 13);

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=102 ;

--
-- Daten für Tabelle `fragment`
--

INSERT INTO `fragment` (`id`, `name`, `scenario_id`) VALUES
(1, 'fragment1', 1),
(2, 'fragment2', 1),
(3, 'fragment3', 1),
(100, 'testfragment', 101),
(101, 'fragmentScenario2', 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `fragmentinstance`
--

CREATE TABLE IF NOT EXISTS `fragmentinstance` (
`id` int(11) NOT NULL,
  `terminated` tinyint(1) NOT NULL DEFAULT '0',
  `fragment_id` int(11) NOT NULL,
  `scenarioinstance_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=620 ;

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
(219, 0, 3, 83),
(220, 0, 1, 84),
(221, 0, 2, 84),
(222, 0, 3, 84),
(223, 0, 1, 92),
(224, 0, 2, 92),
(225, 0, 3, 92),
(226, 0, 1, 94),
(227, 0, 2, 94),
(228, 0, 3, 94),
(229, 0, 1, 95),
(230, 0, 2, 95),
(231, 0, 3, 95),
(232, 0, 1, 97),
(233, 0, 2, 97),
(234, 0, 3, 97),
(235, 0, 1, 99),
(236, 0, 2, 99),
(237, 0, 3, 99),
(238, 0, 1, 101),
(239, 0, 2, 101),
(240, 0, 3, 101),
(241, 0, 1, 103),
(242, 0, 2, 103),
(243, 0, 3, 103),
(244, 0, 1, 104),
(245, 0, 2, 104),
(246, 0, 3, 104),
(247, 0, 1, 105),
(248, 0, 2, 105),
(249, 0, 3, 105),
(250, 0, 1, 106),
(251, 0, 2, 106),
(252, 0, 3, 106),
(253, 0, 1, 107),
(254, 0, 2, 107),
(255, 0, 3, 107),
(256, 0, 1, 108),
(257, 0, 2, 108),
(258, 0, 3, 108),
(259, 0, 1, 109),
(260, 0, 2, 109),
(261, 0, 3, 109),
(262, 0, 1, 110),
(263, 0, 2, 110),
(264, 0, 3, 110),
(265, 0, 1, 111),
(266, 0, 2, 111),
(267, 0, 3, 111),
(268, 0, 1, 112),
(269, 0, 2, 112),
(270, 0, 3, 112),
(271, 0, 1, 113),
(272, 0, 2, 113),
(273, 0, 3, 113),
(274, 0, 1, 114),
(275, 0, 2, 114),
(276, 0, 3, 114),
(277, 0, 1, 115),
(278, 0, 2, 115),
(279, 0, 3, 115),
(280, 0, 1, 116),
(281, 0, 2, 116),
(282, 0, 3, 116),
(283, 0, 1, 117),
(284, 0, 2, 117),
(285, 0, 3, 117),
(286, 0, 1, 118),
(287, 0, 2, 118),
(288, 0, 3, 118),
(289, 0, 1, 119),
(290, 0, 2, 119),
(291, 0, 3, 119),
(292, 0, 1, 120),
(293, 0, 2, 120),
(294, 0, 3, 120),
(295, 0, 1, 121),
(296, 0, 2, 121),
(297, 0, 3, 121),
(298, 0, 1, 122),
(299, 0, 2, 122),
(300, 0, 3, 122),
(301, 0, 1, 123),
(302, 0, 2, 123),
(303, 0, 3, 123),
(304, 0, 1, 124),
(305, 0, 2, 124),
(306, 0, 3, 124),
(307, 0, 1, 125),
(308, 0, 2, 125),
(309, 0, 3, 125),
(310, 0, 1, 126),
(311, 0, 2, 126),
(312, 0, 3, 126),
(313, 0, 1, 127),
(314, 0, 2, 127),
(315, 0, 3, 127),
(316, 0, 1, 128),
(317, 0, 2, 128),
(318, 0, 3, 128),
(319, 0, 1, 129),
(320, 0, 2, 129),
(321, 0, 3, 129),
(322, 0, 1, 130),
(323, 0, 2, 130),
(324, 0, 3, 130),
(325, 0, 1, 131),
(326, 0, 2, 131),
(327, 0, 3, 131),
(328, 0, 1, 132),
(329, 0, 2, 132),
(330, 0, 3, 132),
(331, 0, 1, 133),
(332, 0, 2, 133),
(333, 0, 3, 133),
(334, 0, 1, 134),
(335, 0, 2, 134),
(336, 0, 3, 134),
(337, 0, 1, 135),
(338, 0, 2, 135),
(339, 0, 3, 135),
(340, 0, 1, 136),
(341, 0, 2, 136),
(342, 0, 3, 136),
(343, 0, 1, 137),
(344, 0, 2, 137),
(345, 0, 3, 137),
(346, 0, 1, 138),
(347, 0, 2, 138),
(348, 0, 3, 138),
(349, 0, 1, 139),
(350, 0, 2, 139),
(351, 0, 3, 139),
(352, 0, 1, 140),
(353, 0, 2, 140),
(354, 0, 3, 140),
(355, 0, 1, 141),
(356, 0, 2, 141),
(357, 0, 3, 141),
(358, 0, 1, 142),
(359, 0, 2, 142),
(360, 0, 3, 142),
(361, 0, 1, 143),
(362, 0, 2, 143),
(363, 0, 3, 143),
(364, 0, 1, 144),
(365, 0, 2, 144),
(366, 0, 3, 144),
(367, 0, 1, 145),
(368, 0, 2, 145),
(369, 0, 3, 145),
(370, 0, 1, 146),
(371, 0, 2, 146),
(372, 0, 3, 146),
(373, 0, 1, 147),
(374, 0, 2, 147),
(375, 0, 3, 147),
(376, 0, 1, 148),
(377, 0, 2, 148),
(378, 0, 3, 148),
(379, 0, 1, 149),
(380, 0, 2, 149),
(381, 0, 3, 149),
(382, 0, 1, 150),
(383, 0, 2, 150),
(384, 0, 3, 150),
(385, 0, 1, 151),
(386, 0, 2, 151),
(387, 0, 3, 151),
(388, 0, 1, 152),
(389, 0, 2, 152),
(390, 0, 3, 152),
(391, 0, 1, 153),
(392, 0, 2, 153),
(393, 0, 3, 153),
(394, 0, 1, 154),
(395, 0, 2, 154),
(396, 0, 3, 154),
(397, 0, 1, 155),
(398, 0, 2, 155),
(399, 0, 3, 155),
(400, 0, 1, 156),
(401, 0, 2, 156),
(402, 0, 3, 156),
(403, 0, 1, 157),
(404, 0, 2, 157),
(405, 0, 3, 157),
(406, 0, 1, 158),
(407, 0, 2, 158),
(408, 0, 3, 158),
(409, 0, 1, 159),
(410, 0, 2, 159),
(411, 0, 3, 159),
(412, 0, 1, 160),
(413, 0, 2, 160),
(414, 0, 3, 160),
(415, 0, 1, 161),
(416, 0, 2, 161),
(417, 0, 3, 161),
(418, 0, 1, 162),
(419, 0, 2, 162),
(420, 0, 3, 162),
(421, 0, 1, 163),
(422, 0, 2, 163),
(423, 0, 3, 163),
(424, 0, 1, 164),
(425, 0, 2, 164),
(426, 0, 3, 164),
(427, 0, 1, 165),
(428, 0, 2, 165),
(429, 0, 3, 165),
(430, 0, 1, 166),
(431, 0, 2, 166),
(432, 0, 3, 166),
(433, 0, 1, 167),
(434, 0, 2, 167),
(435, 0, 3, 167),
(436, 0, 1, 168),
(437, 0, 2, 168),
(438, 0, 3, 168),
(439, 0, 1, 169),
(440, 0, 2, 169),
(441, 0, 3, 169),
(442, 0, 1, 170),
(443, 0, 2, 170),
(444, 0, 3, 170),
(445, 0, 1, 171),
(446, 0, 2, 171),
(447, 0, 3, 171),
(448, 0, 1, 172),
(449, 0, 2, 172),
(450, 0, 3, 172),
(451, 0, 1, 173),
(452, 0, 2, 173),
(453, 0, 3, 173),
(454, 0, 1, 174),
(455, 0, 2, 174),
(456, 0, 3, 174),
(457, 0, 1, 175),
(458, 0, 2, 175),
(459, 0, 3, 175),
(460, 0, 1, 176),
(461, 0, 2, 176),
(462, 0, 3, 176),
(463, 0, 1, 177),
(464, 0, 2, 177),
(465, 0, 3, 177),
(466, 0, 1, 178),
(467, 0, 2, 178),
(468, 0, 3, 178),
(469, 0, 1, 179),
(470, 0, 2, 179),
(471, 0, 3, 179),
(472, 0, 1, 180),
(473, 0, 2, 180),
(474, 0, 3, 180),
(475, 0, 1, 181),
(476, 0, 2, 181),
(477, 0, 3, 181),
(478, 0, 1, 182),
(479, 0, 2, 182),
(480, 0, 3, 182),
(481, 0, 1, 183),
(482, 0, 2, 183),
(483, 0, 3, 183),
(484, 0, 1, 184),
(485, 0, 2, 184),
(486, 0, 3, 184),
(487, 0, 1, 185),
(488, 0, 2, 185),
(489, 0, 3, 185),
(490, 0, 1, 186),
(491, 0, 2, 186),
(492, 0, 3, 186),
(493, 0, 1, 187),
(494, 0, 2, 187),
(495, 0, 3, 187),
(496, 0, 1, 188),
(497, 0, 2, 188),
(498, 0, 3, 188),
(499, 0, 1, 189),
(500, 0, 2, 189),
(501, 0, 3, 189),
(502, 0, 1, 190),
(503, 0, 2, 190),
(504, 0, 3, 190),
(505, 0, 1, 191),
(506, 0, 2, 191),
(507, 0, 3, 191),
(508, 0, 1, 192),
(509, 0, 2, 192),
(510, 0, 1, 193),
(511, 0, 2, 193),
(512, 0, 3, 193),
(513, 0, 1, 194),
(514, 0, 2, 194),
(515, 0, 3, 194),
(516, 0, 1, 195),
(517, 0, 2, 195),
(518, 0, 3, 195),
(519, 0, 1, 196),
(520, 0, 2, 196),
(521, 0, 3, 196),
(522, 0, 1, 197),
(523, 0, 2, 197),
(524, 0, 3, 197),
(525, 0, 1, 198),
(526, 0, 2, 198),
(527, 0, 3, 198),
(528, 0, 1, 199),
(529, 0, 2, 199),
(530, 0, 3, 199),
(531, 0, 1, 200),
(532, 0, 2, 200),
(533, 0, 3, 200),
(534, 0, 1, 201),
(535, 0, 2, 201),
(536, 0, 3, 201),
(537, 0, 1, 202),
(538, 0, 2, 202),
(539, 0, 3, 202),
(540, 0, 1, 203),
(541, 0, 2, 203),
(542, 0, 3, 203),
(543, 0, 1, 204),
(544, 0, 2, 204),
(545, 0, 3, 204),
(546, 0, 1, 205),
(547, 0, 2, 205),
(548, 0, 3, 205),
(549, 0, 1, 206),
(550, 0, 2, 206),
(551, 0, 3, 206),
(552, 0, 1, 207),
(553, 0, 2, 207),
(554, 0, 3, 207),
(555, 0, 1, 208),
(556, 0, 2, 208),
(557, 0, 3, 208),
(558, 0, 1, 209),
(559, 0, 2, 209),
(560, 0, 3, 209),
(561, 0, 1, 210),
(562, 0, 2, 210),
(563, 0, 3, 210),
(564, 0, 1, 212),
(565, 0, 2, 212),
(566, 0, 3, 212),
(567, 0, 1, 214),
(568, 0, 2, 214),
(569, 0, 3, 214),
(570, 0, 1, 215),
(571, 0, 2, 215),
(572, 0, 3, 215),
(573, 0, 1, 216),
(574, 0, 2, 216),
(575, 0, 3, 216),
(576, 0, 1, 217),
(577, 0, 2, 217),
(578, 0, 3, 217),
(579, 0, 1, 218),
(580, 0, 2, 218),
(581, 0, 3, 218),
(582, 0, 1, 219),
(583, 0, 2, 219),
(584, 0, 3, 219),
(585, 0, 1, 220),
(586, 0, 2, 220),
(587, 0, 3, 220),
(588, 0, 1, 221),
(589, 0, 2, 221),
(590, 0, 3, 221),
(591, 0, 1, 222),
(592, 0, 2, 222),
(593, 0, 3, 222),
(594, 0, 1, 223),
(595, 0, 2, 223),
(596, 0, 3, 223),
(597, 0, 1, 224),
(598, 0, 2, 224),
(599, 0, 3, 224),
(600, 0, 1, 226),
(601, 0, 2, 226),
(602, 0, 3, 226),
(603, 0, 1, 228),
(604, 0, 2, 228),
(605, 0, 3, 228),
(606, 0, 101, 229),
(607, 0, 101, 230),
(608, 0, 101, 231),
(609, 0, 101, 232),
(610, 0, 101, 233),
(611, 0, 101, 234),
(612, 0, 101, 235),
(613, 0, 101, 236),
(614, 0, 101, 237),
(615, 0, 101, 238),
(616, 0, 101, 239),
(617, 0, 101, 240),
(618, 1, 101, 241),
(619, 0, 101, 241);

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

--
-- Daten für Tabelle `gatewayinstance`
--

INSERT INTO `gatewayinstance` (`id`, `type`, `gateway_state`) VALUES
(100, 'AND', 'init'),
(636, 'AND', 'init'),
(639, 'AND', 'init'),
(642, 'AND', 'init'),
(645, 'AND', 'init'),
(648, 'AND', 'terminated'),
(651, 'AND', 'init'),
(652, 'AND', 'terminated'),
(655, 'AND', 'terminated'),
(656, 'AND', 'terminated'),
(659, 'AND', 'init');

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
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=102 ;

--
-- Daten für Tabelle `scenario`
--

INSERT INTO `scenario` (`id`, `name`) VALUES
(1, 'HELLOWORLD'),
(2, 'helloWorld2'),
(100, 'TestScenario'),
(101, 'Test Insert Scenario');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scenarioinstance`
--

CREATE TABLE IF NOT EXISTS `scenarioinstance` (
`id` int(11) NOT NULL,
  `scenario_id` int(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=242 ;

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
(83, 1),
(84, 1),
(85, 100),
(86, 100),
(88, 101),
(89, 101),
(90, 101),
(91, 101),
(92, 1),
(93, 101),
(94, 1),
(95, 1),
(96, 101),
(97, 1),
(98, 101),
(99, 1),
(100, 101),
(101, 1),
(102, 101),
(103, 1),
(104, 1),
(105, 1),
(106, 1),
(107, 1),
(108, 1),
(109, 1),
(110, 1),
(111, 1),
(112, 1),
(113, 1),
(114, 1),
(115, 1),
(116, 1),
(117, 1),
(118, 1),
(119, 1),
(120, 1),
(121, 1),
(122, 1),
(123, 1),
(124, 1),
(125, 1),
(126, 1),
(127, 1),
(128, 1),
(129, 1),
(130, 1),
(131, 1),
(132, 1),
(133, 1),
(134, 1),
(135, 1),
(136, 1),
(137, 1),
(138, 1),
(139, 1),
(140, 1),
(141, 1),
(142, 1),
(143, 1),
(144, 1),
(145, 1),
(146, 1),
(147, 1),
(148, 1),
(149, 1),
(150, 1),
(151, 1),
(152, 1),
(153, 1),
(154, 1),
(155, 1),
(156, 1),
(157, 1),
(158, 1),
(159, 1),
(160, 1),
(161, 1),
(162, 1),
(163, 1),
(164, 1),
(165, 1),
(166, 1),
(167, 1),
(168, 1),
(169, 1),
(170, 1),
(171, 1),
(172, 1),
(173, 1),
(174, 1),
(175, 1),
(176, 1),
(177, 1),
(178, 1),
(179, 1),
(180, 1),
(181, 1),
(182, 1),
(183, 1),
(184, 1),
(185, 1),
(186, 1),
(187, 1),
(188, 1),
(189, 1),
(190, 1),
(191, 1),
(192, 1),
(193, 1),
(194, 1),
(195, 1),
(196, 1),
(197, 1),
(198, 1),
(199, 1),
(200, 1),
(201, 1),
(202, 1),
(203, 1),
(204, 1),
(205, 1),
(206, 1),
(207, 1),
(208, 1),
(209, 1),
(210, 1),
(211, 101),
(212, 1),
(213, 101),
(214, 1),
(215, 1),
(216, 1),
(217, 1),
(218, 1),
(219, 1),
(220, 1),
(221, 1),
(222, 1),
(223, 1),
(224, 1),
(225, 101),
(226, 1),
(227, 101),
(228, 1),
(229, 2),
(230, 2),
(231, 2),
(232, 2),
(233, 2),
(234, 2),
(235, 2),
(236, 2),
(237, 2),
(238, 2),
(239, 2),
(240, 2),
(241, 2);

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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=659;
--
-- AUTO_INCREMENT for table `configuration`
--
ALTER TABLE `configuration`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `controlnode`
--
ALTER TABLE `controlnode`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=107;
--
-- AUTO_INCREMENT for table `controlnodeinstance`
--
ALTER TABLE `controlnodeinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=660;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `dataobject`
--
ALTER TABLE `dataobject`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `dataobjectinstance`
--
ALTER TABLE `dataobjectinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=113;
--
-- AUTO_INCREMENT for table `dataset`
--
ALTER TABLE `dataset`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=14;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=102;
--
-- AUTO_INCREMENT for table `fragmentinstance`
--
ALTER TABLE `fragmentinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=620;
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
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=102;
--
-- AUTO_INCREMENT for table `scenarioinstance`
--
ALTER TABLE `scenarioinstance`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=242;
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
