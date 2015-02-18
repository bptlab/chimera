package de.uni_potsdam.hpi.bpt.bp2014.config;

/**
 * ********************************************************************************
 *
 * _________ _______  _        _______ _________ _        _______
 * \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
 * )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
 * |  |  | (__    |   \ | || |         | |   |   \ | || (__
 * |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)
 * |  |  | (      | | \   || | \_  )   | |   | | \   || (
 * |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
 * (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
 *
 * ******************************************************************
 *
 * Copyright © All Rights Reserved 2014 - 2015
 *
 * Please be aware of the License. You may found it in the root directory.
 *
 * **********************************************************************************
 */


public class Config {

    // Processeditor URL
    public static String processeditorServerUrl = "http://172.16.64.113:1205/";
    public static String processeditorServerName = "root";
    public static String processeditorServerPassword = "inubit";

    // Comparser URL
    public static String jcomparserServerUrl = "http://172.16.64.113:8080/";

    // JCore URL
    public static String jcoreServerUrl = "http://172.16.64.113:8080/";

    // FrontEnd URL
    public static String frontendServerUrl = "http://172.16.64.113/Frontend/";

    // FrontEnd Admin URL
    public static String frontendAdminServerUrl = "http://172.16.64.113/Frontend/admin/";

    //Database Connection via web.xml
    //public static String databaseURL = "jdbc:mysql://127.0.0.1/JEngineV2";
    //public static String databaseUser = "root";
    //public static String databasePassword = "";
}
