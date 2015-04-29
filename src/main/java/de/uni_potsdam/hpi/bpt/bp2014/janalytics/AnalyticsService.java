package de.uni_potsdam.hpi.bpt.bp2014.janalytics;

public class AnalyticsService {
    private ExampleAlgorithm exampleAlgorithm = new ExampleAlgorithm();

    public boolean getAnalysisResultForInstance(int scenarioInstance_id) {
        return exampleAlgorithm.exampleAlgorithm1();
    }
}
