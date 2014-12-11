package de.uni_potsdam.hpi.bpt.bp2014.jengine;

import com.sun.org.apache.xpath.internal.operations.Bool;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragment;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;

import java.util.LinkedList;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class ScenarioInstance {
    public LinkedList<ControlNodeInstance> controlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> enabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> controlFlowEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> dataEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> runningControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> terminatedControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<FragmentInstance>();
    private LinkedList<DataObject> dataObjects = new LinkedList<DataObject>();
    private int scenarioInstance_id;
    private int scenario_id;
    private String name;
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbFragment dbFragment = new DbFragment();

    public ScenarioInstance(int scenario_id){
        this.scenario_id = scenario_id;
        //TODO: more then one Scenario Instance
        if (dbScenarioInstance.existScenario(scenario_id)){
            scenarioInstance_id = dbScenarioInstance.getScenarioInstanceID(scenario_id);
            System.out.println("exist");
            this.initializeFragments();
        } else {
            System.out.println("exist not");
            dbScenarioInstance.createNewScenarioInstance(scenario_id);
            scenarioInstance_id = dbScenarioInstance.getScenarioInstanceID(scenario_id);
            this.initializeFragments();
        }
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
}
