package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.lang.String;


public class JComparser {

    public static void main(String args[]) {

        /* Settings */
        boolean retrieval_by_url = true;
        boolean rest_option = false;
        boolean file_upload = true;

        boolean bpmn_img_retrieval = false;

        /* Initialization */
        String Processeditor_server_url = "http://172.16.64.113:1205/";

        if (file_upload) {
            FileUpload jUpload = new FileUpload();
        }

        if (rest_option) {
            REST jREST = new REST();
        }
        if (retrieval_by_url) {
            Retrieval jRetrieval = new Retrieval();
            ArrayList<String> scenarioXML_List = new ArrayList<>();
            List<String> scenariosURL_list = new ArrayList<>();

            String response_list = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url + "models");
            scenariosURL_list = de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.selectScenarioURLS(response_list);
            String modelXML = "";

            for (int i = 0; i < scenariosURL_list.size(); i++) {
                modelXML = jRetrieval.getHTMLwithAuth(Processeditor_server_url, scenariosURL_list.get(i));
                scenarioXML_List.add(modelXML);
            }
            int fragmentID;
            ArrayList<String> controlNodes;
            for(String scenarioXML: scenarioXML_List){
                int scenarioId;
                Parser parser = new Parser(scenarioXML); //extract scenarioName
                Connector connector = new Connector();
                scenarioId = connector.insertScenarioIntoDatabase(parser.getScenarioName());
                HashMap<Integer, String> fragments = parser.getFragmentDetails();
                for (Map.Entry<Integer, String> fragment: fragments.entrySet()) {
                    fragmentID =  connector.insertFragmentIntoDatabase(fragment.getValue(), scenarioId);
                    controlNodes = parser.getControlNodesForFragment(fragment.getKey());

                }
            }



            //de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(scenarioXML_List);



            /*???????????????????????
            String xml_response ="";
            xml_response = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url);
            xml_response = xml_response.replaceAll("[^\\x20-\\x7e]", "");
            handleFileRetrieval(xml_response);*/
        }

        if (bpmn_img_retrieval) {
            //retrieve an image for each model via its modelid
            Retrieval jRetrieval = new Retrieval();
            List<String> pcm = new ArrayList<String>();
            List<String> models_list = new ArrayList<String>();


            String response_list = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url + "modelid.png");
        }
    }

    public static void handleFileUpload(List pcm) {
/*
        int pcm_size = pcm.size();
        String pcm_item = "";

        List<String> pcm_list = new ArrayList<String>();
        Object xml_path_url = pcm.get(1);

        for(int i=0; i < pcm_size; i++) {
            pcm_item = (String) pcm.get(i);
            xml_path_url = pcm_item;
            InputStream xml_content;

            try {
                xml_content = new FileInputStream(xml_path_url);
                pcm_list.add(xml_content);
            } catch (IOException e) {
                System.out.println("Error in Reading file ");
                System.out.println(xml_path_url);
            }
        }

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm);
*/
    }

    public static void handleFileRetrieval(String pcm) {

        ArrayList<String> pcm_list = new ArrayList<String>();
        pcm_list.add(pcm);

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm_list);
    }

}
