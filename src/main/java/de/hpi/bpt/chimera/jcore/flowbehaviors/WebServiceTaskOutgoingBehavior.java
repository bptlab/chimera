package de.hpi.bpt.chimera.jcore.flowbehaviors;

import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;

public class WebServiceTaskOutgoingBehavior extends AbstractOutgoingBehavior {

  private ActivityInstance wsTask;
    
  
  @Override
  public void terminate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void skip() {
    // Do nothing?

  }
  
  /**
   * Stores the result of the webservice call into a data object.
   */
  private void writeDataObjects() {
    
    // get output set
    wsTask.getOutputSets();
    // check that output set is unique
    
    // create data objects if necessary
    
    // get jsonpath expressions for attributes
    
  }

}
