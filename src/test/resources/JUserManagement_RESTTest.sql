-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 20, 2015 at 09:34 AM
-- Server version: 5.5.41-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `JUserManagement`
--

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `rolename`, `description`, `admin_id`) VALUES
(1, 'service_mitarbeiter', 'Die Bearbeiten Antraege und rufen Leute an', 2),
(10, 'manager', 'Die koordinieren und managen die Aufgaben', 0);

-- --------------------------------------------------------

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `role_id`, `description`) VALUES
(1, 'max', 1, 'Max der Grosse'),
(2, 'robert', 1, 'Mitarbeiter des Monats, admin der rolle 1'),
(3, 'Lisa', 1, ''),
(4, 'Steffi', 10, 'Manager'),
(5, 'Rolf', 10, 'Top Manager');
