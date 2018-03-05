package com.msy.globalaccess.data.bean.base;


/**
 * Created by chensh on 2017/2/9 0009.
 */

public class BaseBean<T> extends NoDataBean {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
