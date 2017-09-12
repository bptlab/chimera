package de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller;

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

import de.hpi.bpt.chimera.parser.fragment.bpmn.unmarshaller.xml.FragmentXmlWrapper;

public class XmlUnmarshaller {
	private static Logger log = Logger.getLogger(XmlUnmarshaller.class.getName());

	private XmlUnmarshaller() {
	}

	/**
	 * Build the Wrapper for the XML version of a Fragment.
	 * 
	 * @param fragmentXmlString
	 * @return FragmentXmlWrapper
	 * @throws JAXBException
	 */
	public static FragmentXmlWrapper buildFragment(String fragmentXmlString) throws JAXBException {
		Document doc = getXmlDocFromString(fragmentXmlString);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(FragmentXmlWrapper.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (FragmentXmlWrapper) jaxbUnmarshaller.unmarshal(doc);
		} catch (JAXBException e) {
			log.error(e);
			throw new JAXBException("Fragment xml was not valid");
		}
	}

	private static Document getXmlDocFromString(String xml) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			return dbf.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			log.error(e);
			throw new IllegalArgumentException("Creation of xml fragment failed");
		}
	}
}
