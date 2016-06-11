package de.uni_potsdam.hpi.bpt.bp2014.jcore.flowbehaviors;

import de.uni_potsdam.hpi.bpt.bp2014.database.controlnodes.events.DbEventMapping;

import java.util.List;

/**
 *
 */
public class EventGatewayOutgoingBehavior extends AbstractOutgoingBehavior {
    private final int fragmentInstanceId;
    List<Integer> followingEventControlNodes;

    public EventGatewayOutgoingBehavior(List<Integer> followingEventControlNodes,
                                        int fragmentInstanceId) {
        this.followingEventControlNodes = followingEventControlNodes;
        this.fragmentInstanceId = fragmentInstanceId;
    }

    @Override
    public void terminate() {
        // This will also try to delete the event which
        DbEventMapping eventMapping = new DbEventMapping();
        for (Integer eventControlNode : this.followingEventControlNodes) {
            eventMapping.removeEventMapping(this.fragmentInstanceId, eventControlNode);
        }
    }
}
