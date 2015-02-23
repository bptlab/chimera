package de.uni_potsdam.hpi.bpt.bp2014.util;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;

public class JsonUtil {
    /**
     * @param content contains a LinkedList
     * @return a wrapped json
     */
    public static String JsonWrapperLinkedList(LinkedList<Integer> content) {
        Gson gson = new Gson();
        JsonIntegerList json = new JsonIntegerList(content);
        return gson.toJson(json);
    }

    /**
     * @param content contains a LinkedList
     * @param labels  contains a String
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

    public static class JsonHashMapIntegerString {
        private LinkedList<Integer> ids;
        private HashMap<Integer, String> label;

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

    public static class JsonIntegerList {
        private LinkedList<Integer> ids;

        public JsonIntegerList(LinkedList<Integer> ids) {
            this.ids = ids;
        }
    }

    public static class JsonInteger {
        private Integer id;

        public JsonInteger(Integer id) {
            this.id = id;
        }
    }
    public static class JsonStringHashMap {
        private HashMap<String, String> ids;

        public JsonStringHashMap(HashMap<String, String> ids) {
            this.ids = ids;
        }
    }
}
