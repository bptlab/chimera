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

    public ActivityInstance getActivityById(ScenarioInstance scenarioInstance, Integer id) {
        List<AbstractControlNodeInstance> nodes = scenarioInstance.getControlNodeInstances();
        Optional<AbstractControlNodeInstance> activityInstance = nodes.stream()
                .filter(x -> x instanceof ActivityInstance)
                .filter(x -> x.getControlNodeId() == id).findFirst();
        if (activityInstance.isPresent()) {
            return (ActivityInstance) activityInstance.get();
        }
        throw new IllegalArgumentException("Invalid activity instance id");
    }

}
