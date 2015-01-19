package de.uni_potsdam.hpi.bpt.bp2014.database;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static Connection instance = null;
    private static File file;
    private static String username;
    private static String password;
    private static String url;
    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    @Resource(name="jdbc/JEngineV2")
    private DataSource ds;

    private Connection(){
    }

    public static Connection getInstance(String path) {
        if (instance == null) {
            instance = new Connection();
            instance.initializeDatabaseConfiguration(path);
        }
        return instance;
    }

    public static Connection getInstance() {
        if (instance == null) {
            String path = "src/main/resources/database_connection";
            //instance = new Connection("C:/xampp/tomcat/webapps/JEngine/WEB-INF/classes/database_connection");
            instance = new Connection();
            instance.initializeDatabaseConfiguration(path);
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
        //System.err.println(file.getAbsoluteFile());
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
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initializeDatabaseConfiguration(String path) {
        try {
            username = "root";
            password = "foi6cixoo0Quah2e";
            url = "jdbc:mysql://localhost:3306/JEngineV2";
        } catch(Exception e) {
            System.err.println("You are running the Engine on Tomcat");
        }

    }
}
