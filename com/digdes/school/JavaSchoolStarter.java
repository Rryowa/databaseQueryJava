package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaSchoolStarter{
    List<Map<String,Object>> data = new ArrayList<>();

    public JavaSchoolStarter(){

    }

    public List<Map<String,Object>> execute(String request){
        String trimRequest = request.toLowerCase().replaceAll(" ", "").replaceAll("'", "");
        
        //"insertvalueslastname=федоров,id=3,age=40,active=true"
        if (trimRequest.contains("insert")) {
            List<Map<String,Object>> result = new ArrayList<>();
            Map<String,Object> row = new HashMap<>();
            result.add(insert(row, trimRequest.replaceAll("insertvalues", "").split(",")));
            //update data
            data.add(result.get(0));
            return result;
        }

        //"updatevaluesactive=true,cost=100whereid<3andid>1"
        else if (trimRequest.contains("update")) {
            return update(trimRequest.replaceAll("updatevalues", "").split("where"));
        }
       
        //"SELECTWHERE'age'>=30and'lastName'ilike'%п%'"
        else if (trimRequest.contains("select")) {
            return select(trimRequest.replaceAll("selectwhere", ""));
        }

        else if (trimRequest.contains("delete")) {
            return delete(trimRequest.replaceAll("deletewhere", ""));
        }

        else {
            return data;
        }
    }


    public Map<String,Object> insert(Map<String,Object> row, String[] tokens) {
        for (String token : tokens) {
            if (token.contains("id")) {
                String x = token.split("=")[1];
                row.put("id", Long.parseLong(x));
            }
            if (token.contains("lastname")) {
                String x = token.split("=")[1];
                row.put("lastname", x);
            }
            if (token.contains("age")) {
                String x = token.split("=")[1];
                row.put("age", Long.parseLong(x));
            }
            if (token.contains("cost")) {
                String x = token.split("=")[1];
                row.put("cost", Double.parseDouble(x));
            }
            if (token.contains("active")) {
                String x = token.split("=")[1];
                row.put("active", Boolean.parseBoolean(x));
            }
        }
        return row;
    }


    //"active=false,cost=100", "id<3andid>1"]
    public List<Map<String,Object>> update(String[] tokensWhere) {
        List<Map<String,Object>> result = new ArrayList<>();
        List<Map<String,Object>> selected = select(tokensWhere[1]);

        for (Map<String, Object> mapSelected : selected) {
            for (Map<String, Object> map : data) {
                
                if (map.equals(mapSelected)){
                    //add updated rows
                    result.add(insert(map, tokensWhere[0].split(",")));
                }
            }
        }
        return result;
    }


    public List<Map<String,Object>> select(String request) {
        List<Map<String,Object>> result = new ArrayList<>();
        //input: "age>25 and age<=40 or age>30 and age<80 and age>25"

        ParsedLogic logic = new ParsedLogic();
        ArrayList<String> parsed = logic.parseLogic(request);
        
        Comparative comp = new Comparative(data);

        //arrays of indexes of matching rows
        ArrayList<ArrayList<Integer>> listIndexes = new ArrayList<ArrayList<Integer>>();

        //parsed: {age>25 and age<=40, age>30 and age<80 and age>25}
        for (String token : parsed){
            //System.out.println("parsed: " + token);
            
            if (token.contains("and")){
                
                listIndexes.add(comp.compareAnd(token));
            }
            else {
                //add matching arrays of indexes
                listIndexes.add(comp.compare(token));
                //for each token we have array of indexes of matching rows
            }
        }

        //listIndexes = {{1, 2}, {2}}

        //Union of ArrayList's of indexes of matching rows;
        //[1, 2]
        Set<Integer> current = new HashSet<>(listIndexes.get(0));
        
        for (int i = 1; i < listIndexes.size(); i++) {
            //[2]
            ArrayList<Integer> next = listIndexes.get(i);
            //[1, 2]!!!!!!!!!
            current.addAll(next);
        }
        
        //return matching rows
        for (int i = 0; i < data.size(); i++) {
            for (int x : current) {
                if (i == x) {
                    result.add(data.get(i));
                }
            }
        }
        //rows 0 and 2
        return result;
    }

    public List<Map<String,Object>> delete(String request) {
        List<Map<String,Object>> result = new ArrayList<>();
        List<Map<String,Object>> selected = select(request);

        for (Map<String, Object> mapSelected : selected) {
            for (Map<String, Object> map : data) {
        
                if (map.equals(mapSelected)){
                    result.add(map);
                    data.remove(map);
                    break;
                }
            }
        }
        return result;
    }

    public void print(List<Map<String,Object>> result) {
        for (Map<String, Object> map : result) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue().toString() + ", ");
            }
            System.out.println();
        }
    }

    public void printData() {
        for (Map<String, Object> map : data) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue().toString() + ", ");
            }
            System.out.println();
        }
    }
}