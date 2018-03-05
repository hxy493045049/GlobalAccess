package com.msy.globalaccess.data.bean.tourism;

import android.support.annotation.IntDef;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_AUTHENTICATION_TIME;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_CONFIRM_BUTTON;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_CREATER_TIME;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_GUEST_SOURCE;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TEAM;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TEAM_STATE;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TITLE;
import static com.msy.globalaccess.data.bean.tourism.TourismDropListBean.ViewType.TYPE_TRAVEL_DATA;

/**
 * Created by shawn on 2017/5/15 0015.
 * <p>
 * description : 旅游局部分下拉框的item的数据结构(更多,行程日期)
 */
public class TourismDropListBean implements MultiItemEntity {
    @ViewType
    private int viewType;
    private ArrayList<DropSelectableBean> data = new ArrayList<>();
    private int nestRvGridSpan = 3;

    public TourismDropListBean(int viewType) {
        this.viewType = viewType;
    }

    public int getNestRvGridSpan() {
        return nestRvGridSpan;
    }

    public void setNestRvGridSpan(int nestRvGridSpan) {
        this.nestRvGridSpan = nestRvGridSpan;
    }

    @Override
    @ViewType
    public int getItemType() {
        return viewType;
    }

    public ArrayList<DropSelectableBean> getData() {
        return data;
    }

    public void setData(DropSelectableBean item) {
        data.add(item);
    }

    @IntDef({TYPE_TITLE, TYPE_TRAVEL_DATA, TYPE_CREATER_TIME, TYPE_TEAM, TYPE_CONFIRM_BUTTON,
            TYPE_AUTHENTICATION_TIME, TYPE_TEAM_STATE, TYPE_GUEST_SOURCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
        int TYPE_TITLE = 0;//分组标题
        int TYPE_TRAVEL_DATA = 1;//行程日期
        int TYPE_CREATER_TIME = 2;//创建时间
        int TYPE_TEAM = 3;//团队类型
        int TYPE_CONFIRM_BUTTON = 4;//确定
        int TYPE_AUTHENTICATION_TIME = 5;//认证时间
        int TYPE_TEAM_STATE = 6;//团队状态
        int TYPE_GUEST_SOURCE = 7;//客源地
    }

    public static class DropSelectableBean {
        @ViewType
        private int viewType;
        private String name;
        private String value = "-1";
        private boolean isSeleted;

        public DropSelectableBean(String name) {
            this.name = name;
        }

        public DropSelectableBean(String name, String value, boolean isSeleted) {
            this.name = name;
            this.value = value;
            this.isSeleted = isSeleted;
        }


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSeleted() {
            return isSeleted;
        }

        public void setSeleted(boolean seleted) {
            isSeleted = seleted;
        }
    }
}
