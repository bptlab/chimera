package de.uni_potsdam.hpi.bpt.bp2014.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
    private static Connection instance = null;
    private static File file;
    private static String username;
    private static String password;
    private static String url;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

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
            instance = new Connection();
            instance.initializeDatabaseConfiguration(path);
        }
        return instance;
    }


    public java.sql.Connection connect() {
        java.sql.Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }

    private void initializeDatabaseConfiguration(String path) {
        file = new File(path);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            username = br.readLine();
            password = br.readLine();
            url = br.readLine();
        } catch (FileNotFoundException e) {
            try {
                Context ctx = new InitialContext();
                username = (String)ctx.lookup("java:comp/env/username");
                password = (String)ctx.lookup("java:comp/env/password");
                url = (String)ctx.lookup("java:comp/env/url");
            } catch (NamingException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}