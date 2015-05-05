package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

public class AnalyticsService {
    private ExampleAlgorithm exampleAlgorithm = new ExampleAlgorithm();

	/**
	*   This method returns the actual result of a specific algorithm
	*/
    public boolean getAnalysisResultForInstance(int scenarioInstance_id) {
        //TODO: to be edited as a simple sql call to retrieve the results set in executeAnalysisResultForInstance 
        return exampleAlgorithm.exampleAlgorithm1();
    }

	/**
	*   This method executes the giving algorithm identified by its id and returns its success / failure
	*/
    public boolean executeAnalysisResultForInstance(int scenarioInstance_id) {
        //TODO: persistent the result in a separat data table
        return exampleAlgorithm.exampleAlgorithm1();
    }
}
