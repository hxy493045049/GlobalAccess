package com.msy.globalaccess.business.touristAdmin.datapreview.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.ScenicAuthTimeContract;
import com.msy.globalaccess.business.touristAdmin.datapreview.contract.impl.ScenicAuthTimePresenter;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.bean.statistics.ScenicCheckTimeCountListWrapper;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.TimeFormat;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

import static com.msy.globalaccess.utils.helper.TimeHelper.TODAY;


/**
 * Created by shawn on 2017/7/10 0010.
 * <p>
 * description : 景区认证时间分布
 */
@SuppressWarnings("deprecation")
public class AuthTimeDistributionActivity extends BaseActivity implements ScenicAuthTimeContract.View {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @Inject
    ScenicAuthTimePresenter mPresenter;
    @BindView(R.id.llTvScenicSpotTime)
    LinearLayout llTvScenicSpotTime;
    @BindView(R.id.split)
    View split;
    @BindView(R.id.llTvScenicSpotName)
    LinearLayout llTvScenicSpotName;
    @BindView(R.id.ccv_guide)
    ColumnChartView authTimeView;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    //********filter1******************
    @BindView(R.id.tvScenicSpotName)
    TextView tvScenicSpotName;
    //********filter1 -- end******************

    // ******************filter 2******************
    @BindView(R.id.tvScenicSpotTime)
    TextView tvScenicSpotTime;
    // ******************filter 2-- end******************
    ArrayList<String> conditions = new ArrayList<>(Arrays.asList(App.getResource().getStringArray(R.array
            .scenicAuthTimeConditions)));
    //*****************dialog*********************
    //时间选中框
    private WheelViewDialog timePickDialog;
    //景点下拉框
    private WheelViewDialog<ScenicListBean> scenicSpotDialog;
    private String[] scenics = App.getResource().getStringArray(R.array.authScenicItem);
    private List<ScenicListBean> scenicListBeen = new ArrayList<>();
    //*****************dialog end*********************

    private Drawable arrowDown, arrowUp;

    @TimeHelper.TimeStrategy
    private int timeStrategy = TODAY;

    private ScenicListBean selectedScenic;

    public static void callActivity(Context ctx) {
        Intent intent = new Intent(ctx, AuthTimeDistributionActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_more_auth_time;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
        basePresenter = mPresenter;
    }

    @Override
    protected void init() {
        setTitle(App.getResourceString(R.string.auth_time_title));
        srl.setEnabled(false);
        srl.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        llTitle.setVisibility(View.GONE);
        split.setVisibility(View.GONE);
        tvUnit.setText(App.getResourceString(R.string.auth_time_unit));
        arrowDown = getResources().getDrawable(R.mipmap.icon_arrow_down1);
        arrowDown.setBounds(0, 0, arrowDown.getIntrinsicWidth(), arrowDown.getIntrinsicHeight());

        arrowUp = getResources().getDrawable(R.mipmap.icon_arrow_up1);
        arrowUp.setBounds(0, 0, arrowUp.getIntrinsicWidth(), arrowUp.getIntrinsicHeight());
        initDialog();
        initListener();
        initColumnView();
    }


    @Override
    public void showProgress() {
        srl.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        srl.setRefreshing(false);
    }

    /**
     * 显示景区认证时间分布饼形图
     */
    @Override
    public void showScenicAuthTimeData(List<ScenicCheckTimeCountListWrapper.ScenicCheckTimeCountList> data,
                                       ColumnChartData columnChartData) {
        if (data == null || data.size() <= 0) {
            ToastUtils.showToast("暂无数据");
            authTimeView.setColumnChartData(null);
            return;
        }
        if (data.size() > 5) {
            authTimeView.setScrollEnabled(true);
        } else {
            authTimeView.setScrollEnabled(false);
        }
        int size = columnChartData.getColumns().size();
        authTimeView.setColumnChartData(columnChartData);
        Viewport viewportMax = new Viewport(-0.5f, authTimeView.getMaximumViewport().height() * 1.25f, size, 0);
        authTimeView.setMaximumViewport(viewportMax);
        Viewport viewport = new Viewport(0, authTimeView.getMaximumViewport().height() * 1.25f, size > 5 ? 5 :
                size, 0);
        authTimeView.setCurrentViewport(viewport);
        authTimeView.moveTo(0, 0);
        authTimeView.setVisibility(View.VISIBLE);
    }


    @Override
    public String[] getTimes() {
        return TimeHelper.getTimeByStrategy(timeStrategy, TimeFormat.regular7);
    }

    @Override
    public String getScenicId() {
        if (selectedScenic != null) {
            return selectedScenic.getScenicId();
        }
        return "";
    }

    @Override
    public void onScenicAuthTimeError(@ResultCode int resultCode, String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }


    //*******************************private ****************************************
    private void initListener() {
        Filter1 click1 = new Filter1();
        tvScenicSpotName.setOnClickListener(click1);
        llTvScenicSpotName.setOnClickListener(click1);

        Filter2 click2 = new Filter2();
        llTvScenicSpotTime.setOnClickListener(click2);
        tvScenicSpotTime.setOnClickListener(click2);
    }

    private void initDialog() {
        for (String str : scenics) {
            String[] s = str.split("_");
            ScenicListBean bean = new ScenicListBean();

            bean.setScenicName(s[0]);
            if (s[0].equals("全部")) {
                bean.setScenicId("");
            } else {
                bean.setScenicId(s[1]);
            }
            scenicListBeen.add(bean);
        }

        scenicSpotDialog = new WheelViewDialog<>(this, scenicListBeen, new WheelViewDialog
                .onWheelViewPickedListener1<ScenicListBean>() {
            @Override
            public void onPicked(ScenicListBean pickedItem, int position) {
                AuthTimeDistributionActivity.this.selectedScenic = pickedItem;
                selectedScenic = pickedItem;
                if (TextUtils.isEmpty(selectedScenic.getScenicId())) {
                    tvScenicSpotName.setText("景点");
                } else {
                    tvScenicSpotName.setText(selectedScenic.getScenicName());
                }
                if (mPresenter != null) {
                    mPresenter.getScnicAuthTimeStatisticsData();
                }
            }
        });
        scenicSpotDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tvScenicSpotName.setCompoundDrawables(null, null, arrowDown, null);
            }
        });

        timePickDialog = new WheelViewDialog<>(this, conditions, new WheelViewDialog
                .onWheelViewPickedListener1<String>() {
            @Override
            public void onPicked(String pickedItem, int position) {
                tvScenicSpotTime.setText(pickedItem);
                timeStrategy = position;
                if (mPresenter != null) {
                    mPresenter.getScnicAuthTimeStatisticsData();
                }
            }
        });
        timePickDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                tvScenicSpotTime.setCompoundDrawables(null, null, arrowUp, null);
            }
        });
        timePickDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                tvScenicSpotTime.setCompoundDrawables(null, null, arrowDown, null);
            }
        });
    }

    private void initColumnView() {
        //柱形图
        authTimeView.setValueTouchEnabled(false);
        authTimeView.setZoomEnabled(false);
        authTimeView.setScrollEnabled(true);
        authTimeView.setHorizontalScrollBarEnabled(true);
        authTimeView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
    }

    private class Filter1 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (scenicSpotDialog != null && !scenicSpotDialog.isShowing()) {
                scenicSpotDialog.show();
                tvScenicSpotName.setCompoundDrawables(null, null, arrowUp, null);
            }
        }
    }

    private class Filter2 implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (timePickDialog != null) {
                timePickDialog.show();
                tvScenicSpotTime.setCompoundDrawables(null, null, arrowUp, null);
            }
        }
    }

}
