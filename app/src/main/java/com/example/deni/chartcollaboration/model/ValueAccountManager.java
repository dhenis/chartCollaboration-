package com.example.deni.chartcollaboration.model;

import java.util.List;

/**
 * Created by deni on 28/07/2018.
 */

public class ValueAccountManager {

    String value;

    String message;

    List<AccountManager> result;

    public String getValue(){ return value;}

    public String getMessage(){
        return message;
    }

    public List<AccountManager> getChartAccountResult(){
        return result;
    } // retrive when using all data


    public String getLastElement(){

        return String.valueOf(result.get(result.size()-1));

    }


}
