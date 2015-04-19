package de.uni_potsdam.hpi.bpt.bp2014.rest;

import com.google.gson.Gson;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * This class provides the helper methods/classes used for the JSON representation for the REST connections.
 */
public class JsonUtil {

    // {"myArrayList":[{"map":{"id":1,"rolename":"marketing","description":"die, die viel reden","admin_id":1}}]}
    // {"myArrayList":[{"map":{"id":1,"username":"max","role_id":1,"description":""}},{"map":{"id":2,"username":"robert","role_id":1,"description":""}}]}
    //TODO: edit correct naming within json string
    public static String JsonWrapperArrayListHashMap(ArrayList<HashMap<String,Object>> content) {
        Gson gson = new Gson();
        JSONArray json = new JSONArray(content);
        return gson.toJson(json);
    }

    /**
     * @param content contains a LinkedList.
     * @return a wrapped json.
     */
    public static String JsonWrapperLinkedList(LinkedList<Integer> content) {
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(content);
        return gson.toJson(json);
    }

    /**
     * @param content contains a LinkedList.
     * @param labels  contains a String.
     * @return a wrapped json
     */
    public static String JsonWrapperHashMap(LinkedList<Integer> content, HashMap<Integer, String> labels) {
        Gson gson = new Gson();
        JsonHashMapIntegerString json = new JsonHashMapIntegerString(content, labels);
        return gson.toJson(json);
    }

    /**
     * @param content contains a LinkedList
     * @param labels  contains a String
     * @param states  contains a String
     * @return a wrapped json
     */
    public static String JsonWrapperMultipleHashMap(LinkedList<Integer> content, HashMap<Integer, String> labels, HashMap<Integer, String> states) {
        Gson gson = new Gson();
        JsonHashMapMultipleIntegerString json = new JsonHashMapMultipleIntegerString(content, labels, states);
        return gson.toJson(json);
    }
    /**
     * This class is a JSON HashMap containing a List of ID's and a HashMap consisting of the given ID's and labels as Strings.
     */
    public static class JsonHashMapIntegerString {
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        /**
         * constructor.
         *
         * @param ids This are the database ID's for the given Objects in the HashMap.
         * @param labels This is a HashMap of database ID's and the corresponding labels as a String.
         */
        public JsonHashMapIntegerString(LinkedList<Integer> ids, HashMap<Integer, String> labels) {
            this.ids = ids;
            this.label = labels;
        }
    }

    public static class JsonHashMapMultipleIntegerString {
        private HashMap<Integer, String> states;
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

        public JsonHashMapMultipleIntegerString(LinkedList<Integer> ids, HashMap<Integer, String> labels, HashMap<Integer, String> states) {
            this.ids = ids;
            this.label = labels;
            this.states = states;
        }
    }
    /**
     * This class handles the JSON representation of a list filled with database ID's.
     */
    public static class JsonIntegerList {
        private LinkedList<Integer> ids;

        /**
         * constructor.
         *
         * @param ids These are the database ID's of given objects.
         */
        public JsonIntegerList(LinkedList<Integer> ids) {
            this.ids = ids;
        }
    }

    /**
     * This class is the representation of an Integer as JSON.
     */
    public static class JsonInteger {
        private Integer id;

        /**
         * constructor.
         *
         * @param id Integer Value of a database ID.
         */
        public JsonInteger(Integer id) {
            this.id = id;
        }
    }

    /**
     * This class is the representation of a HashMap that maps a String to a String.
     */
    public static class JsonStringHashMap {
        private HashMap<String, String> ids;

        /**
         * constructor.
         *
         * @param ids This is a HashMap of names as a String and database ID's as a String.
         */
        public JsonStringHashMap(HashMap<String, String> ids) {
            this.ids = ids;
        }
    }

    /**
     *
     * @param jsonLine
     * @return parsed json string
     */
    public static Map parse(String jsonLine) {
        Map jsonJavaRootObject = new Gson().fromJson(jsonLine, Map.class);
        return jsonJavaRootObject;

    }
}
