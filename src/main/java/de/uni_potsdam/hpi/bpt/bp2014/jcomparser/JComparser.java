package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class JComparser {
    public static void main(String args[]) {

        /* Settings */
        boolean retrieval_by_url = true;
        boolean rest_option = false;
        boolean file_upload = true;

        /* Initialization */
        String XML_response = "";
        //String Processeditor_server_url = "http://localhost:8080/models/"; -> Liste aller Modelle
        //String Processeditor_server_model_url = "http://localhost:8080/models/";
        String Processeditor_server_url = "http://hpi.framsteg.de/bp2014w1/PCMBeispiele/transportieren.model";

        if(file_upload){
            FileUpload jUpload = new FileUpload();
        }

        if (rest_option) {
            REST jREST = new REST();
        }
        if (retrieval_by_url) {
            /*
            1- first get a list of all models via Processeditor_server_url
            2 - then get each scenario via Processeditor_server_model_url

            Retrieval jRetrieval = new Retrieval();
            List<String> pcm = new ArrayList<String>();

            response_list = jRetrieval.getHTML(Processeditor_server_url);

            int response_size = response_list.size();
            String response_item = "";

            for(int i=0; i < response_size; i++) {
                response_item = response_list.get(i);
                XML_response = jRetrieval.getHTML(Processeditor_server_model_url + response_item + "/md/");
                pcm.add(XML_response);
            }
            handleFileRetrieval(pcm);
             */
            PostMethod post = new PostMethod("http://172.16.64.113:1205/users/login/");
            NameValuePair[] data = {
                    new NameValuePair("user", "root"),
                    new NameValuePair("password", "inubit")
            };
            post.setRequestBody(data);
            // execute method and handle any error responses.

            InputStream in = null;
            try {
                in = post.getResponseBodyAsStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(in);

            Retrieval jRetrieval = new Retrieval();
            XML_response = jRetrieval.getHTML(Processeditor_server_url);
            XML_response = XML_response.replaceAll("[^\\x20-\\x7e]", "");
            handleFileRetrieval(XML_response);
        }
    }

    public static void handleFileUpload(List pcm) {

        int pcm_size = pcm.size();
        String pcm_item = "";

        // List<String> pcm_list = new ArrayList<String>();
       // Object xml_path_url = pcm.get(1);

       /* for(int i=0; i < pcm_size; i++) {
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
        }*/

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm);
    }

    public static void handleFileRetrieval(String pcm) {

        List<String> pcm_list = new ArrayList<String>();
        pcm_list.add(pcm);

        de.uni_potsdam.hpi.bpt.bp2014.jcomparser.Parser.parsePCM(pcm_list);
    }
}