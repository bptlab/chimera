package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlFlow;
import de.uni_potsdam.hpi.bpt.bp2014.database.DbControlNode;

public class OutgoingBehavior {
    protected DbControlFlow dbControlFlow = new DbControlFlow();
    protected DbControlNode dbControlNode = new DbControlNode();
    protected ScenarioInstance scenarioInstance;
    protected int controlNode_id;
    protected int fragmentInstance_id;

    public void terminate(){}
}
