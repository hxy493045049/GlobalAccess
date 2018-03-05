package com.msy.globalaccess.data.bean.search;

/**
 * 筛选条件实体类
 * Created by chensh on 2017/2/11 0011.
 */

public class PerequisiteBean {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String name;
    private String key;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    private String param;

    public PerequisiteBean(String name, String key, String param) {
        this.key = key;
        this.name = name;
        this.param = param;
    }
}
