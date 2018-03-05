package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import java.util.HashMap;

import rx.Subscription;


/**
 * Created by pepys on 2017/3/23.
 * description:
 */
public interface CityListInteractor<T> {
    /**
     *
     * @param callBack
     * @param cityId    地市编号
     * @param cityLevel 0-国家1-省份2-地市3-区县
     * @param cityCode  NNN-NN-NNNN-NNNN 国家-省份-地市-区县，没有-分隔
     * @param foreigh   0-国内1-国外
     * @param pcityId   上级ID
     * @param cityName  名称（like）
     * @param province  所属省份（like）
     * @param xian      所属区县（like）
     * @param city      所属市（like）
     * @param isAlaway  是否常用
     * @return
     */
    Subscription loadCityList(RequestCallBack<T> callBack, HashMap<String,String> params);
}
