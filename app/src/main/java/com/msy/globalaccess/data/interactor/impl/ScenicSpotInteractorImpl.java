package com.msy.globalaccess.data.interactor.impl;

import com.google.gson.Gson;
import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.api.ScenicSpotApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicListBean;
import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;
import com.msy.globalaccess.data.interactor.ScenicSpotInteractor;
import com.msy.globalaccess.exception.RxException;
import com.msy.globalaccess.utils.ErrorUtils;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.rxhelper.DefaultSubscriber;
import com.msy.globalaccess.utils.rxhelper.RxJavaUtils;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * 提交景点相关实现类
 * Created by chensh on 2017/3/20 0020.
 */

public class ScenicSpotInteractorImpl implements ScenicSpotInteractor {
    private ScenicSpotApi scenicSpotApi;

    @Inject
    ScenicSpotInteractorImpl(Retrofit mRetrofit) {
        this.scenicSpotApi = mRetrofit.create(ScenicSpotApi.class);
    }

    @Override
    public Subscription submitScenic(RequestCallBack callBack, String tripDate, String scenicId) {
        callBack.beforeRequest();
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(ScenicSpotApi.appScenicTicketTypeSearch)
                .setParam(ScenicSpotApi.tripDate, tripDate)
                .setParam(ScenicSpotApi.scenicId, scenicId)
                .build().getParam();
        return scenicSpotApi.submitScenic(params).compose(RxJavaUtils.<BaseBean<Object>>defaultSchedulers())
                .flatMap(new Func1<BaseBean<Object>, Observable<ArrayList<TripScenicTicketBean>>>() {
                    @Override
                    public Observable<ArrayList<TripScenicTicketBean>> call(BaseBean<Object> objectBaseBean) {
                        if (objectBaseBean.getStatus() == ResultCode.SUCCESS) {
                            Gson gson = new Gson();
                            TripScenicListBean scenicListBean = gson.fromJson(objectBaseBean.getData().toString(), TripScenicListBean.class);
                            return Observable.just(changDate(scenicListBean));
                        } else {
                            String errorMessage = ErrorUtils.getErrorMessage(objectBaseBean, R.string.internet_error);
                            return Observable.error(new RxException(objectBaseBean.getStatus(), errorMessage));
                        }
                    }
                }).subscribe(new DefaultSubscriber<ArrayList<TripScenicTicketBean>>(callBack));
    }

    /**
     * 变更数据类型
     *
     * @param data 票务信息
     * @return 票务信息
     */
    private ArrayList<TripScenicTicketBean> changDate(TripScenicListBean data) {
        ArrayList<TripScenicTicketBean> list = new ArrayList<>();
        if (data != null && data.getTripSceniclist() != null && data.getTripSceniclist().size() > 0) {
            ArrayList<TripScenicListBean.TripSceniclistBean> tripScenictecketlist = (ArrayList<TripScenicListBean.TripSceniclistBean>) data.getTripSceniclist();
            for (int i = 0; i < tripScenictecketlist.size(); i++) {
                TripScenicTicketBean tripScenicTicketBean = new TripScenicTicketBean();
                tripScenicTicketBean.setPrice(tripScenictecketlist.get(i).getPrice());
                tripScenicTicketBean.setTicketPriceName(tripScenictecketlist.get(i).getTicketPriceName());
                tripScenicTicketBean.setTicketType(tripScenictecketlist.get(i).getTicketType());
                tripScenicTicketBean.setScenicTicketTypeId(tripScenictecketlist.get(i).getScenicTicketTypeId());
                tripScenicTicketBean.setChangeType("2");
                tripScenicTicketBean.setIsDate("0");
                list.add(tripScenicTicketBean);
            }
        }
        return list;
    }
}
