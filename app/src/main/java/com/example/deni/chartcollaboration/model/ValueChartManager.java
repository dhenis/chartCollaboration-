package com.example.deni.chartcollaboration.model;

import java.util.List;

/**
 * Created by deni on 20/07/2018.
 */

public class ValueChartManager {

    String value;

    String message;

    List<ChartManager> result;

    public String getValue(){ return value;}
    public String getMessage(){
        return message;
    }

    public List<ChartManager> getChartManagerResult(){
        return result;
    } // retrive when using all data

}
