package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

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

    public String getHTMLwithAuth(String hosturl, String urlToRead) {
        /* credits to  http://www.avajava.com/tutorials/lessons/how-do-i-connect-to-a-url-using-basic-authentication.html */

        HttpURLConnection connection = null;
        String username = "root";
        String password = "inubit";
        HttpURLConnection conn;

        try {
            connection = (HttpURLConnection) new URL(hosturl + "user/login").openConnection();
            String encoded = Base64.encode(username + ":" + password);
            connection.setRequestProperty("Authorization", "Basic " + encoded);

            connection.setRequestMethod("GET");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
