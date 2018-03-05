package com.msy.globalaccess.business.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.widget.popupwindow.TourismPopManager;
import com.msy.globalaccess.widget.recyclerview.GridDecoration;

import java.util.Date;
import java.util.List;

import cn.msy.zc.commonutils.DisplayUtil;
import cn.msy.zc.commonutils.TimeFormat;

/**
 * Created by shawn on 2017/5/15 0015.
 * <p>
 * description : 旅游局-->团队地区统计-->(更多+行程日期的)适配器
 */

public class TourismDropListAdapter extends BaseMultiItemQuickAdapter<TourismDropListBean, BaseViewHolder> {

    private TourismPopManager.OnItemChildClickListener listener;
    private SparseIntArray lastSelectedPos = new SparseIntArray();
    private NestInteractListener nestInteractListener;
    private SparseArray<NestDropdownAdapter> adapters = new SparseArray<>();
    private OnNestInflateListener mNestInflateListener;
    private OnTimeDeleteCallback timeCallback;
    private Drawable icon_arrow_down;

    public TourismDropListAdapter(List<TourismDropListBean> data) {
        super(data);
        icon_arrow_down = App.getResource().getDrawable(R.mipmap.icon_arrow_down);
        icon_arrow_down.setBounds(0, 0, icon_arrow_down.getMinimumWidth(), icon_arrow_down.getMinimumHeight());
        //标题
        addItemType(TourismDropListBean.ViewType.TYPE_TITLE, R.layout.item_group_title);
        //行程日期
        addItemType(TourismDropListBean.ViewType.TYPE_TRAVEL_DATA, R.layout.item_dropdown_single_time_picker);
        //创建日期
        addItemType(TourismDropListBean.ViewType.TYPE_CREATER_TIME, R.layout.item_dropdown_single_time_picker);
        //认证日期
        addItemType(TourismDropListBean.ViewType.TYPE_AUTHENTICATION_TIME, R.layout.item_dropdown_double_time_picker);
        //确认按钮
        addItemType(TourismDropListBean.ViewType.TYPE_CONFIRM_BUTTON, R.layout.item_btn_only);
        //团队类型
        addItemType(TourismDropListBean.ViewType.TYPE_TEAM, R.layout.item_dropdown_nest_rv);
        //客源地
        addItemType(TourismDropListBean.ViewType.TYPE_GUEST_SOURCE, R.layout.item_dropdown_nest_rv);
        //团队状态
        addItemType(TourismDropListBean.ViewType.TYPE_TEAM_STATE, R.layout.item_dropdown_nest_rv);
    }

    public void addOnItemChildClickListener(TourismPopManager.OnItemChildClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, TourismDropListBean entity) {
        switch (entity.getItemType()) {
            case TourismDropListBean.ViewType.TYPE_AUTHENTICATION_TIME://认证日期
                String authBegin = getTime(entity.getData().get(0).getName(), TimeFormat.regular9, TimeFormat.regular7);
                String authEnd = getTime(entity.getData().get(1).getName(), TimeFormat.regular9, TimeFormat.regular7);
                String authBeginDetail = getTimeDetail(entity.getData().get(0).getName(), TimeFormat.regular9,
                        TimeFormat.regular8);
                String authEndDetail = getTimeDetail(entity.getData().get(1).getName(), TimeFormat.regular9,
                        TimeFormat.regular8);

                initTimeView(authBegin, R.id.tvTimeBegin, baseViewHolder, false);
                initTimeView(authEnd, R.id.tvTimeEnd, baseViewHolder, false);
                initTimeView(authBeginDetail, R.id.tvTimeBeginDetail, baseViewHolder, true);
                initTimeView(authEndDetail, R.id.tvTimeEndDetail, baseViewHolder, true);

                baseViewHolder
                        .setText(R.id.tvTimeBeginLabel, App.getResourceString(R.string.authentication_start))
                        .setText(R.id.tvTimeEndLabel, App.getResourceString(R.string.authentication_end))
                        .addOnClickListener(R.id.tvTimeBegin)
                        .addOnClickListener(R.id.tvTimeBeginDetail)
                        .addOnClickListener(R.id.tvTimeEnd)
                        .addOnClickListener(R.id.tvTimeEndDetail)
                        .addOnClickListener(R.id.tvDeleteStart)
                        .addOnClickListener(R.id.tvDeleteEnd);
                break;

            case TourismDropListBean.ViewType.TYPE_CREATER_TIME://创建日期
                String travelBegin = getTime(entity.getData().get(0).getName(),
                        TimeFormat.regular7, TimeFormat.regular7);

                String travelEnd = getTime(entity.getData().get(1).getName(), TimeFormat.regular7, TimeFormat.regular7);

                initTimeView(travelBegin, R.id.tvTimeBegin, baseViewHolder, false);
                initTimeView(travelEnd, R.id.tvTimeEnd, baseViewHolder, false);

                baseViewHolder
                        .setText(R.id.tvTimeBeginLabel, App.getResourceString(R.string.create_start))
                        .setText(R.id.tvTimeEndLabel, App.getResourceString(R.string.create_end))
                        .addOnClickListener(R.id.llTimeEnd)
                        .addOnClickListener(R.id.llTimeBegin);

                onStartDeleteClicked(baseViewHolder, TourismDropListBean.ViewType.TYPE_CREATER_TIME);
                onEndDeleteClicked(baseViewHolder, TourismDropListBean.ViewType.TYPE_CREATER_TIME);
                break;
            case TourismDropListBean.ViewType.TYPE_CONFIRM_BUTTON://确定按钮
                baseViewHolder.addOnClickListener(R.id.btnConfirm);
                break;
            case TourismDropListBean.ViewType.TYPE_TEAM://团队类型
            case TourismDropListBean.ViewType.TYPE_GUEST_SOURCE://客源地
            case TourismDropListBean.ViewType.TYPE_TEAM_STATE://团队状态
                baseViewHolder.addOnClickListener(R.id.tvAll);
                initNestRecyclerView(baseViewHolder, entity, entity.getItemType());
                break;
            case TourismDropListBean.ViewType.TYPE_TITLE://分组标题
                baseViewHolder.setText(R.id.tvGroupTitle, entity.getData().get(0).getName());
                break;
            case TourismDropListBean.ViewType.TYPE_TRAVEL_DATA://行程日期
                String beginTime = getTime(entity.getData().get(0).getName(), TimeFormat.regular7, TimeFormat.regular7);
                initTimeView(beginTime, R.id.tvTimeBegin, baseViewHolder, false);

                baseViewHolder
                        .setVisible(R.id.llTimeEnd, false)
                        .setText(R.id.tvTimeBeginLabel, App.getResourceString(R.string.travel_begin))
                        .setText(R.id.tvTimeEndLabel, App.getResourceString(R.string.travel_end))
                        .addOnClickListener(R.id.llTimeEnd)
                        .addOnClickListener(R.id.llTimeBegin);

                //删除开始时间
                onStartDeleteClicked(baseViewHolder, TourismDropListBean.ViewType.TYPE_TRAVEL_DATA);
                break;
        }
    }


    public void clearNestSelectedState(@TourismDropListBean.ViewType int viewType) {
        NestDropdownAdapter nestAdapter = adapters.get(viewType);
        int lastPos = lastSelectedPos.get(viewType);
        if (nestAdapter != null && lastPos >= 0 && lastPos < nestAdapter.getData().size()) {
            TourismDropListBean.DropSelectableBean valueBean = nestAdapter.getData().get(lastPos);
            valueBean.setSeleted(false);
            nestAdapter.notifyItemChanged(lastSelectedPos.get(viewType));
        }
        setNestSelectedItem(viewType, -1);
    }

    public void setNestInteractListener(NestInteractListener nestInteractListener) {
        this.nestInteractListener = nestInteractListener;
    }

    public void setNestSelectedItem(@TourismDropListBean.ViewType int viewType, int pos) {
        lastSelectedPos.put(viewType, pos);
        NestDropdownAdapter adapter = adapters.get(viewType);

        if (pos >= 0 && pos < adapter.getData().size()) {
            adapter.getData().get(pos).setSeleted(true);
            adapter.notifyItemChanged(pos);
        }
    }

    public void notifyNesteItem(int viewType) {
        adapters.get(viewType).notifyDataSetChanged();
    }

    public void setOnNestInflateListener(OnNestInflateListener listener) {
        this.mNestInflateListener = listener;
    }

    public void setOnTimeDeleteCallback(OnTimeDeleteCallback callBack) {
        timeCallback = callBack;
    }

    //****************************private*****************************************************
    private void initTimeView(String time, @IdRes int id, BaseViewHolder baseViewHolder, boolean isDetail) {

        AppCompatTextView tv = baseViewHolder.getView(id);
        tv.setText(time);
        if (time == null || TextUtils.isEmpty(time.trim())) {
            tv.setCompoundDrawables(null, null, null, null);
        } else {
            tv.setCompoundDrawables(null, null, icon_arrow_down, null);
        }

        float width = 0;
        if (isDetail) {
            width = tv.getPaint().measureText(TimeFormat.formatData(TimeFormat.regular8, new Date()));
        } else {
            width = tv.getPaint().measureText(TimeFormat.formatData(TimeFormat.regular7, new Date()));
        }
        width = width + tv.getPaddingLeft() + tv.getPaddingRight() + tv.getCompoundDrawablePadding() +
                icon_arrow_down.getMinimumWidth();
        tv.setMinimumWidth((int) width);
    }

    private void initNestRecyclerView(BaseViewHolder baseViewHolder, final TourismDropListBean entity,
                                      @TourismDropListBean.ViewType final int viewType) {

        RecyclerView rvTeamType = baseViewHolder.getView(R.id.rvTeamType);
        final View all = baseViewHolder.getView(R.id.tvAll);
        all.setSelected(true);
        NestDropdownAdapter nestAdapter = adapters.get(viewType);
        if (nestAdapter == null) {
            nestAdapter = new NestDropdownAdapter(entity.getData());
            rvTeamType.setLayoutManager(new GridLayoutManager(mContext, entity.getNestRvGridSpan(), GridLayoutManager
                    .VERTICAL, false));

            final NestDropdownAdapter finalNestAdapter = nestAdapter;
            nestAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    if (view.getId() == R.id.tvCell) {
                        clearNestSelectedState(viewType);
                        setNestSelectedItem(viewType, i);

                        if (listener != null) {
                            listener.onCallBack(finalNestAdapter.getData().get(i), entity.getItemType());
                        }

                        if (nestInteractListener != null) {
                            nestInteractListener.onNestedSelected(viewType);
                        }
                    }
                }
            });
            rvTeamType.setAdapter(nestAdapter);
            rvTeamType.setHasFixedSize(true);
            rvTeamType.addItemDecoration(new GridDecoration(DisplayUtil.dip2px(10)));
            ((SimpleItemAnimator) rvTeamType.getItemAnimator()).setSupportsChangeAnimations(false);
            adapters.put(viewType, nestAdapter);
            //初始化完毕后,通知manager,并将all按钮传递过去
            if (mNestInflateListener != null) {
                mNestInflateListener.onNestInflated(viewType, all);
            }
        } else {
            adapters.get(viewType).setNewData(entity.getData());
        }
    }

    //点击删除开始时间的按钮
    private void onStartDeleteClicked(final BaseViewHolder baseViewHolder,
                                      final @TourismDropListBean.ViewType int viewType) {
        baseViewHolder.getView(R.id.tvDeleteStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourismDropListBean bean = getDataByViewType(viewType);
                if (bean != null) {
                    bean.getData().set(0, new TourismDropListBean.DropSelectableBean(""));
                    notifyItemChanged(baseViewHolder.getLayoutPosition());
                }
                if (timeCallback != null) {
                    timeCallback.onDelete(viewType, OnTimeDeleteCallback.START_TIME, "");
                }
            }
        });
    }

    //点击删除结束时间的按钮
    private void onEndDeleteClicked(final BaseViewHolder baseViewHolder,
                                    final @TourismDropListBean.ViewType int viewType) {
        baseViewHolder.getView(R.id.tvDeleteEnd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TourismDropListBean bean = getDataByViewType(viewType);
                if (bean != null) {
                    bean.getData().set(1, new TourismDropListBean.DropSelectableBean(""));
                    notifyItemChanged(baseViewHolder.getLayoutPosition());
                }
                if (timeCallback != null) {
                    timeCallback.onDelete(viewType, OnTimeDeleteCallback.END_TIME, "");
                }
            }
        });
    }

    private String getTimeDetail(String time, String beforeRegular, String afterRegular) {
        if (time != null && !TextUtils.isEmpty(time.trim())) {
            return TimeFormat.transformData(time, beforeRegular, afterRegular);
        } else {
            return "";
        }
    }

    private String getTime(String time, String beforeRegular, String afterRegular) {
        if (time != null && !TextUtils.isEmpty(time.trim())) {
            return TimeFormat.transformData(time, beforeRegular, afterRegular);
        } else {
            return "";
        }
    }

    private TourismDropListBean getDataByViewType(@TourismDropListBean.ViewType int viewType) {
        List<TourismDropListBean> data = getData();
        TourismDropListBean target = null;
        for (TourismDropListBean temp : data) {
            if (temp.getItemType() == viewType) {
                target = temp;
                break;
            }
        }
        return target;
    }

    public interface NestInteractListener {
        void onNestedSelected(@TourismDropListBean.ViewType int viewType);
    }

    //渲染完毕后通知manager
    public interface OnNestInflateListener {
        void onNestInflated(@TourismDropListBean.ViewType int viewType, View btnAll);
    }

    public interface OnTimeDeleteCallback {
        int START_TIME = 0;
        int END_TIME = 1;

        void onDelete(@TourismDropListBean.ViewType int type, int timeType, String time);
    }
}
