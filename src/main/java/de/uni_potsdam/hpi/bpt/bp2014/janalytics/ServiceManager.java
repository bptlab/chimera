package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class ServiceManager {
    HashMap<String, AnalyticsService> services = new HashMap<>();
    DbObject dbObject = new DbObject();

    /**
     * Initialize the services manager and register the services.
     */
    public ServiceManager() {
        registerServices();
    }

    /**
     * Gives all registered services.
     *
     * @return a Set with all registered services.
     */
    public Set<String> getServices() {
        return services.keySet();
    }

    /**
     * Runs the calculation algorithm of the give service and saves the result as json in the database.
     *
     * @param service the service.
     * @param args    arguments for the service.
     */
    public void calculateResultForService(String service, String[] args) {
        JSONObject jsonObject = services.get(service).calculateResult(args);
        dbObject.executeUpdateStatement("UPDATE janalyticsresults SET json = '" + jsonObject.toString() + "' WHERE service = '" + services.get(service).getClass().getName() + "'");
    }

    /**
     * Checks if a service is registered to the service manager.
     *
     * @param service a String with the service name.
     * @return true if the service is registered. False if not.
     */
    public boolean existService(String service) {
        return services.containsKey(service);
    }

    /**
     * Gives the Result for a service. Returns it as json. The json could be empty if the service never calculated a result.
     *
     * @param service the service.
     * @return a json with the result of the service.
     */
    public JSONObject getResultForService(String service) {
        String json = dbObject.executeStatementReturnsString("SELECT json FROM janalyticsresults WHERE service = '" + services.get(service).getClass().getName() + "'", "json");
        return new JSONObject(json);
    }

    /**
     * Adds a Service to the service manager.
     *
     * @param service An AnalyticsService.
     */
    private void addService(AnalyticsService service) {
        String serviceName = service.getClass().getName();
        services.put(serviceName, service);
        if (!dbObject.executeExistStatement("SELECT * FROM janalyticsresults WHERE service = '" + serviceName + "'")) {
            dbObject.executeInsertStatement("INSERT INTO janalyticsresults (service, json) VALUES ('" + serviceName + "', '{}')");
        }
    }

    /**
     * Register new Services with addService(new classNameOfAlgorithm).
     */
    private void registerServices() {
        addService(new ExampleAlgorithm());
        // add further Algorithms here
    }
}
