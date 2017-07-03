package bpt.chimera.scripttasktest;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import bpt.chimera.scripttasklibrary.IChimeraDelegate;

/**
 * Created by marc on 23.06.17.
 */
public class SimpleScriptTask implements IChimeraDelegate {
    @Override
    public void execute(IChimeraContext iChimeraContext) {
        iChimeraContext.log("Hello World");

        int sum = 0;

        sum += Integer.parseInt(iChimeraContext.getParam("Test", "b").toString());
        sum += Integer.parseInt(iChimeraContext.getParam("Test", "a").toString());

        iChimeraContext.setParam("Test", "c", String.format("%d", sum));

        iChimeraContext.log(String.format("Result (local): %s", sum));
        iChimeraContext.log(String.format("Result (remote): %s", iChimeraContext.getParam("Test", "c").toString()));
    }
}
