package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import java.io.*;
import java.net.*;

/*
As a part of the JComparser we need to retrieve XML docus from a source URL like the Repo from the Processeditor.
 */

public class Retrieval {
    public String getHTML(String urlToRead) {
        /* credits to Kalpak http://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java */
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
