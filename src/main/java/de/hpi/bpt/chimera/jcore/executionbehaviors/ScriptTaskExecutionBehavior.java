package de.hpi.bpt.chimera.jcore.executionbehaviors;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import bpt.chimera.scripttasklibrary.IChimeraDelegate;
import de.hpi.bpt.chimera.database.controlnodes.DbScriptTask;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context.ChimeraContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Handles the script task execution behavior.
 */
public class ScriptTaskExecutionBehavior extends ActivityExecutionBehavior {

    private static Logger log = Logger.getLogger(ScriptTaskExecutionBehavior.class);

    /**
     * Database ConnectionWrapper class for script tasks.
     */
    private DbScriptTask dbScriptTask = new DbScriptTask();

    /**
     * script task specific attributes
     */
    private final int controlNodeId;
    private String scripttaskjar;
    private String scripttaskclasspath;

    public ScriptTaskExecutionBehavior(ActivityInstance activityInstance) {
        super(activityInstance);
        controlNodeId = activityInstance.getControlNodeId();
    }

    /**
     * The handling method for script tasks execution behavior
     */
    @Override
    public void execute() {
        this.setValues();

        // get file object of jar file
        File file;
        try {
            file = new File(scripttaskjar);
        } catch (NullPointerException e) {
            log.error("The path to the jar file is not allowed to be 'null'!");
            return;
        }

        URL url;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            log.error("Could not get url from jar file, because it's malformed!");
            return;
        }

        // build class loader of jar file
        URL[] urls = new URL[]{url};
        URLClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader());

        // get class and create new instance
        Class delegateClass;
        Object delegateInstance;
        try {
            delegateClass = Class.forName(scripttaskclasspath, true, cl);
            delegateInstance = delegateClass.newInstance();
        } catch (ClassNotFoundException e) {
            log.error("Could not find class in jar! (" + scripttaskclasspath + ")");
            return;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("An error has occurred by creating an instance of the java class! (" + e.getClass().getSimpleName() + ")");
            return;
        }

        // check if class implements the 'IChimeraDelegate' interface
        if(delegateInstance instanceof IChimeraDelegate) {
            // get method object of 'execute' function
            Method method;
            try {
                method = delegateClass.getDeclaredMethod("execute", IChimeraContext.class);
            } catch (NoSuchMethodException e) {
                log.error("This class doesn't contains the neccessary 'execute' method!");
                return;
            }

            // invoke method on delegate instance
            try {
                method.invoke(delegateInstance, new ChimeraContext(activityInstance));
            } catch (InvocationTargetException e) {
                // TODO maybe we can add the name of the exception behind the text in the brackets
                log.error("The class has thrown an error! ()");
            } catch (IllegalAccessException e) {
                log.error("The 'execute' method cannot be accessed!");
            }
        } else {
            log.error("The class is not implementing from 'IChimeraDelegate'!");
        }
    }

    /**
     * Set the atrributes for the script task by reading from
     * the database.
     */
    private void setValues() {
        //scripttaskjar = dbScriptTask.getScriptTaskJar(controlNodeId);
        scripttaskjar = "D:\\Programme\\SimpleScriptTask.jar";
        scripttaskclasspath = dbScriptTask.getScriptTaskClassPath(controlNodeId);
    }

}
