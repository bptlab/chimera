//package de.uni_potsdam.hpi.bpt.bp2014.jcomparser.xml;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
///**
// *
// */
//public class DatabaseFragmentTest {
//    @Test
//    public void testFragment() {
//        DatabaseFragment fragment = new DatabaseFragment();
//        File scenario = new File("src/test/resources/fragments/fragment1.xml");
//        try {
//            String xml = FileUtils.readFileToString(scenario);
//            Map<Long, DataClass> idToDataClass = new HashMap<>();
//            fragment.initialize(xml, 1, "someName", 1, idToDataClass);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}