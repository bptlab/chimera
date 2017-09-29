package de.hpi.bpt.chimera.parser.fragment.bpmn;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.XmlUnmarshaller;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public final class BpmnXmlFragmentParser {
	private static Logger log = Logger.getLogger(BpmnXmlFragmentParser.class.getName());

	private BpmnXmlFragmentParser() {
	}

	/**
	 * Parses a given XMl-String which represents a Fragment. Therefore first,
	 * all the Sequence- and Dataflows are parsed and stored. Then all
	 * Activities, Events and Datanodes are parsed using the former parsed
	 * Sequence- and Dataflows. So in Activities, Events, Datanodes aren't
	 * separated from their "Flows" any more. In the end a BpmFragment data
	 * structure is built up, where every Activiy/Event/Datanode has a reference
	 * to its incoming and and outgoing Sequence- or Dataflows and therby a
	 * reference to its predecessors and successors.
	 * 
	 * @param xmlFragmentString
	 * @param parserHelper
	 * @return the built BpmnFragment
	 */
	public static BpmnFragment parseBpmnXmlFragment(String xmlFragmentString, CaseModelParserHelper parserHelper) {
		BpmnFragment fragment = new BpmnFragment();


		try {
			FragmentXmlWrapper fragXmlWrap = XmlUnmarshaller.buildFragment(xmlFragmentString);

			fragment.setId(fragXmlWrap.getId());

			// it's important to first create a new SequenceFlowResolver
			// After that all Parser can use this resolver to resolve their
			// Associations and register themselves as the End of these
			// Associations in the Resolver
			// You have to parse all ControlNodes so that for every Outgoing and
			// Incoming Association for every ControlNode the Resolver is called
			// IN THE END the Resolver holds all the resolved Associations which
			// then can be added to the final parsed fragment
			SequenceFlowResolver sfResolver = new SequenceFlowResolver(fragXmlWrap);
			// ^has to be called first
			DataFlowResolver dfResolver = new DataFlowResolver(fragXmlWrap, parserHelper);

			EventParser.parseEvents(fragment, fragXmlWrap, sfResolver, dfResolver);
			ActivityParser.parseActivities(fragment, fragXmlWrap, sfResolver, dfResolver);
			GatewayParser.parseGateways(fragment, fragXmlWrap, sfResolver);

			fragment.setSequenceFlowAssociations(sfResolver.getResolvedSequenceFlowAssociations());
			fragment.setDataNodes(dfResolver.getResolvedDataNodes());
			// ^has to be called last
		} catch (JAXBException e) {
			log.error(e);
		}

		

		return fragment;
	}
}
