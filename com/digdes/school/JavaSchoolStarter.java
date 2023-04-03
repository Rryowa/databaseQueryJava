import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaSchoolStarter {
    List<Map<String,Object>> data = new ArrayList<>();

    public JavaSchoolStarter(){

    }

    public List<Map<String,Object>> execute(String request){
        String trimRequest = request.toLowerCase().replaceAll(" ", "").replaceAll("'", "");
        
        //"INSERTVALUES'lastName'='Федоров','id'=3,'age'=40,'active'=true"
        if (trimRequest.contains("insert")) {
            List<Map<String,Object>> row = new ArrayList<>();
            row.add(insert(trimRequest.replaceAll("insertvalues", "").split(",")));
            return row;
        }

/*
        //"UPDATEVALUES'active'=false,'cost'=10.1where‘id’=3"
        else if (trimRequest.contains("update")) {
            return update(trimRequest.replaceAll("updatevalues", "").split("where"));
        }
*/       
        //"SELECTWHERE'age'>=30and'lastName'ilike'%п%'"
        else if (trimRequest.contains("select")) {

            return select(trimRequest.replaceAll("selectwhere", ""));
        }
        else {
            return data;
        }
    }


    public Map<String,Object> insert(String[] tokens) {
        Map<String,Object> row = new HashMap<>();
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
        data.add(row);
        return row;
    }

/*
    //["active=false,cost=10.1", "id<3andid>1"]
    public List<Map<String,Object>> update(String[] tokensWhere) {
        List<Map<String,Object>> result = new ArrayList<>();
        select(tokensWhere[1]);
        
        
        for (Map<String, Object> map : data) {
            if (isEqual(columnVal, map.get(columnName))) {
                String[] tokens = tokensWhere[0].split(",");
                Map<String,Object> row = new HashMap<>();
                String[] columns = {"id", "lastname", "age", "cost", "active"};
                for (String token : tokens) {
                    for (String name : columns) {
                        if (token.contains(name)) {
                            String[] tokenArray = token.split("=");
                            row.put(name, tokenArray[1]);
                        }
                    }
                }
            }
        }
        return result;
    }
*/
    public List<Map<String,Object>> select(String request) {
        List<Map<String,Object>> result = new ArrayList<>();
        //input: "age>=30 or age<40 or age>10 and age>80"
        //parsed: {age>=30, age<40, age>10 and age>80}
        ArrayList<String> parsed = parse(request);

        
        //arrays of indexes of matching rows
        ArrayList<ArrayList<Integer>> listIndexes = new ArrayList<ArrayList<Integer>>();
        
        //TODO: implement Logic for parsed array splitted by or

        for (String token : parsed){
            //add matching arrays of indexes
            listIndexes.add(compare(token));
        }

        //Intersection ArrayList's of indexes of matching rows;
        //[0, 2]
        ArrayList<Integer> current = listIndexes.get(0);
        
        for (int i = 1; i < listIndexes.size(); i++) {
            //[1, 2]
            ArrayList<Integer> next = listIndexes.get(i);
            //2
            current.retainAll(next);
        }
        
        //return matching rows
        return adder(current, result);


        else if (!parsed.getOrArray().isEmpty()) {
            //arrays of indexes of matching rows
            ArrayList<ArrayList<Integer>> listIndexes = new ArrayList<ArrayList<Integer>>();
            
            for (String token : parsed.getOrArray()){
                //add matching arrays of indexes
                listIndexes.add(compare(token));
                //for each token we have array of indexes of matching rows
            }

            //Union of ArrayList's of indexes of matching rows;
            //[0, 2]
            Set<Integer> current = new HashSet<>(listIndexes.get(0));
            
            for (int i = 1; i < listIndexes.size(); i++) {
                //[1, 2]
                ArrayList<Integer> next = listIndexes.get(i);
                //[0, 1, 2]
                current.addAll(next);
            }
            
            //return matching rows
            return adder(current, result);
        }
        else return result;
    }


    //"age>=30andlastNameilike%п%andage<40"
    public ArrayList<String> parse(String request) {
        //аналог mergeSort
        //массив с массивами, массивы с токенами and разделены запятыми, то есть or
        //если есть or, разбиваем на два массива слева от or и справа
        //рекурсивно идем по этим массивам, если в них есть or, то разбиваем на два
        //повторить

        //convert string to list
        ArrayList<String> str = new ArrayList<String>();
        str.add(request);
        
        //call recursive
        ArrayList<String> result = separate(str);
        
        for (String x : result) {
            System.out.println(x);
        }
        return separate(result);
    }

    //"age>=30 and age<40 or age>10 and age>80"
    public ArrayList<String> separate(ArrayList<String> request){
        //convert list ot string
        String str = request.get(0);
    	if (!(str.contains("or"))) {
    		return request;
    	}
    	
    	//divide
    	ArrayList<ArrayList<String>> divide = splitter(str);
    	ArrayList<String> one = separate(divide.get(0));
    	ArrayList<String> two = separate(divide.get(1));
    	ArrayList<String> result = merge(one, two);
    	
    	return result;
    }
    
    public ArrayList<String> merge(ArrayList<String> a, ArrayList<String> b) {
    	ArrayList<String> result = new ArrayList<String>();
    	for (String x : a) {
    	    result.add(x);
    	}
    	for (String x : b) {
    	    result.add(x);
    	}
    	return result;
    }

    //returns {{age>=30}, {"age<40andage>10orage>80"}}
    public ArrayList<ArrayList<String>> splitter(String request){
        ArrayList<ArrayList<String>> divide = new ArrayList<ArrayList<String>>();
        int idx = request.indexOf("or");
        ArrayList<String> one = new ArrayList<String>();
        one.add(request.substring(0, idx));
        divide.add(one);
        ArrayList<String> two = new ArrayList<String>();
        two.add(request.substring(idx+3, request.length()));
        divide.add(two);
        return divide;
    }


    public List<Map<String,Object>> adder(Collection<Integer> current,  List<Map<String,Object>> result) {
        for (int i = 0; i < data.size(); i++) {
            for (int x : current) {
                if (i == x) {
                    result.add(data.get(i));
                }
            }
        }
        return result;
    }

    public ArrayList<Integer> compare(String token) {
        //rows that satisfy request
        ArrayList<Integer> indexes = new ArrayList<Integer>();
                
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            if (token.contains(">=")) {
                if (greaterOrEqual(token.split(">="), map)){
                    indexes.add(i);
                }
            }
            else if (token.contains("<")) {
                if (smaller(token.split("<"), map)){
                    indexes.add(i);
                }
            }
        }
        return indexes;
    }

    public boolean greaterOrEqual(String[] token, Map<String, Object> map) {
        String name = token[0];
        String value = token[1];
        
        var mapVal = map.get(name);
        if (map.get(name) instanceof Long){
            Long mapValue = Long.parseLong(mapVal.toString());
            Long tokenValue = Long.parseLong(value);
            if (tokenValue >= mapValue) {
                return true;
            }
            return false;
        }
        else if (map.get(name) instanceof Double){
            Double mapValue = Double.parseDouble(mapVal.toString());
            Double tokenValue = Double.parseDouble(value);
            if (tokenValue >= mapValue) {
                return true;
            }
            return false;
        }
        //TODO: return ERROR
        return false;
    }

    public boolean smaller(String[] token, Map<String, Object> map) {
        String name = token[0];
        String value = token[1];
        
        var mapVal = map.get(name);
        if (map.get(name) instanceof Long){
            Long mapValue = Long.parseLong(mapVal.toString());
            Long tokenValue = Long.parseLong(value);
            if (tokenValue < mapValue) {
                return true;
            }
            return false;
        }
        else if (map.get(name) instanceof Double){
            Double mapValue = Double.parseDouble(mapVal.toString());
            Double tokenValue = Double.parseDouble(value);
            if (tokenValue < mapValue) {
                return true;
            }
            return false;
        }
        //TODO: return ERROR
        return false;
    }
}