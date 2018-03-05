package com.msy.globalaccess.business.travelAgency.search.contract.impl;

import android.text.TextUtils;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.impl.BasePresenterImpl;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchDelegateGuiderContract;
import com.msy.globalaccess.data.api.GuiderApi;
import com.msy.globalaccess.data.bean.search.SearchBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import static com.msy.globalaccess.config.DataSetting.LIST_DATA_SIZE;

/**
 * Created by shawn on 2017/7/3 0003.
 * <p>
 * description :
 */

public class SearchDelegateGuiderPresenterImpl extends BasePresenterImpl<SearchDelegateGuiderContract.View>
        implements SearchDelegateGuiderContract.Presenter {
    private String[] keys = App.getResource().getStringArray(R.array.touristAdminQueryCondition);
    private ArrayList<String> typeValues = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .delegateSearchType)));
    private ArrayList<String> statusValues = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .delegateSearchStatus)));

    @Inject
    public SearchDelegateGuiderPresenterImpl() {
    }

    @Override
    public void onStart() {
        loadQueryCondition();
    }

    @Override
    public void loadQueryCondition() {
        mView.onQueryConditionCallback(getSearchCondition());
    }

    @Override
    public void getQueryParam() {
        mView.onParamCallBack(buildParam());
    }

    private HashMap<String, Object> buildParam() {
        List<SearchBean> conditions = mView.getCurrentCondition();
        String teamCode = "", opType = "", operStatus = "", createTimeStart = "", createTimeEnd = "";
        for (SearchBean bean : conditions) {
            if (bean.getKey().equals(keys[0])) {//处理类型
                opType = bean.getSelectedValue();
            } else if (bean.getKey().equals(keys[1])) {//业务状态
                operStatus = bean.getSelectedValue();
            } else if (bean.getKey().equals(keys[2])) {//团队编号
                teamCode = bean.getSelectedValue();
            } else if (bean.getKey().equals(keys[3])) {//创建日期
                String[] times = bean.getTimes();
                if (times != null && times.length == 2) {
                    createTimeStart = !TextUtils.isEmpty(bean.getTimes()[0]) ? bean.getTimes()[0] : "";
                    createTimeEnd = !TextUtils.isEmpty(bean.getTimes()[1]) ? bean.getTimes()[1] : "";
                } else {
                    createTimeStart = !TextUtils.isEmpty(bean.getTimes()[0]) ? bean.getTimes()[0] : "";
                    createTimeEnd = "";
                }
            }
        }

        HashMap<String, Object> param = new HashMap<>();
        param.put(GuiderApi.DelegateApi.USERID, App.userHelper.getUser().getUserId());
        param.put(GuiderApi.DelegateApi.CURRENTPAGENUM, "1");
        param.put(GuiderApi.DelegateApi.SHOWNUM, LIST_DATA_SIZE);
        param.put(GuiderApi.DelegateApi.TEAMCODE, teamCode);
        param.put(GuiderApi.DelegateApi.OPTYPE, typeValues.indexOf(opType) != -1 ? typeValues.indexOf(opType) : "");
        param.put(GuiderApi.DelegateApi.OPSTATUS, statusValues.indexOf(operStatus) != -1 ? statusValues.indexOf(operStatus) : "");
        param.put(GuiderApi.DelegateApi.CREATETIMESTART, createTimeStart);
        param.put(GuiderApi.DelegateApi.CREATETIMEEND, createTimeEnd);
        return param;
    }

    private List<SearchBean> getSearchCondition() {
        ArrayList<SearchBean> list = new ArrayList<>();
        String[] str = App.getResource().getStringArray(R.array.touristAdminQueryCondition);
        for (int i = 0; i < str.length; i++) {
            SearchBean bean = new SearchBean();
            ArrayList<String> values = new ArrayList<>();
            if (i == 0) {
                bean.setValueType(SearchBean.TYPE_SCROLL);
                for (int j = 0; j < typeValues.size(); j++) {
                    values.add(typeValues.get(j));
                }
            } else if (i == 1) {
                bean.setValueType(SearchBean.TYPE_SCROLL);
                for (int j = 0; j < statusValues.size(); j++) {
                    values.add(statusValues.get(j));
                }
            } else if (i == 2) {
                bean.setValueType(SearchBean.TYPE_TEXT);
            } else if (i == 3) {
                bean.setValueType(SearchBean.TYPE_MULTI_TIME);
            }
            bean.setKey(str[i]);
            bean.setValues(values);
            list.add(bean);
        }
        return list;
    }
}
