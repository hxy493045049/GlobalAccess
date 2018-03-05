
package com.msy.globalaccess.data.bean.base;

/**
 * 通用bean
 * 以下已下用到界面
 * 1、查询相关
 * Created by chensh on 2017/2/14 0014.
 */

public class CurrencyBean {
    public CurrencyBean() {
    }

    public CurrencyBean(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String id;
}
