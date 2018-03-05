package com.msy.globalaccess.business.travelAgency.team.guider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.data.bean.team.TeamBaseInfoBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.DateTimePickDialog;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.ScenicSpotSubmitDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.StringUtils;
import cn.msy.zc.commonutils.TimeFormat;

import static com.msy.globalaccess.business.travelAgency.team.guider.GuideListActivity.KEY_GUIDER_EDIT;

/**
 * 编辑导游界面
 * Created by shawn on 2017/3/20 0020.
 * <p>
 * description : 导游编辑页面
 */
public class EditGuiderActivity extends BaseActivity {

    public final static String DETAILBEAN = "detailBean";
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;
    @BindView(R.id.tvDelegateBeginDay)
    AppCompatTextView tvDelegateBeginDay;
    @BindView(R.id.tvDelegateBeginHour)
    AppCompatTextView tvDelegateBeginHour;
    @BindView(R.id.tvDelegateEndDay)
    AppCompatTextView tvDelegateEndDay;
    @BindView(R.id.tvDelegateEndHour)
    AppCompatTextView tvDelegateEndHour;
    @BindView(R.id.guider_submit)
    AppCompatTextView submit;
    private GuiderListBean guiderBean;//变更导游实体类
    private TeamDetailBean detailBean;//团队详情实体类
    private Date UserBeginDateTime;//用户选择时间范围 开始时间
    private Date UserEndDateTime;//用户选择时间范围 结束时间

    private String oldBeginDateTime;//限制范围 开始时间
    private String oldEndDateTime;//限制范围 结束时间

    public static void callActivity(GuiderListBean bean, TeamDetailBean detailBean, Activity ctx, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(ctx, EditGuiderActivity.class);
        intent.putExtra(KEY_GUIDER_EDIT, bean);
        intent.putExtra(DETAILBEAN, detailBean);
        ctx.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_guider;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {
        tvToolbarCenter.setVisibility(View.VISIBLE);
        tvToolbarCenter.setText(App.getResourceString(R.string.guider_edit_title));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(KEY_GUIDER_EDIT)) {
                guiderBean = bundle.getParcelable(KEY_GUIDER_EDIT);
            }
            if (bundle.containsKey(DETAILBEAN)) {
                detailBean = bundle.getParcelable(DETAILBEAN);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClick();
            }
        });
        if (detailBean != null && detailBean.getTeamBaseInfo() != null) {
            TeamBaseInfoBean infoBean = detailBean.getTeamBaseInfo();
            //开始时间
            //yyyy-MM-dd
            String oldBeginDate = TimeFormat.transformData(infoBean.getDepartureInfo(), TimeFormat.regular9,
                    TimeFormat.regular7);
            //HH:mm
            String oldBeginTime = TimeFormat.transformData(infoBean.getDepartureInfo(), TimeFormat.regular9,
                    TimeFormat.regular8);
            //结束时间
            //yyyy-MM-dd
            String oldEndDate = TimeFormat.transformData(infoBean.getReturnInfo(), TimeFormat.regular9,
                    TimeFormat.regular7);
            //HH:mm
            String oldEndTime = TimeFormat.transformData(infoBean.getReturnInfo(), TimeFormat.regular9,
                    TimeFormat.regular8);
            //开始时间
            oldBeginDateTime = oldBeginDate + " " + oldBeginTime;
            //结束时间
            oldEndDateTime = oldEndDate + " " + oldEndTime;
        }

        if (guiderBean != null) {
            // 填充开始时间
            tvDelegateBeginDay.setText(TimeFormat.transformData(guiderBean.getAppointStartDate(), TimeFormat.regular9,
                    TimeFormat.regular7));

            tvDelegateBeginHour.setText(TimeFormat.transformData(guiderBean.getAppointStartDate(), TimeFormat.regular9,
                    TimeFormat.regular8));
            // 填充结束时间
            tvDelegateEndDay.setText(TimeFormat.transformData(guiderBean.getAppointEndDate(), TimeFormat.regular9,
                    TimeFormat.regular7));
            tvDelegateEndHour.setText(TimeFormat.transformData(guiderBean.getAppointEndDate(), TimeFormat.regular9,
                    TimeFormat.regular8));
            //获取用户选择时间
            SimpleDateFormat CurrentTime = new SimpleDateFormat(TimeFormat.regular9, Locale.getDefault());
            try {
                UserBeginDateTime = CurrentTime.parse(tvDelegateBeginDay.getText().toString() + " " + tvDelegateBeginHour
                        .getText().toString());
                UserEndDateTime = CurrentTime.parse(tvDelegateEndDay.getText().toString() + " " + tvDelegateEndHour.getText
                        ().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        onclick(tvDelegateBeginDay, true);
        onclick(tvDelegateBeginHour, false);
        onclick(tvDelegateEndDay, true);
        onclick(tvDelegateEndHour, false);
    }

    @OnClick(R.id.guider_submit)
    public void guiderSubmit() {
        // 做比较
        SimpleDateFormat CurrentTime = new SimpleDateFormat(TimeFormat.regular9, Locale.getDefault());
        try {
            //限制范围  开始时间-结束时间
            Date oldBeiginTime = CurrentTime.parse(oldBeginDateTime);
            Date oldEndTime = CurrentTime.parse(oldEndDateTime);
            //选择的 开始时间-结束时间
            Date beiginTime = CurrentTime.parse(tvDelegateBeginDay.getText().toString() + " " + tvDelegateBeginHour
                    .getText().toString());
            Date endTime = CurrentTime.parse(tvDelegateEndDay.getText().toString() + " " + tvDelegateEndHour.getText
                    ().toString());

            //开始时间不能大于结束时间
            if ((beiginTime.getTime() - endTime.getTime()) > 0) {
                ToastUtils.showToast(App.getResourceString(R.string.guider_invalid_time));
                return;
            }
            //比较时间
            if ((beiginTime.getTime() - oldBeiginTime.getTime()) >= 0 && (oldEndTime.getTime() - endTime.getTime())
                    >= 0) {
                if (guiderBean != null) {
                    SimpleDateFormat format = new SimpleDateFormat(TimeFormat.regular9, Locale.getDefault());
                    Intent intent = new Intent(EditGuiderActivity.this, GuideListActivity.class);
                    guiderBean.setAppointStartDate(format.format(beiginTime));
                    guiderBean.setAppointEndDate(format.format(endTime));
                    intent.putExtra(KEY_GUIDER_EDIT, guiderBean);
                    setResult(RESULT_OK, intent);
                }
                finish();
            } else {
                String message = "委派时间应该在行程日期" + oldBeginDateTime + "至" + oldEndDateTime + "之间";
                PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
                builder.setMessage(message, 18);
                builder.setTitle(null, 0);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     *
     * @param inputView    View
     * @param isPickdialog 区分日期/时间
     */
    private void onclick(final AppCompatTextView inputView, boolean isPickdialog) {
        int timeType;
        if (isPickdialog) {
            timeType = DateTimePickDialog.TYPE_SIMPLIFY;
        } else {
            timeType = DateTimePickDialog.TYPE_TIME;
        }
        final int finalTimeType = timeType;
        inputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimePickDialog dateTimePickDialog;
                if (!StringUtils.isEmpty(inputView.getText().toString())) {
                    dateTimePickDialog = new DateTimePickDialog(EditGuiderActivity.this, inputView.getText().toString
                            (), finalTimeType);
                } else {
                    dateTimePickDialog = new DateTimePickDialog(EditGuiderActivity.this, finalTimeType);
                }
                dateTimePickDialog.buildDialog(inputView);
                dateTimePickDialog.show();
            }
        });
    }

    /**
     * dialog
     */
    private void backdialog() {
        ScenicSpotSubmitDialog.Builder builder = new ScenicSpotSubmitDialog.Builder(this);
        builder.setMessage("您当前处于编辑状态，确认放弃本次操作");
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.edit_scenic_sure, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditGuiderActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.edit_scenic_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        PopUpWindowAlertDialog dialog = builder.create();
        builder.GoneNumberView();
        builder.GonePrepayLin();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    /**
     * 判断时间
     */
    private void backClick() {
        if (UserBeginDateTime != null && UserEndDateTime != null) {
            SimpleDateFormat CurrentTime = new SimpleDateFormat(TimeFormat.regular9, Locale.getDefault());
            try {
                //获取用户修改后的时间
                Date beiginTime = CurrentTime.parse(tvDelegateBeginDay.getText().toString() + " " + tvDelegateBeginHour
                        .getText().toString());
                Date endTime = CurrentTime.parse(tvDelegateEndDay.getText().toString() + " " + tvDelegateEndHour.getText
                        ().toString());
                //对时间修改前后进行修改
                if (UserBeginDateTime.getTime() != beiginTime.getTime() || UserEndDateTime.getTime() != endTime.getTime()) {
                    backdialog();
                } else {
                    EditGuiderActivity.this.finish();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                EditGuiderActivity.this.finish();
            }
        } else {
            EditGuiderActivity.this.finish();
        }
    }
}
