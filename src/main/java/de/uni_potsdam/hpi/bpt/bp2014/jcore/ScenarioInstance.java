package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.*;


import java.util.LinkedList;


/***********************************************************************************
*   
*   _________ _______  _        _______ _________ _        _______ 
*   \__    _/(  ____ \( (    /|(  ____ \\__   __/( (    /|(  ____ \
*      )  (  | (    \/|  \  ( || (    \/   ) (   |  \  ( || (    \/
*      |  |  | (__    |   \ | || |         | |   |   \ | || (__    
*      |  |  |  __)   | (\ \) || | ____    | |   | (\ \) ||  __)   
*      |  |  | (      | | \   || | \_  )   | |   | | \   || (      
*   |\_)  )  | (____/\| )  \  || (___) |___) (___| )  \  || (____/\
*   (____/   (_______/|/    )_)(_______)\_______/|/    )_)(_______/
*
*******************************************************************
*
*   Copyright © All Rights Reserved 2014 - 2015
*
*   Please be aware of the License. You may found it in the root directory.
*
************************************************************************************/



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
    public LinkedList<DataObjectInstance> dataObjectInstancesOnChange = new LinkedList<DataObjectInstance>();
    public int scenarioInstance_id;
    public int scenario_id;
    private String name;
    private DbScenarioInstance dbScenarioInstance = new DbScenarioInstance();
    private DbFragment dbFragment = new DbFragment();
    private DbDataObject dbDataObject = new DbDataObject();
    private DbTerminationCondition dbTerminationCondition = new DbTerminationCondition();

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
        if(dbScenarioInstance.getTerminated(this.scenarioInstance_id) == 0) {
            this.initializeFragments();
        }
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
            DataObjectInstance dataObjectInstance = new DataObjectInstance(dataObject, scenario_id, scenarioInstance_id, this);
            //checks if dataObjectInstance is locked
            if(dataObjectInstance.getOnChange()){
                dataObjectInstancesOnChange.add(dataObjectInstance);
            }else{
                dataObjectInstances.add(dataObjectInstance);
            }
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

    public Boolean setDataObjectInstanceToOnChange(int dataObject_id){
        DataObjectInstance dataObjectInstanceOnChange = null;
        for(DataObjectInstance dataObjectInstance: dataObjectInstances){
            if(dataObjectInstance.dataObject_id == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if(dataObjectInstanceOnChange != null){
            dataObjectInstances.remove(dataObjectInstanceOnChange);
            dataObjectInstancesOnChange.add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(true);
            return true;
        }
        return false;
    }

    public Boolean setDataObjectInstanceToNotOnChange(int dataObject_id){
        DataObjectInstance dataObjectInstanceOnChange = null;
        for(DataObjectInstance dataObjectInstance: dataObjectInstancesOnChange){
            if(dataObjectInstance.dataObject_id == dataObject_id) {
                dataObjectInstanceOnChange = dataObjectInstance;
                break;
            }
        }
        if(dataObjectInstanceOnChange != null){
            dataObjectInstancesOnChange.remove(dataObjectInstanceOnChange);
            dataObjectInstances.add(dataObjectInstanceOnChange);
            dataObjectInstanceOnChange.setOnChange(false);
            return true;
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

    public void beginEnabledReferenceControlNodeInstanceForControlNodeInstanceID(int controlNode_id){
        for(ControlNodeInstance controlNodeInstance: enabledControlNodeInstances){
            if(controlNodeInstance.controlNode_id == controlNode_id){
                if (controlNodeInstance.getClass() == ActivityInstance.class) ((ActivityInstance)controlNodeInstance).begin();


                //TODO: DEBUG ONLY:
                if (controlNodeInstance.getClass() == ActivityInstance.class) ((ActivityInstance)controlNodeInstance).terminate();
                //----------------------------------------------------------------------------------------------------------------


            }
        }
    }

    /**
     * check termination condition
     * get all termination condition and prove the condition for every condition set
     */
    public boolean checkTerminationCondition(){
        boolean terminated = false;
        //get the condition Set IDs
        LinkedList<Integer> conditionsSets = dbTerminationCondition.getConditionsSetIDsForScenario(scenario_id);
        for(int conditionSet: conditionsSets){
            LinkedList<Condition> conditions = dbTerminationCondition.getConditionsForConditionSetAndScenario(scenario_id, conditionSet);
            //prove every condition in condition set
            for(Condition condition: conditions){
                DataObjectInstance dataObjectInstance = null;
                for(DataObjectInstance currentDataObjectInstance: dataObjectInstances){
                    if(currentDataObjectInstance.dataObject_id == condition.getDataObject_id()){
                        dataObjectInstance = currentDataObjectInstance;
                    }
                }
                if(dataObjectInstance != null){
                    if(dataObjectInstance.state_id == condition.getState_id()){
                        terminated = true;
                    }else{
                        terminated = false;
                        break;
                    }
                }
            }
            //termination condition is true
            if(terminated) break;
        }
        //terminate the scenario
        if(terminated){
            System.out.println("terminiere Szenario");
            this.terminate();
        }
        return terminated;
    }

    private void terminate(){
        dbScenarioInstance.setTerminated(scenarioInstance_id, true);
        controlNodeInstances.clear();
        enabledControlNodeInstances.clear();
        controlFlowEnabledControlNodeInstances.clear();
        dataEnabledControlNodeInstances.clear();
        runningControlNodeInstances.clear();
        terminatedControlNodeInstances.clear();
    }
}
