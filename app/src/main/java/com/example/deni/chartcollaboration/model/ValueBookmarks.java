package com.example.deni.chartcollaboration.model;

import java.util.List;

/**
 * Created by deni on 08/11/2018.
 */

public class ValueBookmarks {


    String value;

    String message;

    List<Bookmarks> result;

    public String getValue(){ return value;}

    public String getMessage(){
        return message;
    }

    public List<Bookmarks> getChartAccountResult(){
        return result;
    } // retrive when using all data

    public List<Bookmarks> getResult(){
        return result;
    } // retrive when using all data


    public String getLastElement(){

        return String.valueOf(result.get(result.size()-1));

    }


}
