package com.simplelineview.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xjj on 2017/7/3.
 */

public class HealthDataInfo {

    public String status;
    public String msg;
    public ArrayList<BodyBean> body;

    public static class BodyBean {

        public int type;
        public int linetype;
        public  String title;
        public List<DatalistBean> datalist;

        public int pointNum; // 默认显示第几个
    }

    public static class DatalistBean {

        public String value;
        public String time;
    }

}
