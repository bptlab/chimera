package de.hpi.bpt.chimera.parser.fragment.bpmnxml;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.hpi.bpt.chimera.jcomparser.jaxb.FragmentXmlWrapper;
import de.hpi.bpt.chimera.jcomparser.saving.Fragment;
import de.hpi.bpt.chimera.model.fragment.BpmnFragment.BpmnFragment;

public class BpmnXmlFragmentParser {
	private static Logger logger = Logger.getLogger(BpmnXmlFragmentParser.class.getName());

	public static BpmnFragment parseBpmnXmlFragment(String XmlFragmentString) {
		BpmnFragment fragment = new BpmnFragment();
		FragmentXmlWrapper fragXmlWrap = new FragmentXmlWrapper();

		try {
			fragXmlWrap = buildFragment(XmlFragmentString);
		} catch (JAXBException e) {
			logger.error(e);
			e.printStackTrace();
		}

		fragment.setId(fragXmlWrap.getId());
		
		//it's important to first create a new SequenceFlowResolver
		//After that all Parser can youse this resolver to resolve their Associations and register themselves as the End of these Associations in the Resolver
		//You have parse alle ControlNodes so that for every Outgoing and Incoming Association for every ControlNode the Resolver is called
		//IN THE END the Resolver holds all the resolved Associations which then can be added to the final parsed fragment
		SequenceFlowResolver sfResolver = new SequenceFlowResolver(fragXmlWrap);
		// ^has to be called first
		fragment.setStartEvent(EventParser.getStartEventFromXmlWrapper(fragXmlWrap, sfResolver));
		fragment.setEndEvent(EventParser.getEndEventFromXmlWrapper(fragXmlWrap, sfResolver));
		fragment.setTasks(ActivityParser.getActitityFromXmlWrapper(fragXmlWrap, sfResolver));
		fragment.setSequenceFlowAssociations(sfResolver.getResolvedSequenceFlowAssociations());
		// ^has to be called last

		return fragment;
	}


	private static FragmentXmlWrapper buildFragment(String fragmentXml) throws JAXBException {
		Document doc = getXmlDocFromString(fragmentXml);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FragmentXmlWrapper.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (FragmentXmlWrapper) jaxbUnmarshaller.unmarshal(doc);
		} catch (JAXBException e) {
			logger.error(e);
			throw new JAXBException("Fragment xml was not valid");
		}
	}

	private static Document getXmlDocFromString(String xml) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			logger.error(e);
			throw new IllegalArgumentException("Creation of xml fragment failed");
		}
	}
}
