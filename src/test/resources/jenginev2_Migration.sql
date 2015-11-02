INSERT INTO `activityinstance` (`id`, `type`, `automaticexecution`, `canTerminate`, `role_id`, `activity_state`, `workitem_state`) VALUES
(2, 'HumanTask', 0, 0, 1, 'ready', 'init'),
(3, 'WebServiceTask', 1, 1, 1, 'terminated', 'init'),
(5, 'HumanTask', 0, 0, 1, 'ready', 'init');

INSERT INTO `aggregation` (`dataclass_id1`, `dataclass_id2`, `multiplicity`) VALUES
(1, 2, 2147483647);

INSERT INTO `controlflow` (`controlnode_id1`, `controlnode_id2`, `condition`) VALUES
(1, 4, ''),
(2, 5, ''),
(3, 5, ''),
(4, 2, ''),
(4, 3, ''),
(5, 6, ''),
(7, 8, ''),
(9, 10, ''),
(10, 11, 'DEFAULT'),
(10, 12, 'DO.Attr1 > 0'),
(11, 7, ''),
(12, 7, ''),
(13, 9, '');

INSERT INTO `controlnode` (`id`, `label`, `type`, `fragment_id`, `modelid`) VALUES
(1, '', 'Startevent', 1, 1069345757),
(2, 'Ref', 'Activity', 1, 826790323),
(3, 'A1', 'WebServiceTask', 1, 517729148),
(4, '', 'AND', 1, 1569336784),
(5, '', 'AND', 1, 2081480666),
(6, '', 'Endevent', 1, 1914825610),
(7, '', 'XOR', 2, 564114893),
(8, '', 'Endevent', 2, 2136115766),
(9, 'Ref', 'Activity', 2, 826790323),
(10, '', 'XOR', 2, 1689490932),
(11, 'B2', 'EmailTask', 2, 1821614206),
(12, 'B1', 'Activity', 2, 1407636184),
(13, '', 'Startevent', 2, 509231813);

INSERT INTO `controlnodeinstance` (`id`, `Type`, `controlnode_id`, `fragmentinstance_id`) VALUES
(1, 'AND', 4, 1),
(2, 'Activity', 2, 1),
(3, 'Activity', 3, 1),
(4, 'AND', 5, 1),
(5, 'Activity', 9, 2);

INSERT INTO `dataattribute` (`id`, `name`, `type`, `default`, `dataclass_id`) VALUES
(1, 'Attr1', '', '', 1),
(2, 'Attr2', '', '', 1),
(3, 'Attr3', '', '', 2);

INSERT INTO `dataattributeinstance` (`id`, `value`, `dataattribute_id`, `dataobjectinstance_id`) VALUES
(1, '', 3, 1),
(2, '', 1, 2),
(3, '', 2, 2);

INSERT INTO `dataclass` (`id`, `name`, `rootnode`) VALUES
(1, 'DO', 1),
(2, 'SubD', 0);

INSERT INTO `dataflow` (`controlnode_id`, `dataset_id`, `input`) VALUES
(3, 1, 1),
(3, 2, 0),
(9, 3, 1),
(11, 5, 0),
(12, 4, 1),
(12, 6, 0);

INSERT INTO `datanode` (`id`, `scenarioId`, `state_id`, `dataclass_id`, `dataobject_id`, `modelid`) VALUES
(1, 1, 2, 2, 1, 1517694277),
(2, 1, 1, 2, 1, 1368161079),
(3, 1, 4, 1, 2, 650069438),
(4, 1, 3, 1, 2, 135409402),
(5, 1, 3, 1, 2, 155099451),
(6, 1, 4, 1, 2, 611573211);

INSERT INTO `dataobject` (`id`, `name`, `dataclass_id`, `scenarioId`, `start_state_id`) VALUES
(1, 'SubDO', 2, 1, 1),
(2, 'DO', 1, 1, 3);

INSERT INTO `dataobjectinstance` (`id`, `scenarioinstance_id`, `state_id`, `dataobject_id`, `onchange`) VALUES
(1, 1, 1, 1, 0),
(2, 1, 4, 2, 0);

INSERT INTO `dataset` (`id`, `input`) VALUES
(1, 1),
(2, 0),
(3, 1),
(4, 1),
(5, 0),
(6, 0);

INSERT INTO `datasetconsistsofdatanode` (`dataset_id`, `datanode_id`) VALUES
(1, 4),
(2, 3),
(3, 6),
(4, 2),
(5, 5),
(6, 1);

INSERT INTO `emailconfiguration` (`id`, `receivermailaddress`, `sendmailaddress`, `subject`, `message`, `controlnode_id`) VALUES
(1, 'bp2014w1@byom.de', 'bp2014w01@framsteg.org', 'Test', 'Test Message', 11);

INSERT INTO `fragment` (`id`, `name`, `scenarioId`, `modelid`, `modelversion`) VALUES
(1, 'FragmentA', 1, 1084827857, 0),
(2, 'FragmentB', 1, 894096069, 0);

INSERT INTO `fragmentinstance` (`id`, `terminated`, `fragment_id`, `scenarioinstance_id`) VALUES
(1, 0, 1, 1),
(2, 0, 2, 1);

INSERT INTO `gatewayinstance` (`id`, `type`, `gateway_state`) VALUES
(1, 'AND', 'terminated'),
(4, 'AND', 'init');

INSERT INTO `reference` (`controlnode_id1`, `controlnode_id2`) VALUES
(2, 9),
(9, 2);

INSERT INTO `scenario` (`id`, `name`, `deleted`, `modelid`, `modelversion`, `datamodelid`, `datamodelversion`) VALUES
(1, 'Scenario', 0, 358512, 1, 790983467, 0);

INSERT INTO `scenarioinstance` (`id`, `name`, `terminated`, `scenarioId`) VALUES
(1, 'Scenario', 0, 1);

INSERT INTO `state` (`id`, `name`, `olc_id`) VALUES
(1, 'init', 2),
(2, 'end', 2),
(3, 'init', 1),
(4, 'state1', 1);

INSERT INTO `terminationcondition` (`conditionset_id`, `dataobject_id`, `state_id`, `scenarioId`) VALUES
(1, 1, 2, 1),
(1, 2, 4, 1),
(2, 1, 1, 1);