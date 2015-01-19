package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;

public class ParallelGatewaySplitBehavior extends OutgoingBehavior {
    private DbDataFlow dbDataFlow = new DbDataFlow();

    ParallelGatewaySplitBehavior(){
        super();
    }
}
