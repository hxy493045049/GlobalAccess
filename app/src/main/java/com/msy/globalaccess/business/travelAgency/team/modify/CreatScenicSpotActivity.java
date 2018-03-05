package com.msy.globalaccess.business.travelAgency.team.modify;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.travelAgency.team.contract.ScenicSpotContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.ScenicSpotPrsenterImpl;
import com.msy.globalaccess.data.bean.team.TeamLineInfoBean;
import com.msy.globalaccess.data.bean.scenic.TripScenicBean;
import com.msy.globalaccess.data.bean.ticket.TripScenicTicketBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.ScenicSpotSubmitDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.StringUtils;
import static com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity.REQUEST_CODE_EDIT;
import static com.msy.globalaccess.business.travelAgency.team.modify.TravelAttractionsActivity.TEAMLINEINFOBEAN;

/**
 * 景点相关安排
 * Created by chensh on 2017/3/15 0015.
 */

public class CreatScenicSpotActivity extends BaseActivity implements ScenicSpotContract.View {
    private final static String TAG = CreatScenicSpotActivity.class.getSimpleName();
    @BindView(R.id.scenic_spot_edit)
    EditText scenic_spot_remark;//备注信息
    @BindView(R.id.ticket_price_lin)
    LinearLayout ticket_price_lin;//票价信息
    @BindView(R.id.prepay)
    TextView prepay;//预付金额
    @BindView(R.id.scenic_spot_change)
    TextView scenic_spot_change;//改变景点按钮
    @BindView(R.id.scenic_spot_name)
    TextView scenic_spot_name;//景点
    @BindView(R.id.submit)
    TextView submit;//确定按钮
    @BindView(R.id.ticket_edit)
    LinearLayout ticket_edit;//订票Lin
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView AppCompatTextView;//标题

    @BindView(R.id.scenic_spot_man)
    TextView scenic_spot_man;//成人数量

    @BindView(R.id.scenic_spot_chiled)
    TextView scenic_spot_chiled;//儿童数量

    @Inject
    ScenicSpotPrsenterImpl presenter;
    public final static String TRIPSCENICBEAN = "tripScenicBean";
    public final static String isNEW = "isNew";
    public final static String IDORDER = "idOrder";
    public final static String ADULTSAMOUNT = "adultsAmount";
    public final static String CHILDRENAMOUT = "childrenAmout";
    public final static String isNewEDIT = "isNewEdit";

    private TripScenicBean tripScenicBean;//变更传过来的实体bean
    private boolean isNew = false;//编辑/新增
    private boolean isNewEdit = false;//新增编辑
    private int people; //总人数
    @BindView(R.id.ticket_info)
    LinearLayout ticket_info;//订票信息

    @BindView(R.id.jiesuan)
    TextView jiesuan;//结算方式

    private String RemarkString = "";

    /**
     * 行程信息
     */
    private TeamLineInfoBean teamLineInfoBean;
    /**
     * 团队详情信息
     */

    private boolean idOrder = false;//是否被预约
    /**
     * loading图
     */
    private SmallDialog smallDialog;
    /**
     * 是否支持预定
     */
    private boolean isPreOrder = false;
    private int areaAdultsAmount;//成人数量
    private int areaChildrenAmout;//儿童数量
    private boolean isChangede = true;//数据是否有变更

    /**
     * 新增景点
     *
     * @param activity         上下文
     * @param isnew            是否新增
     * @param tripScenicBean   传递景点变更实体类
     * @param teamLineInfoBean 团队详情线路信息
     */
    public static void callActivity(Activity activity, boolean isnew, TripScenicBean tripScenicBean, TeamLineInfoBean teamLineInfoBean, int childrenAmout, int adultsAmount) {
        Intent intent = new Intent(activity, CreatScenicSpotActivity.class);
        intent.putExtra(isNEW, isnew);
        intent.putExtra(TRIPSCENICBEAN, tripScenicBean);
        intent.putExtra(TEAMLINEINFOBEAN, teamLineInfoBean);
        intent.putExtra(ADULTSAMOUNT, adultsAmount);
        intent.putExtra(CHILDRENAMOUT, childrenAmout);
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 编辑景点
     *
     * @param context        上下文
     * @param tripScenicBean 传递实体类
     * @param isnew          是否新增
     */
    public static void callActivity(Context context, TripScenicBean tripScenicBean, boolean isnew) {
        Intent intent = new Intent(context, CreatScenicSpotActivity.class);
        intent.putExtra(TRIPSCENICBEAN, tripScenicBean);
        intent.putExtra(isNEW, isnew);
        context.startActivity(intent);
    }

    /**
     * 编辑新增景点
     *
     * @param context        上下文
     * @param tripScenicBean 传递实体类
     * @param isnew          是否新增
     */
    public static void callActivityForResult(Activity context, TripScenicBean tripScenicBean, boolean isnew, boolean idOrder, int adultsAmount, int childrenAmout, boolean isNewEdit) {
        Intent intent = new Intent(context, CreatScenicSpotActivity.class);
        intent.putExtra(TRIPSCENICBEAN, tripScenicBean);
        intent.putExtra(isNEW, isnew);
        intent.putExtra(isNewEDIT, isNewEdit);
        intent.putExtra(IDORDER, idOrder);
        intent.putExtra(ADULTSAMOUNT, adultsAmount);
        intent.putExtra(CHILDRENAMOUT, childrenAmout);
        context.startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    /**
     * 编辑景点
     *
     * @param context        上下文
     * @param tripScenicBean 传递实体类
     * @param isnew          是否新增
     */
    public static void callActivityForResult(Activity context, TripScenicBean tripScenicBean, boolean isnew, boolean idOrder, int adultsAmount, int childrenAmout) {
        Intent intent = new Intent(context, CreatScenicSpotActivity.class);
        intent.putExtra(TRIPSCENICBEAN, tripScenicBean);
        intent.putExtra(isNEW, isnew);
        intent.putExtra(IDORDER, idOrder);
        intent.putExtra(ADULTSAMOUNT, adultsAmount);
        intent.putExtra(CHILDRENAMOUT, childrenAmout);
        context.startActivityForResult(intent, REQUEST_CODE_EDIT);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_scenic_spot;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = presenter;
    }

    private void initIntent() {
        if (getIntent().hasExtra(isNEW)) {
            isNew = getIntent().getBooleanExtra(isNEW, false);
        }
        if (getIntent().hasExtra(isNewEDIT)) {
            isNewEdit = getIntent().getBooleanExtra(isNewEDIT, false);
        }
        if (getIntent().hasExtra(TRIPSCENICBEAN)) {
            tripScenicBean = getIntent().getParcelableExtra(TRIPSCENICBEAN);
        }
        if (getIntent().hasExtra(TEAMLINEINFOBEAN)) {
            teamLineInfoBean = getIntent().getParcelableExtra(TEAMLINEINFOBEAN);
        }
        if (getIntent().hasExtra(TRIPSCENICBEAN)) {
            tripScenicBean = getIntent().getParcelableExtra(TRIPSCENICBEAN);
        }
        if (getIntent().hasExtra(IDORDER)) {
            idOrder = getIntent().getBooleanExtra(IDORDER, false);
        }
        if (getIntent().hasExtra(CHILDRENAMOUT)) {
            areaChildrenAmout = getIntent().getIntExtra(CHILDRENAMOUT, 0);
        }
        if (getIntent().hasExtra(ADULTSAMOUNT)) {
            areaAdultsAmount = getIntent().getIntExtra(ADULTSAMOUNT, 0);
        }
    }

    @Override
    protected void init() {
        initIntent();
        smallDialog = new SmallDialog(this, getResources().getString(R.string.small_dialog));
        AppCompatTextView.setVisibility(View.VISIBLE);
        //设置备注触摸事件
        scenic_spot_remark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof EditText) {
                    EditText et = (EditText) v;
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (et.getLineHeight() * et.getLineCount() > et.getHeight()) {
                            et.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                return false;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClick();
            }
        });
        //是否支持预定
        if (tripScenicBean != null) {
            isPreOrder = tripScenicBean.getIsOrderTicket().equals("0");
//            ticketList = tripScenicBean.getTripScenicTicket();
            RemarkString = tripScenicBean.getRemark();
        }
        //已预订时备注信息不可编辑/确定按钮置灰
        if (idOrder) {
            scenic_spot_remark.setEnabled(false);
            scenic_spot_remark.setFocusable(false);
            submit.setBackgroundColor(getResources().getColor(R.color.border_color));
        }
        //显示选择景点按钮
        if (isNew) {
            if (!isNewEdit) {
                AppCompatTextView.setText(R.string.scenic_new_title);
                scenic_spot_change.setVisibility(View.VISIBLE);
            } else {
                AppCompatTextView.setText(R.string.scenic_title);
                scenic_spot_change.setVisibility(View.GONE);
            }
            //编辑
            if (tripScenicBean != null) {
                scenic_spot_name.setText(tripScenicBean.getTripName());
                initView();

            }
            //新增时获取门票信息
            if (teamLineInfoBean != null) {
                if (!isPreOrder) {
                    presenter.submit(teamLineInfoBean.getTripDate(), tripScenicBean.getScenicId());
                }
            }

        } else {
            AppCompatTextView.setText(R.string.scenic_title);
            scenic_spot_change.setVisibility(View.GONE);
            initView();
        }
        initTicket();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        if (tripScenicBean != null) {
            scenic_spot_name.setText(tripScenicBean.getTripName());
            scenic_spot_man.setText("成人(" + areaAdultsAmount + ")");
            scenic_spot_chiled.setText("儿童(" + areaChildrenAmout + ")");
            String PrePayMoney = "￥" + (StringUtils.isEmpty(tripScenicBean.getPrePayMoney()) ? "0" : StringUtils.getMsyPrice(tripScenicBean.getPrePayMoney()));
            prepay.setText(PrePayMoney);
            if (tripScenicBean.getAuditType() != null && tripScenicBean.getAuditType().equals("0")) {
                jiesuan.setText("不结算");
            } else if (tripScenicBean.getAuditType() != null && tripScenicBean.getAuditType().equals("1")) {
                jiesuan.setText("实时结算");
            } else {
                jiesuan.setText("");
            }
            //不支持预定  0
            if (isPreOrder) {
                ticket_info.setVisibility(View.GONE);
            } else {
                ticket_info.setVisibility(View.VISIBLE);
            }
            if (!StringUtils.isEmpty(tripScenicBean.getRemark())) {
                scenic_spot_remark.setText(tripScenicBean.getRemark());
            }
        }
    }

    /**
     * 初始化订票信息布局
     */
    private void initTicket() {

        if (tripScenicBean != null && tripScenicBean.getTripScenicTicket() != null && tripScenicBean.getTripScenicTicket().size() > 0) {
            ticket_info.setVisibility(View.VISIBLE);
            if (ticket_info.getVisibility() == View.GONE) {
                ticket_info.setVisibility(View.VISIBLE);
            }
            List<TripScenicTicketBean> list = tripScenicBean.getTripScenicTicket();
            ticket_edit.removeAllViews();
            ticket_price_lin.removeAllViews();
            for (int i = 0; i < list.size(); i++) {
                TripScenicTicketBean tripScenicTicketBean = list.get(i);
                //价格
                RelativeLayout priceLin = (RelativeLayout) getLayoutInflater().inflate(R.layout.scenic_price, null);
                TextView price = (TextView) priceLin.findViewById(R.id.price);
                price.setText(tripScenicTicketBean.getTicketPriceName() + ": ￥" + StringUtils.getMsyPrice(tripScenicTicketBean.getPrice()) + "/人");
                //订票数量
                LinearLayout scenic_dynamics = (LinearLayout) getLayoutInflater().inflate(R.layout.scenic_dynamics, null);
                scenic_dynamics.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                TextView ticketName = (TextView) scenic_dynamics.findViewById(R.id.ticket_name);
                final EditText edittext = (EditText) scenic_dynamics.findViewById(R.id.ticket_edittext);
                //根据票种添加票种类型
                ticketName.setText(tripScenicTicketBean.getTicketPriceName());
                //添加团队里的票种人数，在edit中提示
                edittext.setText(String.valueOf(StringUtils.stringConvertInt(tripScenicTicketBean.getAmount())));
                if (idOrder) {
                    edittext.setEnabled(false);
                    edittext.setFocusable(false);
                } else {
                    edittext.setSelection(edittext.getText().length());
                }
                edittext.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        prepay.setText("￥" + StringUtils.getMsyPrice(String.valueOf(getChildCount())));
                    }
                });


                ticket_edit.addView(scenic_dynamics);
                ticket_price_lin.addView(priceLin);
            }
        } else {
            ticket_info.setVisibility(View.GONE);
        }

    }


    @OnClick({R.id.submit, R.id.scenic_spot_change})
    public void setSubmit(View view) {
        switch (view.getId()) {
            case R.id.submit:
                //已预定不可点击
                if (idOrder) {
                    return;
                }
                //判断是否有门票信息
                if (!isPreOrder && tripScenicBean != null && tripScenicBean.getScenicId() != null && !tripScenicBean.getScenicId().equals("")) {
                    if (tripScenicBean == null || tripScenicBean.getTripScenicTicket() == null || tripScenicBean.getTripScenicTicket().size() == 0) {
                        ToastUtils.showToast(R.string.scenic_ticket_nothing);
                        return;
                    }
                }
                //钱数
                final double money = getChildCount();
                if (people > (areaAdultsAmount + areaChildrenAmout)) {
                    ToastUtils.showToast(R.string.edit_scenic_more_people);
                    return;
                }
                ScenicSpotSubmitDialog.Builder builder = new ScenicSpotSubmitDialog.Builder(CreatScenicSpotActivity.this);
                if (isPreOrder) {
                    builder.setMessage(R.string.edit_scenic_save_info);
                } else {
                    builder.setMessage(R.string.edit_scenic_save_tiket_info);
                }
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.edit_scenic_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getticket();
                        tripScenicBean.setPrePayMoney(String.valueOf(money));
                        if (isNew) {
                            tripScenicBean.setAdultsAmount(String.valueOf(areaAdultsAmount));
                            tripScenicBean.setChildrenAmout(String.valueOf(areaChildrenAmout));
                            tripScenicBean.setChangeType("2");
                        } else {
                            if (!isChangede || !RemarkString.equals(tripScenicBean.getRemark())) {
                                if (tripScenicBean.getChangeType() != null &&!tripScenicBean.getChangeType().equals("2") ) {
                                    tripScenicBean.setChangeType("3");
                                }
                            }
                        }
                        tripScenicBean.setDelTag("0");
                        Intent intent = new Intent();
                        intent.putExtra(TRIPSCENICBEAN, tripScenicBean);
                        setResult(RESULT_OK, intent);
                        CreatScenicSpotActivity.this.finish();
                    }
                });
                builder.setNegativeButton(R.string.edit_scenic_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                PopUpWindowAlertDialog dialog = builder.create();
                if (!isPreOrder) {
                    if (people < (areaAdultsAmount + areaChildrenAmout)) {
                        builder.ShowNumberView();
                    } else {
                        builder.GoneNumberView();
                    }
                    builder.showPrepayLin(StringUtils.getMsyPrice(String.valueOf(money)));
                } else {
                    //不支持预定
                    builder.GoneNumberView();
                    builder.GonePrepayLin();
                }
                dialog.show();
                break;
            case R.id.scenic_spot_change:   //选择景点
                finish();
                break;
        }

    }


    /**
     * 获取订票数量
     */
    private void getticket() {
        if (ticket_edit.getChildCount() > 0) {
            for (int i = 0; i < ticket_edit.getChildCount(); i++) {
                EditText lin = (EditText) ticket_edit.getChildAt(i).findViewById(R.id.ticket_edittext);
                String text = lin.getText().toString();
                TripScenicTicketBean tripScenicTicketBean = tripScenicBean.getTripScenicTicket().get(i);

                if (!StringUtils.isEmpty(text)) {
                    if (!text.equals( tripScenicTicketBean.getAmount())) {
                        isChangede = false;
                    }
                    if (isNew) {
                        tripScenicTicketBean.setChangeType("2");
                    } else {
                        if (! text.equals(tripScenicTicketBean.getAmount())) {
                            if (tripScenicTicketBean.getChangeType() != null && !tripScenicTicketBean.getChangeType().equals("2")) {
                                tripScenicTicketBean.setChangeType("3");
                            }
                        }
                    }
                    tripScenicTicketBean.setAmount(text);
                } else {
                    tripScenicTicketBean.setAmount("0");
                }
            }
        }
        tripScenicBean.setRemark(scenic_spot_remark.getText().toString());
    }


    /**
     * 在点击返回的时候判断是否被修改
     *
     * @return 布尔
     */
    private boolean isEdit() {
        boolean isEdit = false;
        if (ticket_edit.getChildCount() > 0) {
            for (int i = 0; i < ticket_edit.getChildCount(); i++) {
                EditText lin = (EditText) ticket_edit.getChildAt(i).findViewById(R.id.ticket_edittext);
                String text = lin.getText().toString();
                TripScenicTicketBean tripScenicTicketBean = tripScenicBean.getTripScenicTicket().get(i);

                if (!StringUtils.isEmpty(text)) {
                    if (! text.equals(tripScenicTicketBean.getAmount())) {
                        isEdit = true;
                    }
                }
            }
        }
        return isEdit;
    }

    /**
     * 获取  总金额
     *
     * @return 金额
     */
    private double getChildCount() {
        double count = 0;
        if (ticket_edit.getChildCount() > 0) {
            people = 0;
            for (int i = 0; i < ticket_edit.getChildCount(); i++) {
                EditText lin = (EditText) ticket_edit.getChildAt(i).findViewById(R.id.ticket_edittext);
                String text = lin.getText().toString();
                if (!StringUtils.isEmpty(text)) {
                    people = people + Integer.valueOf(lin.getText().toString());
                    count = count + Integer.valueOf(lin.getText().toString()) * Double.parseDouble(tripScenicBean.getTripScenicTicket().get(i).getPrice());
                }
            }
        }
        return count;
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    /**
     * 返回dialog提示
     */
    private void backClick() {
        if (isNew && !isNewEdit) {
            backdialog();
        } else {
            if (!idOrder) {
                if (isEdit()) {
                    backdialog();
                } else {
                    setResult(RESULT_CANCELED);
                    CreatScenicSpotActivity.this.finish();
                }
            } else {
                setResult(RESULT_CANCELED);
                CreatScenicSpotActivity.this.finish();
            }
        }
    }

    private void backdialog() {
        ScenicSpotSubmitDialog.Builder builder = new ScenicSpotSubmitDialog.Builder(this);
        builder.setMessage("您当前处于编辑状态，确认放弃本次操作");
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.edit_scenic_sure, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_CANCELED);
                CreatScenicSpotActivity.this.finish();
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
    public void showProgress() {
        if (smallDialog != null) {
            smallDialog.shows();
        }
    }

    @Override
    public void hideProgress() {
        if (smallDialog != null) {
            smallDialog.dismisss();
        }
    }


    @Override
    public void getTicketInfo(ArrayList<TripScenicTicketBean> data) {
        if (data != null && data.size() > 0) {
            tripScenicBean.setTripScenicTicket(data);
            initTicket();
        }
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v     getCurrentFocus  View
     * @param event MotionEvent
     * @return 是否隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token getWindowToken
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
