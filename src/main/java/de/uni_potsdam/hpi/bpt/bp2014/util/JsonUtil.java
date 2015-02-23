package de.uni_potsdam.hpi.bpt.bp2014.util;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * This class provides the helper methods/classes used for the JSON representation for the REST connections.
 */
public class JsonUtil {
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
}
