package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataObject;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbFragment;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbScenarioInstance;

import java.util.LinkedList;
/*
represents a scenario instance
the constructor looks for an scenario instance in the database or create a new one in the database
the constructor also initialize the fragment instances and data object instances
the scenario instance has Lists for all enabled, control flow enabled, data flow enabled, running and terminated activity
instances, fragment instances and all data object instances
the scenario instance provide methods for the administration of the data object instances
 */
public class ScenarioInstance {
    public LinkedList<ControlNodeInstance> controlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> enabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> controlFlowEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> dataEnabledControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> runningControlNodeInstances = new LinkedList<ControlNodeInstance>();
    public LinkedList<ControlNodeInstance> terminatedControlNodeInstances = new LinkedList<ControlNodeInstance>();
    private LinkedList<FragmentInstance> fragmentInstances = new LinkedList<FragmentInstance>();
    public LinkedList<DataObjectInstance> dataObjectInstances = new LinkedList<DataObjectInstance>();
    public int scenarioInstance_id;
    public int scenario_id;
    private String name;
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbFragment dbFragment = new DbFragment();
    private DbDataObject dbDataObject = new DbDataObject();

    public ScenarioInstance(int scenario_id, int scenarioInstance_id){
        this.scenario_id = scenario_id;
        if (dbScenarioInstance.existScenario(scenario_id, scenarioInstance_id)){
            //creates an existing Scenario Instance using the database information
            this.scenarioInstance_id = scenarioInstance_id;
        } else {
            //creates a new Scenario Instance also in database, using autoincrement to getting the scenario instances id
            this.scenarioInstance_id = dbScenarioInstance.createNewScenarioInstance(scenario_id);
        }
        this.initializeDataObjects();
        this.initializeFragments();
    }

    //starts a new scenario instance
    public ScenarioInstance(int scenario_id){
        this.scenario_id = scenario_id;
        this.scenarioInstance_id = dbScenarioInstance.createNewScenarioInstance(scenario_id);
        this.initializeDataObjects();
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

        //removes the old control node instances
        LinkedList<ControlNodeInstance> updatedList = new LinkedList<ControlNodeInstance>(terminatedControlNodeInstances);
        for(ControlNodeInstance controlNodeInstance: updatedList){
            if(controlNodeInstance.fragmentInstance_id == fragmentInstance_id){
                terminatedControlNodeInstances.remove(controlNodeInstance);
            }
        }
        updatedList = new LinkedList<ControlNodeInstance>(controlNodeInstances);
        for(ControlNodeInstance controlNodeInstance: updatedList){
            if(controlNodeInstance.fragmentInstance_id == fragmentInstance_id){
                controlNodeInstances.remove(controlNodeInstance);
            }
        }
        initializeFragment(fragmentInstance.fragment_id);
    }

    public void initializeDataObjects(){
        LinkedList<Integer> data = dbDataObject.getDataObjectsForScenario(scenario_id);
        for(Integer dataObject: data){
            dataObjectInstances.add(new DataObjectInstance(dataObject, scenario_id, scenarioInstance_id));
        }
    }

    public Boolean checkDataObjectState(int dataObject_id, int state_id){
        for(DataObjectInstance dataObjectInstance: dataObjectInstances){
            if(dataObjectInstance.dataObject_id == dataObject_id && dataObjectInstance.state_id == state_id) return true;
        }
        return false;
    }
    public Boolean changeDataObjectInstanceState(int dataObject_id, int state_id){
        for(DataObjectInstance dataObjectInstance: dataObjectInstances){
            if(dataObjectInstance.dataObject_id == dataObject_id) {
                dataObjectInstance.setState(state_id);
                return true;
            }
        }
        return false;
    }

    public void checkDataFlowEnabled(){
        for (ControlNodeInstance activityInstance: controlFlowEnabledControlNodeInstances){
            if (activityInstance.getClass() == ActivityInstance.class){
                ((ActivityInstance) activityInstance).checkDataFlowEnabled();
            }
        }
    }

    public Boolean terminatedControlNodeInstancesContainControlNodeID(int controlNode_id){
        for(ControlNodeInstance controlNodeInstance: terminatedControlNodeInstances){
            if(controlNodeInstance.controlNode_id == controlNode_id) return true;
        }
        return false;
    }
}
