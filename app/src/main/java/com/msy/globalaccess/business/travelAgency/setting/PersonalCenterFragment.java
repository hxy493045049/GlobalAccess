package com.msy.globalaccess.business.travelAgency.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.navigation.NavigationActivity;
import com.msy.globalaccess.common.LoginType;
import com.msy.globalaccess.data.bean.user.User;
import com.msy.globalaccess.data.holder.UserHelper;
import com.msy.globalaccess.utils.ApplicationUtils;
import com.msy.globalaccess.widget.dialog.SmallDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by WuDebin on 2017/1/20.
 */

public class PersonalCenterFragment extends BaseFragment {

    @BindView(R.id.tv_user_login_account)
    TextView tvUserLoginAccount;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_telephone)
    TextView tvUserTelephone;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_user_system)
    TextView tvUserSystem;
    @BindView(R.id.tv_user_company)
    TextView tvUserCompany;
    @BindView(R.id.tv_user_department)
    TextView tvUserDepartment;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.app_version)
    TextView app_version;
    @BindView(R.id.personal_center_ll_logo)
    LinearLayout personal_center_ll_logo;

    private SmallDialog smallDialog;

    @Inject
    public PersonalCenterFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    @Override
    public void initInjector() {

    }

    @Override
    public void init(View view) {
        smallDialog = new SmallDialog(getActivity(), getActivity().getResources().getString(R.string.exit));
        if(BuildConfig.ENVIRONMENT.equals("qyt")){
            personal_center_ll_logo.setVisibility(View.VISIBLE);
            app_version.setText("张家界全域通v"+ ApplicationUtils.getVersion()[0]);
        }else if(BuildConfig.ENVIRONMENT.equals("qyb")){
            personal_center_ll_logo.setVisibility(View.GONE);
            app_version.setText("全域宝v"+ ApplicationUtils.getVersion()[0]);
        }else{
            app_version.setText("全域宝v"+ ApplicationUtils.getVersion()[0]);
        }
        showUserContent();
    }

    public void showUserContent(){
        User user=UserHelper.getInstance().getUser();
//        ArrayList<RoleBean> roleBeanList = new Gson().fromJson(user.getRoleList(), new TypeToken<ArrayList<RoleBean>>(){}.getType());
        tvUserLoginAccount.setText(user.getUserAccount());
        tvUserName.setText(user.getUsername());
        tvUserPhone.setText(user.getUserMobile());
        tvUserTelephone.setText(user.getUserTel());
        tvUserEmail.setText(user.getUserEmail());
        tvUserAddress.setText(user.getUserAddr());
        tvUserSystem.setText(user.getUserSystem());
        tvUserCompany.setText(user.getUserUnitName());
        tvUserDepartment.setText(user.getUserDepName());
    }

    @OnClick(R.id.tv_logout)
    public void logout() {
        smallDialog.show();
        User user=UserHelper.getInstance().getUser();
        user.setUserLoginStatus(LoginType.STATUS_LOGOUT);
        UserHelper.getInstance().updateUser(user);
        UserHelper.getInstance().setUser(null);
        NavigationActivity.callActivity(getActivity());
        getActivity().finish();
        smallDialog.dismisss();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal_center;
    }


}
