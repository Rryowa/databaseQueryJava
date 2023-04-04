package com.digdes.school;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Comparative{

    List<Map<String,Object>> data;
    
    public Comparative(List<Map<String,Object>> data) {
        this.data = data;
    }

    public ArrayList<Integer> compareAnd(String request) {
        ArrayList<ArrayList<Integer>> listOflistOfAndIndexes = new ArrayList<ArrayList<Integer>>();

        //age>30 and age<80 and age>25
        for (String token : request.split("and")){
            listOflistOfAndIndexes.add(compare(token));
        }
        //listOfAndIndexes = {{2}, {0, 1, 2}, {1, 2}}

        //Intersection ArrayList's of indexes of matching rows;
        //{2}
        ArrayList<Integer> current = listOflistOfAndIndexes.get(0);
        
        //retain all intersection indexes(rows from data)
        for (int i = 1; i < listOflistOfAndIndexes.size(); i++) {
            //{0, 1, 2}
            ArrayList<Integer> next = listOflistOfAndIndexes.get(i);
            //{2}
            current.retainAll(next);
        }
        //{2}
        return current;
    }

    //requst = "age>25"
    public ArrayList<Integer> compare(String request) {
        //rows that satisfy request
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        //if request satisfied - add to list
        for (int i = 0; i < data.size(); i++) {

            Map<String, Object> map = data.get(i);
            
            if (request.contains(">=")) {
                if (!(smaller(request.split(">="), map))){
                    indexes.add(i);
                }
            }
            
            else if (request.contains("<=")) {
                if (!(greater(request.split("<="), map))){
                    indexes.add(i);
                }
            }

            else if (request.contains(">")) {
                if (greater(request.split(">"), map)){
                    indexes.add(i);
                }
            }

            else if (request.contains("<")) {
                if (smaller(request.split("<"), map)){
                    indexes.add(i);
                }
            }

            else if (request.contains("=")) {
                if (equal(request.split("="), map)){
                    indexes.add(i);
                }
            }
            
            else if (request.contains("!=")) {
                if (!(equal(request.split("!="), map))){
                    indexes.add(i);
                }
            }
        }
        return indexes;
    }

    public boolean equal(String[] token, Map<String, Object> map) {
        String name = token[0];
        String value = token[1];
        
        if (map.get(name).equals(value)) {
            return true;
        }
        //TODO: return ERROR
        return false;
    }

    public boolean greater(String[] token, Map<String, Object> map) {
        String name = token[0];
        String value = token[1];
        
        var mapVal = map.get(name);
        if (mapVal instanceof Long){
            Long mapValue = Long.parseLong(mapVal.toString());
            Long tokenValue = Long.parseLong(value);
            if (mapValue > tokenValue) {
                return true;
            }
        }
        else if (mapVal instanceof Double){
            Double mapValue = Double.parseDouble(mapVal.toString());
            Double tokenValue = Double.parseDouble(value);
            if (mapValue > tokenValue) {
                return true;
            }
        }
        //TODO: return ERROR
        return false;
    }

    public boolean smaller(String[] token, Map<String, Object> map) {
        String name = token[0];
        String value = token[1];
        
        var mapVal = map.get(name);
        if (mapVal instanceof Long){
            Long mapValue = Long.parseLong(mapVal.toString());
            Long tokenValue = Long.parseLong(value);
            if (mapValue < tokenValue) {
                return true;
            }
        }
        else if (mapVal instanceof Double){
            Double mapValue = Double.parseDouble(mapVal.toString());
            Double tokenValue = Double.parseDouble(value);
            if (mapValue < tokenValue) {
                return true;
            }
        }
        //TODO: return ERROR
        return false;
    }
}
