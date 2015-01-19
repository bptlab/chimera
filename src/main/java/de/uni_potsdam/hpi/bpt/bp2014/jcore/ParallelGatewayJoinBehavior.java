package de.uni_potsdam.hpi.bpt.bp2014.jcore;

import de.uni_potsdam.hpi.bpt.bp2014.database.DbDataFlow;

public class ParallelGatewayJoinBehavior extends IncomingBehavior {
    private DbDataFlow dbDataFlow = new DbDataFlow();

    ParallelGatewayJoinBehavior(GatewayInstance gatewayInstance){
        this.controlNodeInstance = gatewayInstance;
    }
}
