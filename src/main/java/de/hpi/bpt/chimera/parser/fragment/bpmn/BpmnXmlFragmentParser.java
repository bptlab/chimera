package de.hpi.bpt.chimera.parser.fragment.bpmn;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import de.hpi.bpt.chimera.model.fragment.bpmn.BpmnFragment;
import de.hpi.bpt.chimera.parser.CaseModelParserHelper;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.XmlUnmarshaller;
import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class BpmnXmlFragmentParser {
	private static Logger log = Logger.getLogger(BpmnXmlFragmentParser.class.getName());

	private BpmnXmlFragmentParser() {
	}

	public static BpmnFragment parseBpmnXmlFragment(String xmlFragmentString, CaseModelParserHelper parserHelper) {
		BpmnFragment fragment = new BpmnFragment();


		try {
			FragmentXmlWrapper fragXmlWrap = XmlUnmarshaller.buildFragment(xmlFragmentString);

			fragment.setId(fragXmlWrap.getId());

			// it's important to first create a new SequenceFlowResolver
			// After that all Parser can youse this resolver to resolve their
			// Associations and register themselves as the End of these
			// Associations in the Resolver
			// You have parse alle ControlNodes so that for every Outgoing and
			// Incoming Association for every ControlNode the Resolver is called
			// IN THE END the Resolver holds all the resolved Associations which
			// then can be added to the final parsed fragment
			SequenceFlowResolver sfResolver = new SequenceFlowResolver(fragXmlWrap);
			log.info("created sfResolver");
			// ^has to be called first
			DataFlowResolver dfResolver = new DataFlowResolver(fragXmlWrap, parserHelper);
			log.info("created dfResolver");
			EventParser.parseEvents(fragment, fragXmlWrap, sfResolver, dfResolver);
			log.info("parsed Events");
			ActivityParser.parseActivities(fragment, fragXmlWrap, sfResolver, dfResolver);
			log.info("parsed Activities");
			fragment.setSequenceFlowAssociations(sfResolver.getResolvedSequenceFlowAssociations());
			log.info("set sfa");
			fragment.setDataNodes(dfResolver.getResolvedDataNodes());
			log.info("set datanodes");
			// ^has to be called last
		} catch (JAXBException e) {
			log.error(e);
		}

		

		return fragment;
	}
}
