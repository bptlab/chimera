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
import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class JComparser {

   /* public static void main(String args[]) {

        *//* Settings *//*
        boolean retrieval_by_url = true;
        boolean rest_option = false;
        boolean file_upload = true;

        boolean bpmn_img_retrieval = false;

        *//* Initialization *//*
        String xml_response = "";
        String Processeditor_server_url = "http://172.16.64.113:1205/";

        if (file_upload) {
            FileUpload jUpload = new FileUpload();
        }

        if (rest_option) {
            REST jREST = new REST();
        }
        if (retrieval_by_url) {
            Retrieval jRetrieval = new Retrieval();
            List<String> pcm = new ArrayList<String>();
            List<String> models_list = new ArrayList<String>();


            String response_list = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url + "models");
            models_list = de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parseModelList(response_list);
            int models_size = models_list.size();
            String response_item = "";

            for (int i = 0; i < models_size; i++) {
                //    response_item = response_list.get(i);
                xml_response = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url + "models/" + response_item + ".pm");
                pcm.add(xml_response);
            }
            //handleFileRetrieval(pcm);


            InputStream in = null;
            *//* try {
               in = post.getResponseBodyAsStream();
            } catch (IOException e) {
                e.printStackTrace();
            }*//*
            System.out.println(in);

            // jRetrieval = new Retrieval();
            xml_response = jRetrieval.getHTMLwithAuth(Processeditor_server_url, Processeditor_server_url);
            xml_response = xml_response.replaceAll("[^\\x20-\\x7e]", "");
            handleFileRetrieval(xml_response);
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

        int pcm_size = pcm.size();
        String pcm_item = "";

        // List<String> pcm_list = new ArrayList<String>();
        // Object xml_path_url = pcm.get(1);

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

    }

    public static void handleFileRetrieval(String pcm) {

        List<String> pcm_list = new ArrayList<String>();
        pcm_list.add(pcm);

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm_list);
    }*/

}
