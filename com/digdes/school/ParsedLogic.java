package com.digdes.school;

import java.util.ArrayList;

public class ParsedLogic{
    //"age>=30andlastNameilike%п%andage<40"

    public ParsedLogic() {

    }

    public ArrayList<String> parseLogic(String request) {
        //аналог mergeSort
        //массив с массивами, массивы с токенами and разделены запятыми, то есть or
        //если есть or, разбиваем на два массива слева от or и справа
        //рекурсивно идем по этим массивам, если в них есть or, то разбиваем на два
        //повторить

        //convert string to list
        ArrayList<String> str = new ArrayList<String>();
        str.add(request);
        
        //NOTE: separate is a recursive function
        return separate(str);
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
        two.add(request.substring(idx+2, request.length()));
        divide.add(two);
        return divide;
    }

}