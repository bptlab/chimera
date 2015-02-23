package de.uni_potsdam.hpi.bpt.bp2014.database;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ********************************************************************************
 * <p/>
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 * <p/>
 * ******************************************************************
 * <p/>
 * Copyright © All Rights Reserved 2014 - 2015
 * <p/>
 * Please be aware of the License. You may found it in the root directory.
 * <p/>
 * **********************************************************************************
 */

/**
 * This class is used to build a connection to the database.
 * It initializes the JDBC Driver with the needed configuration to be able to connect ot the database.
 */
public class Connection {
    private static Connection instance = null;
    private static File file;
    private static String username;
    private static String password;
    private static String url;
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    /**
     * constructor
     */
    private Connection() {
    }

    /**
     * This method is used to build a connection with a given path for the database.
     *
     * @param path This is the path for the database.
     * @return the instance of a new Connection.
     */
    public static Connection getInstance(String path) {
        if (instance == null) {
            instance = new Connection();
            instance.initializeDatabaseConfiguration(path);
        }
        return instance;
    }

    /**
     * This method builds a connection for the database with a default path.
     *
     * @return the instance of a new Connection.
     */
    public static Connection getInstance() {
        if (instance == null) {
            String path = "src/main/resources/database_connection";
            instance = new Connection();
            instance.initializeDatabaseConfiguration(path);
        }
        return instance;
    }

    /**
     * This connection is used to connect to the database.
     *
     * @return the open connection.
     */
    public java.sql.Connection connect() {
        java.sql.Connection conn = null;
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * This method loads all the configurations needed to connect to the database.
     *
     * @param path This is the path for the database.
     */
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
                username = (String) ctx.lookup("java:comp/env/username");
                password = (String) ctx.lookup("java:comp/env/password");
                url = (String) ctx.lookup("java:comp/env/url");
            } catch (NamingException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}