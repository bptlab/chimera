package de.hpi.bpt.chimera.jcore.executionbehaviors;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import bpt.chimera.scripttasklibrary.IChimeraDelegate;
import de.hpi.bpt.chimera.database.DbSelectedDataObjects;
import de.hpi.bpt.chimera.database.controlnodes.DbScriptTask;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context.ChimeraContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ScriptTaskExecutionBehavior extends ActivityExecutionBehavior {

    private static Logger log = Logger.getLogger(ScriptTaskExecutionBehavior.class);
    private final int controlNodeId;

    private DbScriptTask dbScriptTask = new DbScriptTask();

    public ScriptTaskExecutionBehavior(ActivityInstance instance) {
        super(instance);
        controlNodeId = activityInstance.getControlNodeId();
    }

    @Override
    public void execute() {
        // TODO run java code file
        log.info("Affenschaukel - 0.25");
        String path = System.getProperty("catalina.base") + "\\webapps\\Chimera-Resources\\" + dbScriptTask.getScriptTaskJar(controlNodeId);


        log.info("Affenschaukel - 0.5");

        File file  = new File(path);

        log.info("Affenschaukel - 1");

        URL url = null;
        try {
            url = file.toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);
            Class cls = cl.loadClass(dbScriptTask.getScriptTaskClassPath(controlNodeId));

            log.info("Affenschaukel - 2");

            Object objClass = cls.newInstance();
            log.info("Affenschaukel - 2.5");

            /*Method method = cls.getMethod("test");
            method.invoke(objClass);
            log.info("Affenschaukel - 8");*/

            /*IChimeraDelegate chimeraDelegate = (IChimeraDelegate) objClass;
            chimeraDelegate.execute(new ChimeraContext(activityInstance));
            log.info("Affenschaukel - 8");*/

            /*if(objClass instanceof IChimeraDelegate) {
                log.info("Affenschaukel - 3");
                IChimeraDelegate chimeraDelegate = (IChimeraDelegate) objClass;
                chimeraDelegate.execute(new ChimeraContext(activityInstance)); // TODO insert context here
            }*/
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        DbSelectedDataObjects db = new DbSelectedDataObjects();
        List<Integer> ids = db.getDataObjectSelection(getScenarioInstance().getId(), controlNodeId);

    }

}
