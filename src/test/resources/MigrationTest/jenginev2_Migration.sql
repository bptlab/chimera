
INSERT INTO `activityinstance` (`id`, `type`, `role_id`, `activity_state`, `workitem_state`) VALUES
(6304, 'HumanTask', 1, 'ready', 'init'),
(6305, 'HumanTask', 1, 'ready', 'init'),
(6306, 'HumanTask', 1, 'ready', 'init'),
(6307, 'HumanTask', 1, 'ready', 'init');

INSERT INTO `activitystate` (`state`) VALUES
('init'),
('ready'),
('ready(ControlFlow)'),
('ready(Data)'),
('running'),
('skipped'),
('terminated');

INSERT INTO `controlflow` (`controlnode_id1`, `controlnode_id2`, `condition`) VALUES
(370, 372, ''),
(372, 371, ''),
(373, 374, ''),
(374, 375, '');

INSERT INTO `controlnode` (`id`, `label`, `type`, `fragment_id`, `modelid`) VALUES
(370, '', 'Startevent', 163, 990905013),
(371, '', 'Endevent', 163, 30057439),
(372, 'TestAktivitaet1', 'Activity', 163, 214334952),
(373, '', 'Startevent', 164, 582369356),
(374, 'TestAktivitaet2', 'Activity', 164, 426160629),
(375, '', 'Endevent', 164, 1699219107);

INSERT INTO `controlnodeinstance` (`id`, `Type`, `controlnode_id`, `fragmentinstance_id`) VALUES
(6304, 'Activity', 372, 4273),
(6305, 'Activity', 374, 4274),
(6306, 'Activity', 372, 4275),
(6307, 'Activity', 374, 4276);

INSERT INTO `dataclass` (`id`, `name`) VALUES
(36, 'DO1'),
(37, 'DO2');

INSERT INTO `dataflow` (`controlnode_id`, `dataset_id`, `input`) VALUES
(372, 143, 1),
(374, 144, 1),
(374, 145, 0);

INSERT INTO `datanode` (`id`, `scenario_id`, `state_id`, `dataclass_id`, `dataobject_id`, `modelid`) VALUES
(116, 144, 106, 37, 23, 1343047300),
(117, 144, 105, 36, 24, 377219354),
(118, 144, 107, 36, 24, 500103237);

INSERT INTO `dataobject` (`id`, `name`, `dataclass_id`, `scenario_id`, `start_state_id`) VALUES
(23, 'DO2', 37, 144, 106),
(24, 'DO1', 36, 144, 105);

INSERT INTO `dataobjectinstance` (`id`, `scenarioinstance_id`, `state_id`, `dataobject_id`, `onchange`) VALUES
(730, 902, 106, 23, 0),
(731, 902, 105, 24, 0),
(732, 903, 106, 23, 0),
(733, 903, 105, 24, 0);

INSERT INTO `dataset` (`id`, `input`) VALUES
(143, 1),
(144, 1),
(145, 0);

INSERT INTO `datasetconsistsofdatanode` (`dataset_id`, `datanode_id`) VALUES
(143, 117),
(144, 116),
(145, 118);

INSERT INTO `fragment` (`id`, `name`, `scenario_id`, `modelid`, `modelversion`) VALUES
(163, 'Fragment1', 144, 1946280459, 0),
(164, 'Fragment2', 144, 870945042, 0);

INSERT INTO `fragmentinstance` (`id`, `terminated`, `fragment_id`, `scenarioinstance_id`) VALUES
(4273, 0, 163, 902),
(4274, 0, 164, 902),
(4275, 0, 163, 903),
(4276, 0, 164, 903);

INSERT INTO `scenario` (`id`, `name`, `modelid`, `modelversion`, `datamodelid`, `datamodelversion`) VALUES
(144, 'Testszenario', 1442712096, 0, NULL, NULL);

INSERT INTO `scenarioinstance` (`id`, `terminated`, `scenario_id`) VALUES
(902, 0, 144),
(903, 0, 144);

INSERT INTO `state` (`id`, `name`, `olc_id`) VALUES
(105, 'init', 36),
(106, 'init', 37),
(107, 'terminated', 36);