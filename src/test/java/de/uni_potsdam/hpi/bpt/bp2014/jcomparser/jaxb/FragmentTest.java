//package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.jaxb;
//
//import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Fragment;
//import org.junit.Test;
//import org.w3c.dom.Document;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//
//import static org.junit.Assert.*;
//
///**
// *
// */
//public class FragmentTest {
//    @Test
//    public void testScenarioDeserialization() {
//        String fragmentString =
//            "<bpmn:process id=\"Process_1\" isExecutable=\"false\">\n" +
//            "    <bpmn:startEvent id=\"StartEvent_1\">\n" +
//            "      <bpmn:outgoing>SequenceFlow_0ekkkxc</bpmn:outgoing>\n" +
//            "    </bpmn:startEvent>\n" +
//            "    <bpmn:task id=\"Task_1i91t0u\" name=\"First\">\n" +
//            "      <bpmn:incoming>SequenceFlow_0ekkkxc</bpmn:incoming>\n" +
//            "      <bpmn:outgoing>SequenceFlow_1yduzmd</bpmn:outgoing>\n" +
//            "      <bpmn:dataOutputAssociation id=\"DataOutputAssociation_0kggjsg\">\n" +
//            "        <bpmn:targetRef>DataObjectReference_09uorcc</bpmn:targetRef>\n" +
//            "      </bpmn:dataOutputAssociation>\n" +
//            "      <bpmn:dataOutputAssociation id=\"DataOutputAssociation_0xvrgfq\">\n" +
//            "        <bpmn:targetRef>DataObjectReference_082q2p3</bpmn:targetRef>\n" +
//            "      </bpmn:dataOutputAssociation>\n" +
//            "      <bpmn:dataOutputAssociation id=\"DataOutputAssociation_0j6w1o6\">\n" +
//            "        <bpmn:targetRef>DataObjectReference_0p8oinj</bpmn:targetRef>\n" +
//            "      </bpmn:dataOutputAssociation>\n" +
//            "    </bpmn:task>\n" +
//            "    <bpmn:sequenceFlow id=\"SequenceFlow_0ekkkxc\" sourceRef=\"StartEvent_1\" targetRef=\"Task_1i91t0u\" />\n" +
//            "    <bpmn:task id=\"Task_16elx6t\" name=\"Second\">\n" +
//            "      <bpmn:incoming>SequenceFlow_1yduzmd</bpmn:incoming>\n" +
//            "      <bpmn:outgoing>SequenceFlow_0yz8125</bpmn:outgoing>\n" +
//            "      <bpmn:dataInputAssociation id=\"DataInputAssociation_16pupih\">\n" +
//            "        <bpmn:sourceRef>DataObjectReference_09uorcc</bpmn:sourceRef>\n" +
//            "      </bpmn:dataInputAssociation>\n" +
//            "      <bpmn:dataInputAssociation id=\"DataInputAssociation_0lfeecw\">\n" +
//            "        <bpmn:sourceRef>DataObjectReference_082q2p3</bpmn:sourceRef>\n" +
//            "      </bpmn:dataInputAssociation>\n" +
//            "      <bpmn:dataInputAssociation id=\"DataInputAssociation_1gfhtf7\">\n" +
//            "        <bpmn:sourceRef>DataObjectReference_0p8oinj</bpmn:sourceRef>\n" +
//            "      </bpmn:dataInputAssociation>\n" +
//            "    </bpmn:task>\n" +
//            "    <bpmn:sequenceFlow id=\"SequenceFlow_1yduzmd\" sourceRef=\"Task_1i91t0u\" targetRef=\"Task_16elx6t\" />\n" +
//            "    \n" +
//            "    <bpmn:dataObjectReference id=\"DataObjectReference_09uorcc\" name=\"ImportantDoc&#10;[unready]\" dataObjectRef=\"DataObject_034m4mt\" />\n" +
//            "    <bpmn:dataObject id=\"DataObject_034m4mt\" />\n" +
//            "    \n" +
//            "    <bpmn:dataObjectReference id=\"DataObjectReference_082q2p3\" name=\"ImportantDoc&#10;[ready]\" dataObjectRef=\"DataObject_1ak67zy\" />\n" +
//            "    <bpmn:dataObject id=\"DataObject_1ak67zy\" />\n" +
//            "    \n" +
//            "    <bpmn:dataObjectReference id=\"DataObjectReference_0p8oinj\" name=\"AnotherDoc\" dataObjectRef=\"DataObject_006n731\" />\n" +
//            "    <bpmn:dataObject id=\"DataObject_006n731\" />\n" +
//            "    \n" +
//            "    <bpmn:endEvent id=\"EndEvent_1w2bpyl\">\n" +
//            "      <bpmn:incoming>SequenceFlow_0yz8125</bpmn:incoming>\n" +
//            "    </bpmn:endEvent>\n" +
//            "    <bpmn:sequenceFlow id=\"SequenceFlow_0yz8125\" sourceRef=\"Task_16elx6t\" targetRef=\"EndEvent_1w2bpyl\" />\n" +
//            "  </bpmn:process>\n";
//        Document doc = XmlTestHelper.getDocumentFromXmlString(fragmentString);
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(Fragment.class);
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            Fragment fragment = (Fragment) jaxbUnmarshaller.unmarshal(doc);
//            assertEquals(3, fragment.getSequenceFlow().size());
//            assertEquals(3, fragment.getDataObjects().size());
//            assertEquals(3, fragment.getDataObjectReferences().size());
//            assertEquals(2, fragment.getTasks().size());
//       } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
//}