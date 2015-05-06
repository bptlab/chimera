package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AnalyticsService {
    private ExampleAlgorithm exampleAlgorithm = new ExampleAlgorithm();

	/**
	*   This method returns the actual result of a specific algorithm
	*/
    public Object getAnalysisResultForInstance(int scenarioInstance_id, String algorithmID) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String algorithmName = "exampleAlgorithm" + algorithmID;
        Method method = ExampleAlgorithm.class.getDeclaredMethod(algorithmName, Integer.TYPE);

        return method.invoke(exampleAlgorithm, scenarioInstance_id);
    }

	/**
	*   This method executes the giving algorithm identified by its id and returns its success / failure
	*/
    public Object executeAnalysisResultForInstance(int scenarioInstance_id, String algorithmID) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String algorithmName = "exampleAlgorithm" + algorithmID;
        Method method = ExampleAlgorithm.class.getDeclaredMethod(algorithmName, Integer.TYPE);

        return method.invoke(exampleAlgorithm, scenarioInstance_id);
    }
}
