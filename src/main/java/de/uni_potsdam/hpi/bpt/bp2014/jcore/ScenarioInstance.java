package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragment;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;

import java.util.LinkedList;

public class ScenarioInstance {
    public LinkedList<ControlNodeInstance> controlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> enabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> controlFlowEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> dataEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> runningControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> terminatedControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<FragmentInstance>();
    public LinkedList<DataObjectInstance> dataObjectInstances = new LinkedList<DataObjectInstance>();
    private int scenarioInstance_id;
    private int scenario_id;
    private String name;
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbFragment dbFragment = new DbFragment();
    private DbDataObject dbDataObject = new DbDataObject();

    public ScenarioInstance(int scenario_id, int scenarioInstance_id){
        this.scenario_id = scenario_id;
        if (dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id)){
            this.scenarioInstance_id = scenarioInstance_id;
            System.out.println("Scenario exist");
            this.initializeDataObjects();
            this.initializeFragments();
        } else {
            System.out.println("Scenario exist not");
            dbScenarioInstance.createNewScenarioInstance(scenario_id);
            this.scenarioInstance_id = dbScenarioInstance.getScenarioInstanceID(scenario_id);
            this.initializeDataObjects();
            this.initializeFragments();
        }
    }

    public ScenarioInstance(int scenario_id){
        this.scenario_id = scenario_id;
        dbScenarioInstance.createNewScenarioInstance(scenario_id);
        this.scenarioInstance_id = dbScenarioInstance.getScenarioInstanceID(scenario_id);
        this.initializeFragments();
    }

    private void initializeFragments(){
        LinkedList<Integer> fragment_ids = dbFragment.getFragmentsForScenario(scenario_id);
        for(int fragment_id: fragment_ids){
            this.initializeFragment(fragment_id);
        }
    }

    public void initializeFragment(int fragment_id){
        FragmentInstance fragmentInstance = new FragmentInstance(fragment_id, scenarioInstance_id, this);
        fragmentInstances.add(fragmentInstance);
    }

    public void restartFragment(int fragmentInstance_id){
        FragmentInstance fragmentInstance = null;
        for(FragmentInstance f : fragmentInstances){
            if(f.fragmentInstance_id == fragmentInstance_id) fragmentInstance = f;
        }
        fragmentInstances.remove(fragmentInstance);
        fragmentInstance.terminate();
        initializeFragment(fragmentInstance.fragment_id);
    }

    public void initializeDataObjects(){
        LinkedList<Integer> data = dbDataObject.getDataObjectsForScenario(scenario_id);
        for(Integer dataObject: data){
            dataObjectInstances.add(new DataObjectInstance(dataObject, scenario_id, scenarioInstance_id));
        }
    }
}
