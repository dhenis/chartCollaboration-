package com.example.deni.chartcollaboration.model;

/**
 * Created by deni on 19/06/2018.
 */

public class Charts {
    //{"value":1,"result":[{"x":"1","y":"1","chart_id":"1","code":"1","category":"1","id":"1"}]}

    String x;
    String y;
    String chart_id;
    String code;
    String category;
    String id;

    public String getX(){
        return x;
    }
    public String getY(){
        return y;
    }
    public String getChartId(){
        return chart_id;
    }
    public String getCode(){
        return code;
    }
    public String getCategory(){
        return category;
    }
    public String getId(){
        return id;
    }

}
