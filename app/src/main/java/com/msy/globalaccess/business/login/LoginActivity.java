package com.msy.globalaccess.business.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.login.impl.LoginPresenterImpl;
import com.msy.globalaccess.business.main.ui.AgentMainActivity;
import com.msy.globalaccess.business.main.ui.TourismMainActivity;
import com.msy.globalaccess.common.UserRoleType;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.StringUtils;

/**
 * 登录
 * Created by chensh on 2017/1/20 0020.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {
    @Inject
    LoginPresenterImpl Presenter;
    @BindView(R.id.logion_name)
    EditText name;
    @BindView(R.id.logion_pwd)
    EditText pwd;
    @BindView(R.id.login_submit)
    Button submit;
    @BindView(R.id.activity_login_close)
    ImageView close;
    @BindView(R.id.appCompatImageView)
    AppCompatImageView appCompatImageView;

    private SmallDialog smallDialog;

    private String loginUserType = "";

    public static final String LOGINUSERTYPE = "loginUserType";

    public static void callActivity(Context context, String loginUserType) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LOGINUSERTYPE, loginUserType);
        context.startActivity(intent);
    }

    private void getIntentData() {
        if (getIntent().hasExtra(LOGINUSERTYPE)) {
            loginUserType= getIntent().getStringExtra(LOGINUSERTYPE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        Presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = Presenter;
    }

    @Override
    protected void init() {
        smallDialog = new SmallDialog(this, getString(R.string.small_dialog));
        getIntentData();
        initListen();
        initView();
        prohibitCopy(pwd);
    }

    private void initView() {
        if (BuildConfig.ENVIRONMENT.equals("qyt")) {
            appCompatImageView.setImageResource(R.mipmap.login_logo);
        } else if (BuildConfig.ENVIRONMENT.equals("qyb")) {
            appCompatImageView.setImageResource(R.mipmap.ic_qyb_logo);
        }
        if (!BuildConfig.IS_DEBUG) {
            if(loginUserType.equals(UserRoleType.JOURNEY)){
//                name.setText("mj_lxs");
//                pwd.setText("12345678");
                name.setText("systemAdmin");
                pwd.setText("ab123456");
//                name.setText("T_8901");
//                pwd.setText("12345678");
            }else{
//                name.setText("yys002");
//                pwd.setText("12345678");
                name.setText("yys001");
                pwd.setText("wootide123");
            }

        }
    }

    private void initListen() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pwd.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Editname = name.getText().toString();
                if (StringUtils.isEmpty(Editname)) {
                    ToastUtils.showToast(R.string.login_user_empty);
                    return;
                }
                String Editpwd = pwd.getText().toString();
                if (StringUtils.isEmpty(Editpwd)) {
                    ToastUtils.showToast(R.string.login_user_empty);
                    return;
                }
                Presenter.loadserUsrDetail(Editname, Editpwd);
            }
        });
    }

    @Override
    public void showUserDetail() {
        if(loginUserType.equals(UserHelper.getInstance().getUser().getUserRoleType())){
            if(loginUserType.equals(UserRoleType.JOURNEY)){
                AgentMainActivity.callActivity(LoginActivity.this);
            }else{
                TourismMainActivity.callActivity(LoginActivity.this);
            }
            LoginActivity.this.finish();
            overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
        }else{
            if(loginUserType.equals(UserRoleType.JOURNEY)){
                ToastUtils.showToast("请使用旅行社帐号登录");
            }else{
                ToastUtils.showToast("请使用旅游局帐号登录");
            }
        }
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

    /**
     * 设置View禁止粘贴复制
     *
     * @param editText 输入框
     */
    private void prohibitCopy(EditText editText) {
        editText.setLongClickable(false);
        editText.setTextIsSelectable(false);
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

}
