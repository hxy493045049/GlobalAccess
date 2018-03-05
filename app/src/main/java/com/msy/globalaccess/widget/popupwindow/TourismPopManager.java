package com.msy.globalaccess.widget.popupwindow;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.business.adapter.TourismDropListAdapter;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;

/**
 * Created by shawn on 2017/5/15 0015.
 * <p>
 * description : 旅游局模块深下拉框的封装,用于显示 "行程日期"  和  "更多"
 */
public class TourismPopManager {
    private CustomPopWindow mPop;
    private SparseArrayCompat<OnItemChildClickListener> events;
    private SparseArrayCompat<View> tvAlls;
    private ArrayList<TourismDropListBean> data;
    private Context ctx;
    private BaseQuickAdapter.OnItemClickListener itemClickListener;
    private View contentView;
    private TourismDropListAdapter adapter;

    private TourismPopManager(Builder builder) {
        this.ctx = builder.ctx;
        this.itemClickListener = builder.itemClickListener;
        this.events = builder.events.clone();
        tvAlls = new SparseArrayCompat<>();
        this.data = new ArrayList<>(builder.data);
        int width = builder.width;
        int height = builder.height;
        boolean enableBgDark = builder.enableBgDark;
        PopupWindow.OnDismissListener mOnDismissListener = builder.mOnDismissListener;

        init();
        mPop = new CustomPopWindow.PopupWindowBuilder(ctx)
                .size(width != 0 ? width : DisplayUtil.getScreenWidth(), height != 0 ? height : ViewGroup
                        .LayoutParams.WRAP_CONTENT)
                .setView(contentView)
                .enableBackgroundDark(enableBgDark)
                .setOnDissmissListener(mOnDismissListener)
                .create();
    }

    private static String getDefaultTimeBegin(String regular) {

        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.HOUR_OF_DAY, 0);
        //        calendar.set(Calendar.MINUTE, 0);
        //        Date date = calendar.getTime();
        //        return TimeFormat.formatData(regular, date);
        return "";
    }

    private static String getDefaultTimeEnd(String regular) {
        //        Calendar calendar = Calendar.getInstance();
        //        calendar.set(Calendar.HOUR_OF_DAY, 23);
        //        calendar.set(Calendar.MINUTE, 59);
        //        Date date = calendar.getTime();
        //        return TimeFormat.formatData(regular, date);
        return "";
    }

    public TourismPopManager showPop(View anchor, int xOff, int yOff, int gravity) {
        mPop.showAsDropDown(anchor, xOff, yOff, gravity);
        return this;
    }

    public TourismPopManager showAsDropDown(View anchor, int xOff, int yOff) {
        mPop.showAsDropDown(anchor, xOff, yOff);
        return this;
    }

    public TourismPopManager dismiss() {
        mPop.dissmiss();
        return this;
    }

    public TourismPopManager showAsDropDown(View anchor) {
        mPop.showAsDropDown(anchor, 0, 0);
        return this;
    }

    public TourismDropListAdapter getDropDownAdapter() {
        return adapter;
    }

    //变更行程日期
    public void updataTravelTime(String beginTime) {
        //行程日期
        TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TRAVEL_DATA);
        toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                getDefaultTimeBegin(TimeFormat.regular7) : beginTime));

        int pos = findPosition(TourismDropListBean.ViewType.TYPE_TRAVEL_DATA);
        if (pos == -1) {
            return;
        }
        data.set(pos, toursmTimeBean);
        adapter.notifyItemChanged(pos);
    }

    public void updateTeamType(List<TourismDropListBean.DropSelectableBean> teamTypes) {
        TourismDropListBean teamType = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TEAM);

        for (TourismDropListBean.DropSelectableBean str : teamTypes) {
            teamType.setData(str);
        }

        int pos = findPosition(TourismDropListBean.ViewType.TYPE_TEAM);
        if (pos == -1) {
            return;
        }
        data.set(pos, teamType);
        adapter.clearNestSelectedState(TourismDropListBean.ViewType.TYPE_TEAM);
        adapter.notifyItemChanged(pos);
    }

    public void resetPop() {
        adapter.clearNestSelectedState(TourismDropListBean.ViewType.TYPE_TEAM);
        adapter.clearNestSelectedState(TourismDropListBean.ViewType.TYPE_TEAM_STATE);
        adapter.clearNestSelectedState(TourismDropListBean.ViewType.TYPE_GUEST_SOURCE);
        updataTravelTime("");
        updateAuthTime("", "");
        updateCreaterTime("", "");
        for (int i = 0; i < tvAlls.size(); i++) {
            View v = tvAlls.get(tvAlls.keyAt(i));
            v.setSelected(true);
        }
    }


    public void updateCreaterTime(String beginTime, String endTime) {
        //行程日期
        TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType
                .TYPE_CREATER_TIME);
        toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                getDefaultTimeBegin(TimeFormat.regular7) : beginTime));
        toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(endTime.trim()) ?
                getDefaultTimeEnd(TimeFormat.regular7) : endTime));

        int pos = findPosition(TourismDropListBean.ViewType.TYPE_CREATER_TIME);
        if (pos == -1) {
            return;
        }
        data.set(pos, toursmTimeBean);
        adapter.notifyItemChanged(pos);
    }

    public void updateAuthTime(String beginTime, String endTime) {
        //行程日期
        TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType
                .TYPE_AUTHENTICATION_TIME);
        toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                getDefaultTimeBegin(TimeFormat.regular9) : beginTime));
        toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(endTime.trim()) ?
                getDefaultTimeEnd(TimeFormat.regular9) : endTime));

        int pos = findPosition(TourismDropListBean.ViewType.TYPE_AUTHENTICATION_TIME);
        if (pos == -1) {
            return;
        }
        data.set(pos, toursmTimeBean);
        adapter.notifyItemChanged(pos);
    }

    public void setSetectedItem(@TourismDropListBean.ViewType int viewType, TourismDropListBean.DropSelectableBean
            bean) {
        int pos = findPosition(viewType);
        View tvAll = tvAlls.get(viewType);
        if (pos == -1) {
            return;
        }
        if (bean == null) {
            if (tvAll != null) {
                tvAll.setSelected(true);
            }
            adapter.clearNestSelectedState(viewType);
        } else if (bean.getValue().equals("-1") || bean.getName().equals("全部")) {
            if (tvAll != null) {
                tvAll.setSelected(true);
            }
            adapter.clearNestSelectedState(viewType);
        } else {
            TourismDropListBean item = data.get(pos);
            int itemPos = -1;
            for (int i = 0; i < item.getData().size(); i++) {
                TourismDropListBean.DropSelectableBean d = item.getData().get(i);
                if (d.getValue().equals(bean.getValue())) {
                    itemPos = i;
                    break;
                }
            }

            if (itemPos != -1) {
                if (tvAll != null) {
                    tvAll.setSelected(false);
                }
                adapter.clearNestSelectedState(viewType);
                adapter.setNestSelectedItem(viewType, itemPos);
            }
        }
    }

    private int findPosition(int type) {
        int pos = -1;

        for (int i = 0; i < data.size(); i++) {
            TourismDropListBean bean = data.get(i);
            if (bean.getItemType() == type) {
                return i;
            }
        }
        return pos;
    }

    private void initRecyclerView(RecyclerView recyclerView, List<TourismDropListBean> data, Context ctx) {
        adapter = new TourismDropListAdapter(data);

        //团队类型的具体类型的点击事件,它关联嵌套rv的点击事件,所以要将事件传入到嵌套的adapter中处理
        OnItemChildClickListener nestItemChildClickListener = events.get(R.id.tvCell);
        if (nestItemChildClickListener != null) {
            //这个方法是自定义的
            adapter.addOnItemChildClickListener(nestItemChildClickListener);
        }

        //设置一个回调和嵌套adapter交互,用于确认团队类型的单选状态,当具体类型被点击后,该接口被回调
        adapter.setNestInteractListener(new TourismDropListAdapter.NestInteractListener() {
            @Override
            public void onNestedSelected(int viewType) {
                View tvAll = tvAlls.get(viewType);
                if (tvAll != null) {
                    tvAll.setSelected(false);
                }
            }
        });
        adapter.setOnNestInflateListener(new TourismDropListAdapter.OnNestInflateListener() {
            @Override
            public void onNestInflated(@TourismDropListBean.ViewType int viewType, View btnAll) {
                View v = tvAlls.get(viewType);
                if (v == null) {
                    tvAlls.put(viewType, btnAll);
                }
            }
        });

        //处理rv中子事件的点击,不包含嵌套rv的事件处理,这个方法是baseadapter中的
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                OnItemChildClickListener listener = events.get(view.getId());
                if (listener != null) {
                    TourismDropListBean bean = (TourismDropListBean) baseQuickAdapter.getData().get(i);
                    //这里耦合度非常高
                    switch (view.getId()) {
                        case R.id.llTimeBegin:
                            listener.onCallBack(bean.getData().get(0), bean.getItemType());
                            break;
                        case R.id.llTimeEnd:
                            listener.onCallBack(bean.getData().get(1), bean.getItemType());
                            break;
                        case R.id.tvAll:
                            View v = tvAlls.get(bean.getItemType());
                            if (v == null) {
                                tvAlls.put(bean.getItemType(), view);
                            }
                            view.setSelected(true);
                            adapter.clearNestSelectedState(bean.getItemType());
                            listener.onCallBack(new TourismDropListBean.DropSelectableBean("全部"), bean.getItemType());
                            break;
                        case R.id.tvTimeBegin:
                            listener.onCallBack(bean.getData().get(0), bean.getItemType());
                            break;
                        case R.id.tvTimeBeginDetail:
                            listener.onCallBack(bean.getData().get(0), bean.getItemType());
                            break;
                        case R.id.tvTimeEnd:
                            listener.onCallBack(bean.getData().get(1), bean.getItemType());
                            break;
                        case R.id.tvTimeEndDetail:
                            listener.onCallBack(bean.getData().get(1), bean.getItemType());
                            break;
                        case R.id.btnConfirm:
                            listener.onCallBack(new TourismDropListBean.DropSelectableBean(((AppCompatButton) view)
                                    .getText().toString()), bean.getItemType());
                            break;
                    }
                }
            }
        });

        //处理rv中item事件的点击
        if (itemClickListener != null) {
            adapter.setOnItemClickListener(itemClickListener);
        }

        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setHasFixedSize(true);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void init() {
        contentView = LayoutInflater.from(ctx).inflate(R.layout.recyclerview_only, null, false);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rvOnly);
        initRecyclerView(recyclerView, data, ctx);
    }

    public void setOnTimeDeleteCallback(TourismDropListAdapter.OnTimeDeleteCallback callBack) {
        if (adapter != null) {
            adapter.setOnTimeDeleteCallback(callBack);
        }
    }

    //子item的点击回调
    public interface OnItemChildClickListener {
        void onCallBack(TourismDropListBean.DropSelectableBean value, @TourismDropListBean.ViewType int ViewType);
    }

    @IntDef({R.id.llTimeBegin, R.id.llTimeEnd, R.id.tvAll, R.id.tvCell, R.id.btnConfirm, R.id.tvTimeBegin, R.id
            .tvTimeBeginDetail, R.id.tvTimeEnd, R.id.tvTimeEndDetail})
    @Retention(RetentionPolicy.SOURCE)
    private @interface ItemIds {
    }

    public static class Builder {
        private ArrayList<TourismDropListBean> data;
        private Context ctx;
        private BaseQuickAdapter.OnItemClickListener itemClickListener;
        private SparseArrayCompat<OnItemChildClickListener> events;

        private int width, height;
        private boolean enableBgDark;
        private PopupWindow.OnDismissListener mOnDismissListener;


        public Builder(Context ctx) {
            this.ctx = ctx;
            data = new ArrayList<>();
            events = new SparseArrayCompat<>();
        }

        public Builder setOnDismissListener(PopupWindow.OnDismissListener mOnDismissListener) {
            this.mOnDismissListener = mOnDismissListener;
            return this;
        }

        public Builder setEnableBgDark(boolean flag) {
            enableBgDark = flag;
            return this;
        }


        public Builder setTravelDate(String beginTime) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .travel_group_title)));
            data.add(titleBean);

            //行程日期
            TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TRAVEL_DATA);
            toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                    getDefaultTimeBegin(TimeFormat.regular7) : beginTime));
            //                    toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty
            // (endTime.trim()) ?
            //                            getDefaultTimeBegin(TimeFormat.regular7) : endTime)
            //            );
            data.add(toursmTimeBean);

            return this;
        }

        public Builder setCreaterTime(String beginTime, String endTime) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .creater_group_title)));
            data.add(titleBean);

            //行程日期
            TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType
                    .TYPE_CREATER_TIME);
            toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                    getDefaultTimeBegin(TimeFormat.regular7) : beginTime));
            toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(endTime.trim()) ?
                    getDefaultTimeEnd(TimeFormat.regular7) : endTime));
            data.add(toursmTimeBean);

            return this;
        }

        public Builder setAuthenticationTime(String beginTime, String endTime) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .authentication_group_title)));
            data.add(titleBean);

            //认证日期
            TourismDropListBean toursmTimeBean = new TourismDropListBean(TourismDropListBean.ViewType
                    .TYPE_AUTHENTICATION_TIME);
            toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(beginTime.trim()) ?
                    getDefaultTimeBegin(TimeFormat.regular9) : beginTime));
            toursmTimeBean.setData(new TourismDropListBean.DropSelectableBean(TextUtils.isEmpty(endTime.trim()) ?
                    getDefaultTimeEnd(TimeFormat.regular9) : endTime));
            data.add(toursmTimeBean);

            return this;
        }

        public Builder setTeamType(List<TourismDropListBean.DropSelectableBean> teamTypes) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .team_type)));
            data.add(titleBean);

            //内容
            TourismDropListBean teamType = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TEAM);
            teamType.setNestRvGridSpan(3);//设置一行显示几个
            for (TourismDropListBean.DropSelectableBean str : teamTypes) {
                teamType.setData(str);
            }
            data.add(teamType);
            return this;
        }

        public Builder setTeamState(List<TourismDropListBean.DropSelectableBean> teamTypes) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .team_state)));
            data.add(titleBean);

            //内容
            TourismDropListBean teamState = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TEAM_STATE);
            teamState.setNestRvGridSpan(4);
            for (TourismDropListBean.DropSelectableBean str : teamTypes) {
                teamState.setData(str);
            }
            data.add(teamState);
            return this;
        }

        public Builder setGuestSource(List<TourismDropListBean.DropSelectableBean> teamTypes) {
            //标题
            TourismDropListBean titleBean = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_TITLE);
            titleBean.setData(new TourismDropListBean.DropSelectableBean(App.getResourceString(R.string
                    .guest_source)));
            data.add(titleBean);

            //内容
            TourismDropListBean guestSource = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_GUEST_SOURCE);
            guestSource.setNestRvGridSpan(4);
            for (TourismDropListBean.DropSelectableBean str : teamTypes) {
                guestSource.setData(str);
            }
            data.add(guestSource);
            return this;
        }


        public Builder setSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
            return this;
        }

        /**
         * 目前此方法支持的事件仅限于{@link ItemIds}的范围内
         */
        public Builder addOnItemChildClickListener(@ItemIds int eventId, OnItemChildClickListener listener) {
            events.put(eventId, listener);
            return this;
        }

        public TourismPopManager build() {
            //确定按钮
            TourismDropListBean btn = new TourismDropListBean(TourismDropListBean.ViewType.TYPE_CONFIRM_BUTTON);
            data.add(btn);
            return new TourismPopManager(this);
        }
    }


}
