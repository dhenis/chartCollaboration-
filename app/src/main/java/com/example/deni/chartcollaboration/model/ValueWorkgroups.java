package com.example.deni.chartcollaboration.model;

import java.util.List;

/**
 * Created by deni on 18/07/2018.
 */

public class ValueWorkgroups {

    String value;

    String message;

    List<Workgroups> result;

    public String getValue(){ return value;}
    public String getMessage(){
        return message;
    }

    public List<Workgroups> getWorkgroupResult(){
        return result;
    } // retrive when using all data

}
