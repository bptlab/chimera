package de.uni_potsdam.hpi.bpt.bp2014.jcomparser;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

/*
As a part of the JComparser we need to retrieve XML docus from a source URL like the Repo from the Processeditor.
 */
public class Retrieval {
    private final String loginRequest = "<user>%n" +
            "<property name='name' value='%s'/>%n" +
            "<property name='pwd' value='%s'/>%n" +
            "</user>";

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
            conn.setRequestMethod("POST");
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
            CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );

            Base64 base64 = new Base64();
            connection = (HttpURLConnection) new URL(hosturl + "users/login").openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");
            connection.setInstanceFollowRedirects(false);
            OutputStream os = connection.getOutputStream();
            PrintWriter osw = new PrintWriter(os);
            osw.println(String.format(loginRequest, username, password));
            osw.flush();
            osw.close();
            connection.getResponseCode();
            connection.getResponseMessage();
            HttpURLConnection modelsConnection = (HttpURLConnection) new URL(urlToRead).openConnection();
            modelsConnection.setInstanceFollowRedirects(false);
            modelsConnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(modelsConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line + "\n");
            }
            modelsConnection.disconnect();
            connection.disconnect();
            return stringBuilder.toString();

        } catch (IOException e) {
            System.err.println("Request failed.");
            e.printStackTrace();
        }
        return null;
    }
}
