package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbObject;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jaspar.mang on 11.05.15.
 */
public class ServiceManager {
    HashMap<String, AnalyticsService> services = new HashMap<>();
    DbObject dbObject = new DbObject();

    public ServiceManager(){
        registerServices();
    }

    public Set<String> getServices(){
        return services.keySet();
    }

    public void calculateResultForService(String service, String[] args){
        JSONObject jsonObject = services.get(service).calculateResult(args);
        dbObject.executeUpdateStatement("UPDATE janalyticsresults SET json = '" + jsonObject.toString() + "' WHERE service = '" + services.get(service).getClass().getName() +"'");
    }

    public boolean existService(String service){
        return services.containsKey(service);
    }

    public JSONObject getResultForService(String service){
        String json = dbObject.executeStatementReturnsString("SELECT json FROM janalyticsresults WHERE service = '" + services.get(service).getClass().getName() +"'", "json");
        return new JSONObject(json);
    }

    private void addService(AnalyticsService service){
        String serviceName = service.getClass().getName();
        services.put(serviceName, service);
        if(!dbObject.executeExistStatement("SELECT * FROM janalyticsresults WHERE service = '"+ serviceName +"'")){
            dbObject.executeInsertStatement("INSERT INTO janalyticsresults (service, json) VALUES ('"+ serviceName +"', '{}')");
        }
    }

    private void registerServices(){
        addService(new ExampleAlgorithm());
    }
}
