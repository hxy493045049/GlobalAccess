package com.msy.globalaccess.data.bean.search;

import com.msy.globalaccess.data.bean.base.CurrencyBean;

import java.util.ArrayList;

/**
 * Created by chensh on 2017/2/14 0014.
 */

public class SearchValueListBean {
    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    private int item;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CurrencyBean> getData() {
        return data;
    }

    public void setData(ArrayList<CurrencyBean> data) {
        this.data = data;
    }

    public String name;
    public ArrayList<CurrencyBean> data;

    public SearchValueListBean(int item, String name, ArrayList<CurrencyBean> data) {
        this.name = name;
        this.data = data;
        this.item = item;
    }

    public SearchValueListBean() {
    }

}
