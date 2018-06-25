package com.example.deni.chartcollaboration.model;

import java.util.List;

/**
 * Created by deni on 19/06/2018.
 */


// usage for sending data from android TO API
// result of API
    //{"value":1,"result":[{"x":"1","y":"1","chart_id":"1","code":"1","category":"1","id":"1"}]}
public class Value {

    String value;
    String message;
    List<Charts> result;

    public String getValue(){ return value;}
    public String getMessage(){
        return message;
    }
    public List<Charts> getResult(){
        return result;
    }

}
