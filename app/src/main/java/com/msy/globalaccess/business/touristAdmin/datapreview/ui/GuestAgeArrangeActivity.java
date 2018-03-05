package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.GuestAgeContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.GuestAgePresenterImpl;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.statistics.GuestAgeStatisticsBeanWrapper;
import com.msy.globalaccess.listener.IUpdateable;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.widget.chartview.CustomColumnChartView;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Viewport;

/**
 * Created by shawn on 2017/7/17 0017.
 * <p>
 * description : 游客年龄构成
 */

@SuppressWarnings("deprecation")
public class GuestAgeArrangeActivity extends BaseActivity implements IUpdateable, GuestAgeContract.View {
    @BindView(R.id.tvSelectedCondition)
    TextView tvSelectedCondition;
    @Inject
    GuestAgePresenterImpl mPresenter;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.statisticsView)
    CustomColumnChartView customColumnChartView;
    @BindView(R.id.split)
    View split;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    private Drawable arrowUp, arrowDown, left;

    //*****dialog*******
    private ArrayList<String> conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .guestAgeConditions)));
    private WheelViewDialog<String> chooserDialog;
    private int conditonPos;
    //------- dialog end -------

    public static void callActivity(Context ctx) {
        ctx.startActivity(new Intent(ctx, GuestAgeArrangeActivity.class));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guest_age_arrange;
    }

    @Override
    public void initInjector() {
        getActivityComponent().inject(this);
        basePresenter = mPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void init() {
        llTitle.setVisibility(View.GONE);
        split.setVisibility(View.GONE);
        setTitle(App.getResourceString(R.string.guestAgeArrangeTitle));
        arrowUp = App.getResource().getDrawable(R.mipmap.icon_arrow_up1);
        arrowUp.setBounds(0, 0, arrowUp.getIntrinsicWidth(), arrowUp.getIntrinsicHeight());
        left = App.getResource().getDrawable(R.mipmap.icon_filter);
        left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        arrowDown = App.getResource().getDrawable(R.mipmap.icon_arrow_down1);
        arrowDown.setBounds(0, 0, arrowDown.getIntrinsicWidth(), arrowDown.getIntrinsicHeight());

        initView();
        initDialog();
        initListener();
    }

    @Override
    public void update() {
        if (mPresenter != null) {
            mPresenter.onStart();
        }
    }

    @Override
    public void showProgress() {
        if (srl != null) {
            srl.setRefreshing(true);
        }
    }

    @Override
    public void hideProgress() {
        if (srl != null) {
            srl.setRefreshing(false);
        }
    }


    @Override
    public void onGuestAgeError(@ResultCode int resultCode, String errorMsg) {
        ToastUtils.showToast(errorMsg);
        customColumnChartView.setColumnChartData(null);
    }

    @Override
    public String[] getTimes() {
        return new String[]{getStartTime(), getEndTime()};
    }


    @Override
    public void handlerGuestAgeData(GuestAgeStatisticsBeanWrapper.ResultBean resultBean) {
        List<GuestAgeStatisticsBeanWrapper.GuestAgeStatisticsBean> data = resultBean.getOrigin();
        ColumnChartData columnChartData = resultBean.getColumnChartData();
        if (data == null || data.size() <= 0) {
            customColumnChartView.setColumnChartData(null);
            return;
        }
        int size = columnChartData.getColumns().size();
        if (size <= 5) {
            customColumnChartView.setScrollEnabled(false);
        } else {
            customColumnChartView.setScrollEnabled(true);
        }
        customColumnChartView.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.5f, 110, size, 0);
        customColumnChartView.setMaximumViewport(viewportMax);
        Viewport viewport = new Viewport(0, 110, size > 5 ? 5 : size, 0);
        customColumnChartView.setCurrentViewport(viewport);
        customColumnChartView.moveTo(0, 0);
        customColumnChartView.setVisibility(View.VISIBLE);
    }

    //***************************private **************************************

    private String getStartTime() {
        switch (conditonPos) {
            case 0://今日
                return TimeHelper.getTimeByStrategy(TimeHelper.TODAY, TimeFormat.regular7)[0];
            case 1://近一周
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_WEEK, TimeFormat.regular7)[0];
            case 2://近一月
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_MOHTH, TimeFormat.regular7)[0];
            case 3://近三月
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_THREE_MONTH, TimeFormat.regular7)[0];
            case 4://近六月
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_SIX_MONTH, TimeFormat.regular7)[0];
            case 5://近一年
                return TimeHelper.getTimeByStrategy(TimeHelper.LAST_YEAR, TimeFormat.regular7)[0];
        }
        Logger.e("条件异常");
        return "";
    }

    private String getEndTime() {
        return TimeFormat.getCurrentDate(TimeFormat.regular7);
    }

    private void initDialog() {
        if (conditions == null || conditions.size() == 0) {
            return;
        }
        chooserDialog = new WheelViewDialog<>(this, conditions, new WheelViewDialog
                .onWheelViewPickedListener1<String>() {
            @Override
            public void onPicked(String pickedItem, int position) {
                conditonPos = position;
                tvSelectedCondition.setText(pickedItem);
                if (mPresenter != null) {
                    mPresenter.onStart();
                }
            }
        });
        chooserDialog.setCanceledOnTouchOutside(true);
        tvSelectedCondition.setText(conditions.get(0));
    }

    private void initView() {
        srl.setEnabled(false);
        srl.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        //柱形图
        customColumnChartView.setValueTouchEnabled(false);
        customColumnChartView.setZoomEnabled(false);
        customColumnChartView.setScrollEnabled(true);
        customColumnChartView.setHorizontalScrollBarEnabled(true);
        customColumnChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
    }

    private void initListener() {
        tvSelectedCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooserDialog != null) {
                    chooserDialog.show();
                    tvSelectedCondition.setCompoundDrawables(left, null, arrowUp, null);
                }
            }
        });
        chooserDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tvSelectedCondition.setCompoundDrawables(left, null, arrowDown, null);
            }
        });
    }


}
