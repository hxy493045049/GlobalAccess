package com.msy.globalaccess.data;

import com.msy.globalaccess.common.enums.ResultCode;

/**
 * Created by pepys on 2017/5/15.
 * description: 有进度的
 */
public interface RequestCallBackProgress<T> extends RequestCallBack<T>{

    //注意,所有回调都在主线程执行,异步执行一般通过rxjava控制
    void beforeRequest();

    void progress(int soFarBytes, int totalBytes, int speed);

    void success(T data);


    void onError(@ResultCode int resultCode, String errorMsg);

    void after();

}
