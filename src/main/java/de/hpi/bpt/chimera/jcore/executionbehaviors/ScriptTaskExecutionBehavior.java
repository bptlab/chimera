package de.hpi.bpt.chimera.jcore.executionbehaviors;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import de.hpi.bpt.chimera.database.controlnodes.DbScriptTask;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context.ChimeraContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

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

        // this line doesn't work with intellij idea debugger, because it will create new folder for tomcat
        // C:\Users\Florian\.IntelliJIdea2017.1\system\tomcat\Unnamed_chimera-case-engine_3 ...
        // TODO CHANGE
        //String path = System.getProperty("catalina.base") + "\\webapps\\Chimera-Resources\\" + dbScriptTask.getScriptTaskJar(controlNodeId);

        String path = "D:\\Programme\\SimpleScriptTask.jar";
        File file  = new File(path);

        URL url = null;
        try {
            url = file.toURL();
            URL[] urls = new URL[]{url};

            URLClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader());

            Class cls = Class.forName(dbScriptTask.getScriptTaskClassPath(controlNodeId), true, cl);
            Object obj = cls.newInstance();

            Method method = cls.getDeclaredMethod("execute", IChimeraContext.class);
            method.invoke(obj, new ChimeraContext(activityInstance));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
