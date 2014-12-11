package de.uni_potsdam.hpi.bpt.bp2014.jengine;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

/**
 * Created by jaspar.mang on 24.11.14.
 */
public class OutgoingBehavior {
    protected DbControlFlow dbControlFlow = new DbControlFlow();
    protected DbControlNode dbControlNode = new DbControlNode();
    protected ScenarioInstance scenarioInstance;
    protected int controlNode_id;
    protected int fragmentInstance_id;
}
