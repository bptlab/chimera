package de.uni_potsdam.hpi.bpt.bp2014.database;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jaspar.mang on 26.11.14.
 */
public class Connection {
    private static Connection instance = null;
    private static File file;
    private static String username;
    private static String password;
    private static String url;
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    private Connection(String path){
        file = new File(path);
        username = this.getUsername();
        password = this.getPassword();
        url = this.getUrl();

    }

    public static Connection getInstance(String path) {
        if (instance == null) {
            instance = new Connection(path);
        }
        return instance;
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection("/Users/jaspar.mang/Repositories/rapid-prototype/JEngine-Java-V2/src/main/resources/database_connection");
        }
        return instance;
    }
    private static String getUsername(){
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);
        String username = "";
        try {
            username = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return username;
    }
    private static String getPassword(){
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);
        String password = "";
        try {
            br.readLine();
            password = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }
    private static String getUrl(){
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);
        String url = "";
        try {
            br.readLine();
            br.readLine();
            url = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public java.sql.Connection connect() {
        java.sql.Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //Open a connection
            conn = DriverManager.getConnection(url, password, username);
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }
}
