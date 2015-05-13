package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;
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
 *
 */
public class ServiceManager {
    final HashMap<String, AnalyticsService> services = new HashMap<>();
    DbObject dbObject = new DbObject();

    /**
     *
     */
    public ServiceManager() {
        registerServices();
    }

    /**
     * @return
     */
    public Set<String> getServices() {
        return services.keySet();
    }

    /**
     * @param service
     * @param args
     */
    public void calculateResultForService(String service, String[] args) {
        JSONObject jsonObject = services.get(service).calculateResult(args);
        dbObject.executeUpdateStatement("UPDATE janalyticsresults SET json = '" + jsonObject.toString() + "' WHERE service = '" + services.get(service).getClass().getName() + "'");
    }

    /**
     * @param service
     * @return
     */
    public boolean existService(String service) {
        return services.containsKey(service);
    }

    /**
     * @param service
     * @return
     */
    public JSONObject getResultForService(String service) {
        String json = dbObject.executeStatementReturnsString("SELECT json FROM janalyticsresults WHERE service = '" + services.get(service).getClass().getName() + "'", "json");
        return new JSONObject(json);
    }

    /**
     * @param service
     */
    private void addService(AnalyticsService service) {
        String serviceName = service.getClass().getName();
        services.put(serviceName, service);
        if (!dbObject.executeExistStatement("SELECT * FROM janalyticsresults WHERE service = '" + serviceName + "'")) {
            dbObject.executeInsertStatement("INSERT INTO janalyticsresults (service, json) VALUES ('" + serviceName + "', '{}')");
        }
    }

    /**
     *
     */
    private void registerServices() {
        addService(new ExampleAlgorithm());
    }
}
