package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.jcomparser.saving.AbstractControlNode;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.AbstractControlNodeInstance;
import de.uni_potsdam.hpi.bpt.bp2014.jcore.controlnodes.ActivityInstance;

import java.util.List;
import java.util.Optional;

/**
 * This class is a collection of methods which make the use of scenario instances
 * easier
 */
public class ScenarioUtil {

    /**
     *
     * @param scenarioInstance
     * @param activityInstanceId
     * @return
     */
    public ActivityInstance getActivityById(ScenarioInstance scenarioInstance, Integer activityInstanceId) {
        List<AbstractControlNodeInstance> nodes = scenarioInstance.getControlNodeInstances();
        Optional<AbstractControlNodeInstance> activityInstance = nodes.stream()
                .filter(x -> x instanceof ActivityInstance)
                .filter(x -> x.getControlNodeInstanceId() == activityInstanceId).findFirst();
        if (activityInstance.isPresent()) {
            return (ActivityInstance) activityInstance.get();
        }
        throw new IllegalArgumentException("Invalid activity instance activityInstanceId");
    }
}
