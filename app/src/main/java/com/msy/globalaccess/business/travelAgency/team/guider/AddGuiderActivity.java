package com.msy.globalaccess.business.travelAgency.team.guider;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.FreeGuiderListAdapter;
import com.msy.globalaccess.business.travelAgency.team.contract.IAddGuiderContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.AddGuiderPresenterImpl;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.city.CityBean;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.base.KeyMapBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.dialog.AlertDialogBuilder;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;
import com.msy.globalaccess.widget.recyclerview.TopHorizontalDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.msy.globalaccess.business.travelAgency.team.guider.GuideListActivity.KEY_ADD_GUIDER;

/**
 * Created by shawn on 2017/3/17 0017.
 * <p>
 * description :页面逻辑
 * 1.进入页面时请求城市信息,并将之保存到内存中
 * 如果城市信息成功返回,初始化dialog,如果城市信息未返回,点击"地区时"重新请求,递归~
 * 2.进入页面,地区默认使用用户登录时返回的所属地id,语种默认显示全部
 */
public class AddGuiderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        IAddGuiderContract.View {

    private static final String KEY_TEAM_INFO_ID = "teamInfoId";
    @BindView(R.id.tlFilter)
    TabLayout tlFilter;
    @BindView(R.id.rvAddList)
    RecyclerView rvAddList;
    @BindView(R.id.tvConfirm)
    AppCompatTextView tvConfirm;
    @BindView(R.id.srlContainer)
    SwipeRefreshLayout srlContainer;
    @Inject
    AddGuiderPresenterImpl mPresenter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;
    View notDataView, errorView, loaddingView;
    private AppCompatEditText etFilter;
    private AppCompatTextView tvFilterHint;
    private FreeGuiderListAdapter mAdapter;
    private List<FreeGuiderListBean> mGuiders;//导游数据
    //----------语言滚轮相关---------------
    private ArrayList<KeyMapBean> mLanguageBeen;//语言选项
    private WheelViewDialog<KeyMapBean> languagePicker;
    private String mLanguageType = "";
    private boolean shouldShowLanguagePicker;//是否需要显示语言滚轮的标识
    //----------城市滚轮相关---------------
    private ArrayList<CityBean> mCitys;//城市选项
    private WheelViewDialog<CityBean> cityPicker;
    private String mCityName = "";
    private boolean shouldShowCityPicker;//是否需要显示城市滚轮的标识
    //----------导游信息滚轮相关---------------
    private List<String> freeGuiderInfo;//导游信息选项
    private int guiderInfoType = -1;//导游信息类型,默认-1
    private WheelViewDialog<String> guiderInfoPicker;
    private Dialog filterDialog;
    private String mFilterValueIdentityNum = "", mFilterValueGuiderName = "",
            mFilterValueAffiliateUnit = "";
    private FreeGuiderListBean selectedFreeGuider;
    private boolean useDefaultParam = true;
    private String teamInfofId = "";

    public static void callActivityForResult(Activity ctx, String teamInfoId, int reqCode) {
        Intent intent = new Intent();
        intent.putExtra(KEY_TEAM_INFO_ID, teamInfoId);
        intent.setClass(ctx, AddGuiderActivity.class);
        ctx.startActivityForResult(intent, reqCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_guider;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
        basePresenter = mPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void init() {
        teamInfofId = getIntent().getStringExtra(KEY_TEAM_INFO_ID);
        freeGuiderInfo = Arrays.asList(App.getResource().getStringArray(R.array.freeGuiderInfo));
        initToolbar();
        initRecycierView();
        initTabLayout();
        initSwipeRefresh();

        initGuiderInfoPicker();
        initListener();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            if (mAdapter != null && mAdapter.getData().size() > 0) {
                srlContainer.setRefreshing(true);
            }
            if (mAdapter != null) {
                mAdapter.setEmptyView(loaddingView);
            }
            mPresenter.loadGuiderList();
        }
    }

    @Override
    public void showProgress() {
        //        if (srlContainer != null && !srlContainer.isRefreshing() && !(mGuiders == null || mGuiders.size()
        // == 0)) {
        //            srlContainer.post(new Runnable() {
        //                @Override
        //                public void run() {
        //                    if (!srlContainer.isRefreshing()) {
        //                        srlContainer.setRefreshing(true);
        //                    }
        //                }
        //            });
        //        }
    }

    @Override
    public void hideProgress() {
        srlContainer.setRefreshing(false);
    }

    @SuppressWarnings("deprecation")
    private void initSwipeRefresh() {
        srlContainer.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        srlContainer.setOnRefreshListener(this);
    }

    @Override
    public String getGuiderIdentityNum() {
        return mFilterValueIdentityNum;
    }

    @Override
    public String getGuiderName() {
        return mFilterValueGuiderName;
    }

    @Override
    public String getGuiderAffiliateUnit() {
        return useDefaultParam ? App.userHelper.getUser().getUserUnitName() : mFilterValueAffiliateUnit;
    }

    @Override
    public String getCity() {
        return mCityName;
    }

    @Override
    public String getLanguageType() {
        return mLanguageType;
    }

    @Override
    public String getCityLevel() {
        return "2";
    }

    @Override
    public String getTeamInfoId() {
        return teamInfofId;
    }

    @Override
    public String getRegisterProvince() {
        return App.userHelper.getUser().getProvince();
    }

    @Override
    public void onLanguageCallBack(List<KeyMapBean> languageBeen) {
        mLanguageBeen = (ArrayList<KeyMapBean>) languageBeen;
        KeyMapBean first = new KeyMapBean();
        first.setMapValue(App.getResourceString(R.string.guider_language));
        mLanguageBeen.add(0, first);
        initLanguagePicker();
    }

    @Override
    public void onCityCallBack(List<CityBean> cityBeen) {
        mCitys = (ArrayList<CityBean>) cityBeen;
        CityBean cityBean = new CityBean();
        cityBean.setCityName(App.getResourceString(R.string.guider_area));
        mCitys.add(0, cityBean);
        initCityPicker();
    }

    @Override
    public void onGuiderListCallBack(@ResultCode int result, List<FreeGuiderListBean> data) {
        if (result == ResultCode.SUCCESS || result == ResultCode.SUCCESS_EMPTY) {
            mAdapter.setNewData(data);
        }
        this.mGuiders = mAdapter.getData();
        setAdapterStatus(result);
    }

    @Override
    public void onGuiderListFailed(@ResultCode int resultCode, @Nullable String message) {
        if (mAdapter.getData().size() > 0) {
            ToastUtils.showToast(message);
        }
        mAdapter.setNewData(null);
        setAdapterStatus(resultCode);
    }

    private void initListener() {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (selectedFreeGuider != null) {
                    intent.putExtra(KEY_ADD_GUIDER, selectedFreeGuider);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    //没选则
                    ToastUtils.showToast(App.getResourceString(R.string.hint_no_guider_choose));
                }
            }
        });
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void initToolbar() {
        tvToolbarCenter.setVisibility(View.VISIBLE);
        tvToolbarCenter.setText(App.getResourceString(R.string.guider_add));
    }

    private void initTabLayout() {
        tlFilter.addTab(tlFilter.newTab().setCustomView(R.layout.item_tab_arrow_down)
                .setText(TextUtils.isEmpty(mCityName) ? App.getResourceString(R.string.guider_area) : mCityName));
        tlFilter.addTab(tlFilter.newTab().setCustomView(R.layout.item_tab_arrow_down_separator)
                .setText(freeGuiderInfo.get(0)));
        tlFilter.addTab(tlFilter.newTab().setCustomView(R.layout.item_tab_arrow_down)
                .setText(App.getResourceString(R.string.guider_language)));

        tlFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                clickTab();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                clickTab();
            }
        });

    }

    private void initRecycierView() {
        rvAddList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FreeGuiderListAdapter(R.layout.item_guider_add_list, mGuiders);
        mAdapter.setEnableLoadMore(false);
        rvAddList.setAdapter(mAdapter);
        SimpleItemAnimator animator = (SimpleItemAnimator) rvAddList.getItemAnimator();
        animator.setChangeDuration(0);
        rvAddList.addItemDecoration(new TopHorizontalDecoration(this, R.drawable.divider_10dp_gray));
        rvAddList.setHasFixedSize(true);
        rvAddList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setChecked(position, (FreeGuiderListBean) adapter.getData().get(position),
                        (AppCompatCheckBox) view.findViewById(R.id.btnDelegate));
            }
        });
        setAdapterView();
        mAdapter.setOnCheckedCallBack(new FreeGuiderListAdapter.OnCheckCallBack() {
            @Override
            public void onChecked(FreeGuiderListBean selectedGuider) {
                selectedFreeGuider = selectedGuider;
            }
        });
    }

    private void setAdapterView() {
        notDataView = ViewHelper.getEmptyView(this, rvAddList);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        errorView = ViewHelper.getErrorView(this, rvAddList);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        loaddingView = ViewHelper.getLoadingView(this, rvAddList);

        mAdapter.setEmptyView(loaddingView);
    }

    private void clickTab() {
        int position = tlFilter.getSelectedTabPosition();
        switch (position) {
            case 0:
                shouldShowCityPicker = true;
                showCityPicker();
                break;
            case 1:
                guiderInfoPicker.show();
                break;
            case 2:
                shouldShowLanguagePicker = true;
                showLanguagePicker();
                break;
        }
    }

    private void showCityPicker() {
        if (cityPicker == null) {
            mPresenter.loadCityList();
        } else {
            cityPicker.show();
            shouldShowCityPicker = false;
        }
    }

    private void changeTabText() {
        TabLayout.Tab tab = tlFilter.getTabAt(1);
        if (tab != null) {
            tab.setText(freeGuiderInfo.get(guiderInfoType));
        }
    }

    //城市选择框
    private void initCityPicker() {
        cityPicker = new WheelViewDialog<>(this, mCitys, new WheelViewDialog
                .onWheelViewPickedListener1<CityBean>() {
            @Override
            public void onPicked(CityBean pickedItem, int position) {
                mCityName = pickedItem.getCityName();
                useDefaultParam = false;
                TabLayout.Tab tab = tlFilter.getTabAt(0);
                if (tab != null) {
                    tab.setText(mCityName);
                }

                if (position == 0) {
                    resetCityInfo();
                }
                onRefresh();
            }
        });
        if (shouldShowCityPicker) {
            showCityPicker();
        }
    }

    //导游信息选择框
    private void initGuiderInfoPicker() {
        guiderInfoPicker = new WheelViewDialog<>(this, freeGuiderInfo, new WheelViewDialog
                .onWheelViewPickedListener1<String>() {
            @Override
            public void onPicked(String pickedItem, int position) {
                useDefaultParam = false;
                guiderInfoType = position;
                resetGuiderInfoFilterFlag();
                if (position == 0) {
                    changeTabText();
                    onRefresh();
                    return;
                }
                if (tvFilterHint != null) {
                    switch (position) {
                        case 1://导游证号
                            tvFilterHint.setText(App.getResourceString(R.string.guider_filter_identity_number_hint));
                            break;
                        case 2:// 导游姓名
                            tvFilterHint.setText(App.getResourceString(R.string.guider_filter_name_hint));
                            break;
                        case 3:// 挂靠单位
                            tvFilterHint.setText(App.getResourceString(R.string.guider_filter_affiliate_unit_hint));
                            break;
                    }
                }
                if (filterDialog != null) {
                    filterDialog.show();
                }
            }
        });
        initFilterDialog();
    }

    //语言选择框
    private void initLanguagePicker() {
        languagePicker = new WheelViewDialog<>(this, mLanguageBeen, new WheelViewDialog
                .onWheelViewPickedListener1<KeyMapBean>() {
            @Override
            public void onPicked(KeyMapBean pickedItem, int position) {
                useDefaultParam = false;
                mLanguageType = pickedItem.getMapKey();
                TabLayout.Tab tab = tlFilter.getTabAt(2);
                if (tab != null) {
                    tab.setText(pickedItem.getMapValue());
                }
                if (position == 0) {
                    resetLanguageInfo();
                }
                onRefresh();
            }
        });
        if (shouldShowLanguagePicker) {
            showLanguagePicker();
        }
    }

    //导游信息内容框
    private void initFilterDialog() {
        filterDialog = new AlertDialogBuilder(this)
                .setContentLayout(R.layout.dialog_guilder_filter)
                .setPositiveButton(App.getResourceString(R.string.confirm), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeTabText();
                        switch (guiderInfoType) {
                            case 1:
                                mFilterValueIdentityNum = etFilter.getText().toString().trim();
                                break;
                            case 2:
                                mFilterValueGuiderName = etFilter.getText().toString().trim();
                                break;
                            case 3:
                                mFilterValueAffiliateUnit = etFilter.getText().toString().trim();
                                break;
                        }
                        filterDialog.dismiss();
                        onRefresh();
                    }
                }).setNegativeButton(App.getResourceString(R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filterDialog.dismiss();
                    }
                }).create();

        etFilter = (AppCompatEditText) filterDialog.findViewById(R.id.etFilterValue);
        tvFilterHint = (AppCompatTextView) filterDialog.findViewById(R.id.tvFilterHint);

        filterDialog.setCancelable(true);
        filterDialog.setCanceledOnTouchOutside(false);
    }

    private void showLanguagePicker() {
        if (languagePicker == null) {
            mPresenter.loadLanguage();
        } else {
            languagePicker.show();
            shouldShowLanguagePicker = false;
        }
    }

    private void setAdapterStatus(int result) {
        switch (result) {
            case ResultCode.SEARCH_NO_DATA:
            case ResultCode.SUCCESS_EMPTY:
                mAdapter.setEmptyView(notDataView);
                break;
            default:
                mAdapter.setEmptyView(errorView);
                break;
        }
    }

    private void resetGuiderInfoFilterFlag() {
        if (etFilter != null) {
            etFilter.setText("");
        }
        mFilterValueIdentityNum = "";
        mFilterValueGuiderName = "";
        mFilterValueAffiliateUnit = "";
    }

    private void resetCityInfo() {
        mCityName = "";
    }

    private void resetLanguageInfo() {
        mLanguageType = "";
    }

}
