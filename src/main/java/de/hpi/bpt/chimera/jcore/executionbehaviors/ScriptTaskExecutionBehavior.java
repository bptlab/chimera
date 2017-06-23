package de.hpi.bpt.chimera.jcore.executionbehaviors;

import bpt.chimera.scripttasklibrary.IChimeraContext;
import bpt.chimera.scripttasklibrary.IChimeraDelegate;
import de.hpi.bpt.chimera.database.controlnodes.DbScriptTask;
import de.hpi.bpt.chimera.jcore.controlnodes.ActivityInstance;
import de.hpi.bpt.chimera.jcore.executionbehaviors.scripttasks.context.ChimeraContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
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
        // TODO run java code file
        log.info("Affenschaukel - 0.25");

        String path = System.getProperty("catalina.base") + "\\webapps\\Chimera-Resources\\" + dbScriptTask.getScriptTaskJar(controlNodeId);
        //String path = "D:\\Programme\\SimpleScriptTask.jar";

        File file  = new File(path);

        URL url = null;
        try {
            url = file.toURL();
            URL[] urls = new URL[]{url};

            ClassLoader cl = new URLClassLoader(urls);

            Class cls = cl.loadClass("bpt.chimera.scripttasktest.SimpleScriptTask");
            //log.info("AFFE123 - " + cls.getName());

            //Method method = cls.getMethod("test");
            //log.info("AFFE1234" + method.getName());

            // ------------------------------------------

            //no paramater
            //Class noparams[] = {};

            //String parameter
            //Class[] paramString = new Class[1];
            //paramString[0] = String.class;

            //int parameter
            //Class[] paramInt = new Class[1];d
            //paramInt[0] = Integer.TYPE;

            try{
                //load the AppTest at runtime
                //Class cls = cl.loadClass("bpt.chimera.scripttasktest.SimpleScriptTask");
                //Class cls = Class.forName("bpt.chimera.scripttasktest.SimpleScriptTask", true, cl);
                //Object obj = cls.newInstance();
                //log.info("AFFE123 - " + cls.getName() + " -- " + obj.toString());

                //call the printIt method
                //Method method = cls.getDeclaredMethod("test", noparams);
                //method.invoke(obj, null);
                //for(Method method : obj.getClass().getDeclaredMethods()) {
                //    log.info("AFFE1234");
                //}
                //log.info("AFFE1234 - " + obj.getClass().getDeclaredMethods().length);

                //call the printItString method, pass a String param
                /*method = cls.getDeclaredMethod("printItString", paramString);
                method.invoke(obj, new String("mkyong"));

                //call the printItInt method, pass a int param
                method = cls.getDeclaredMethod("printItInt", paramInt);
                method.invoke(obj, 123);

                //call the setCounter method, pass a int param
                method = cls.getDeclaredMethod("setCounter", paramInt);
                method.invoke(obj, 999);

                //call the printCounter method
                method = cls.getDeclaredMethod("printCounter", noparams);
                method.invoke(obj, null);*/

            } catch(Exception ex){
                log.error(ex.getMessage());
            }

            Object objClass = cls.newInstance();
            // Method method = cls.getMethod("test");
            // method.invoke(objClass);
            //log.info("Affenschaukel - 2");

            /*Object objClass = cls.newInstance();
            log.info("Affenschaukel - 2.5");*/

            /*Method method = cls.getMethod("test");
            method.invoke(objClass);
            log.info("Affenschaukel - 8");*/

            /*IChimeraDelegate chimeraDelegate = (IChimeraDelegate) objClass;
            chimeraDelegate.execute(new ChimeraContext(activityInstance));
            log.info("Affenschaukel - 8");*/

            if(objClass instanceof IChimeraDelegate) {
                log.info("Affenschaukel - 3");
                IChimeraDelegate chimeraDelegate = (IChimeraDelegate) objClass;
                chimeraDelegate.execute(new ChimeraContext(activityInstance)); // TODO insert context here
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        /*DbSelectedDataObjects db = new DbSelectedDataObjects();
        List<Integer> ids = db.getDataObjectSelection(getScenarioInstance().getId(), controlNodeId);*/

    }

}
