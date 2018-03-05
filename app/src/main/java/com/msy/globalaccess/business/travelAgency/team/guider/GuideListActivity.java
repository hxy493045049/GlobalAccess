package com.msy.globalaccess.business.travelAgency.team.guider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.GuiderListAdapter;
import com.msy.globalaccess.business.travelAgency.team.contract.IGuiderListContract;
import com.msy.globalaccess.business.travelAgency.team.contract.impl.GuiderListPresenterImpl;
import com.msy.globalaccess.common.enums.GuiderListOperation;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.utils.helper.ViewHelper;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.recyclerview.RecyclerDecoration;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.TimeFormat;

import static com.msy.globalaccess.common.enums.ResultCode.ARGUMENT_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.DATABASE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.ERROR_GUIDER_ARGUMENT;
import static com.msy.globalaccess.common.enums.ResultCode.GUIDER_ALREADY_BEEN_DELEGATE;
import static com.msy.globalaccess.common.enums.ResultCode.INTERFACE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.NET_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SERVICE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SIGN_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SUCCESS_EMPTY;

/**
 * Created by hxy on 2017/3/15 0015.
 * <p>
 * description :
 */

@SuppressWarnings("deprecation")
public class GuideListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        IGuiderListContract.View {
    public static final String KEY_DATA = "data";

    public static final int REQUEST_CODE_EDIT = 0x001;
    public static final int REQUEST_CODE_DELEGATE = 0x002;
    public static final int REQUEST_CODE_ADD = 0x003;

    public static final String KEY_GUIDER_EDIT = "guiderInfo";//编辑导游
    public static final String KEY_GUIDER_DELEGATE = "guiderdElegate";//委派导游
    public static final String KEY_ADD_GUIDER = "selected_free_guider";//新增导游

    @BindView(R.id.srlChangeGurder)
    SwipeRefreshLayout srlChangeGurder;
    @BindView(R.id.rvAddList)
    RecyclerView rvGuiderList;
    @BindView(R.id.btnAddGuider)
    AppCompatButton btnAddGuider;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarCenter;
    @BindView(R.id.tvToolbarRight)
    AppCompatTextView tvToolbarRight;
    @Inject
    GuiderListPresenterImpl mPresenter;
    private ArrayMap<String, GuiderListBean> editedGuiders = new ArrayMap<>();//编辑过的导游记录
    private LinkedHashMap<String, GuiderListBean> addedGuiders = new LinkedHashMap<>();//新增的导游记录
    private ArrayMap<String, GuiderListBean> deletedGuiders = new ArrayMap<>();//已删除的导游记录
    private ArrayMap<String, GuiderListBean> needRequestRecord = new ArrayMap<>();//必须要申请委派后才能提交的记录
    private GuiderListAdapter mAdapter;
    private View notDataView, errorView, loaddingView;
    private TeamDetailBean bean;
    private PopUpWindowAlertDialog alertDialog;
    private GuiderListBean oprationTarget;
    private SmallDialog commitLoading;
    private SmallDialog progressDialog;
    /**
     * 操作类型,现在有2中,数字2表示删除,数字3表示取消网络委派,它的值说明见{@link GuiderListOperation}相同
     */
    @GuiderListOperation
    private int operType = GuiderListOperation.OPER_INVALID;

    public static void callActivity(TeamDetailBean bean, Context ctx) {
        Intent intent = new Intent();
        intent.setClass(ctx, GuideListActivity.class);
        intent.putExtra(KEY_DATA, bean);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide_list;
    }

    @Override
    protected void initInjector() {
        getActivityComponent().inject(this);
        basePresenter = mPresenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(KEY_DATA)) {
            bean = bundle.getParcelable(KEY_DATA);
        }
        progressDialog = new SmallDialog(this);
        initHeader();
        initContent();
        commitLoading = new SmallDialog(this, App.getResourceString(R.string.commiting));
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.loadGuiderList();
            mAdapter.setEmptyView(loaddingView);
        }
    }

    @Override
    public void showProgress() {
        if (mAdapter != null && mAdapter.getData().size() > 0) {
            progressDialog.shows();
        }
    }

    @Override
    public void hideProgress() {
        srlChangeGurder.setRefreshing(false);
        progressDialog.dismisss();
    }

    @Override
    public String getTeamInfoId() {
        if (bean != null) {
            return bean.getTeamBaseInfo().getTeamId();
        }
        return "";
    }

    @Override
    public List<GuiderListBean> getTeamGuideList() {
        List<GuiderListBean> temp = new ArrayList<>();
        temp.addAll(mAdapter.getData());
        if (deletedGuiders != null && deletedGuiders.size() > 0) {
            temp.addAll(deletedGuiders.values());
            return temp;
        }
        return temp;
    }

    @Override
    public List<GuiderListBean> getUncheckGuideList() {
        List<GuiderListBean> temp = new ArrayList<>();
        temp.addAll(mAdapter.getData());
        if (deletedGuiders != null && deletedGuiders.size() > 0) {
            temp.addAll(deletedGuiders.values());
        }
        try {
            GuiderListBean changeBean = (GuiderListBean) oprationTarget.clone();
            changeBean.setDelFlag("1");
            changeBean.setOpType("0");
            int position = getPositionByItem(temp, changeBean);
            if (position != -1) {
                temp.set(position, changeBean);
            } else {
                Logger.e("找不到导游id为" + changeBean.getGuideId() + "的item");
            }
        } catch (CloneNotSupportedException e) {
            Logger.e("无法复制对象");
        }
        return temp;
    }

    @Override
    public void modifyGuiderSuccess() {
        ToastUtils.showToast(App.getResourceString(R.string.hint_change_success));
        clearRecord();
        finish();
    }

    @Override
    public void handleData(@ResultCode int resultCode, List<GuiderListBean> data) {
        updateData(resultCode, data);
        setAdapterStatus(resultCode);
    }

    @Override
    public void onError(@ResultCode int resultCode, String errorMsg) {
        if (resultCode == SUCCESS_EMPTY || resultCode == NET_ERROR || resultCode == SERVICE_ERROR
                || resultCode == DATABASE_ERROR || resultCode == ARGUMENT_ERROR || resultCode == SIGN_ERROR ||
                resultCode == INTERFACE_ERROR) {
            mAdapter.setNewData(null);
        } else if (resultCode == ERROR_GUIDER_ARGUMENT) {
            onRefresh();//没有有效导游,提交失败,重新刷新数据
        }
        if (resultCode == GUIDER_ALREADY_BEEN_DELEGATE) {
            PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
            builder.setMessage(errorMsg, 16);
            builder.setTitle(null, 0);
            builder.setPositiveButton(App.getResourceString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
        } else {
            ToastUtils.showToast(errorMsg);
        }
        setAdapterStatus(resultCode);
    }

    @Override
    public void showCommitLoading() {
        commitLoading.shows();
    }

    @Override
    public void hideCommitLoading() {
        commitLoading.hide();
    }

    @Override
    public void canDelete() {
        if (operType == GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET) {
            requestCancel(oprationTarget);
        } else if (operType == GuiderListOperation.OPER_DELETE_INTERNET) {
            deleteRecord(oprationTarget);
        }

        oprationTarget = null;
        operType = GuiderListOperation.OPER_INVALID;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_EDIT://编辑成功返回
                GuiderListBean bean1 = data.getParcelableExtra(KEY_GUIDER_EDIT);
                editorRecord(bean1);
                break;
            case REQUEST_CODE_DELEGATE://委派变更返回
                GuiderListBean bean = data.getParcelableExtra(KEY_GUIDER_DELEGATE);
                if (bean.getOpType().equals("0") && needRequestRecord.size() > 0 && needRequestRecord.containsKey(bean
                        .getGuideId())) {
                    needRequestRecord.remove(bean.getGuideId());
                }
                editorRecord(bean);
                break;
            case REQUEST_CODE_ADD://添加导游返回
                GuiderListBean bean2 = adapterData((FreeGuiderListBean) data.getParcelableExtra(KEY_ADD_GUIDER));
                addRecord(bean2);
                break;
        }
    }

    @Override
    public void finish() {
        //        showAlert(GuiderListOperation.OPER_CANCEL);
        if (!editedGuiders.isEmpty()) {
            showAlert(GuiderListOperation.OPER_CANCEL);
        } else {
            super.finish();
        }
    }

    //*********************private**************************************

    private int getPositionByItem(List<GuiderListBean> datas, GuiderListBean guiderListBean) {
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                GuiderListBean guider = datas.get(i);
                if (guider.getGuideId() != null && guider.getGuideId().equals(guiderListBean.getGuideId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 1.如果是本社导游,optype默认为0
     * 2.如果是外社导游
     * ------a.不需要委派,optype=0
     * ------b.需要委派    optype=-1
     */
    private GuiderListBean adapterData(FreeGuiderListBean freeGuider) {
        GuiderListBean guiderListBean = new GuiderListBean();
        guiderListBean.setIsNativeGuider("1");
        guiderListBean.setGuideId(freeGuider.getGuideId());

        guiderListBean.setDelFlag("0");

        if (freeGuider.getIsOur().equals("1") || freeGuider.getMustAppoint().equals("0")) {
            guiderListBean.setOpType("0");
        } else {
            guiderListBean.setOpType("-1");
        }

        guiderListBean.setOpStatus("0");
        guiderListBean.setTeamGuideId("_");
        guiderListBean.setIsOur(freeGuider.getIsOur());
        guiderListBean.setMustAppoint(freeGuider.getMustAppoint());


        guiderListBean.setName(freeGuider.getName());
        guiderListBean.setPhoneNum(freeGuider.getHandPhone());
        guiderListBean.setGuideCode(freeGuider.getGuideCode());

        guiderListBean.setTeamInfoId(bean.getTeamBaseInfo().getTeamId());
        guiderListBean.setSex(freeGuider.getSex());

        String begin = TimeFormat.transformData(bean.getTeamBaseInfo().getDepartureInfo(), TimeFormat.regular9,
                TimeFormat.regular9);
        String end = TimeFormat.transformData(bean.getTeamBaseInfo().getReturnInfo(), TimeFormat.regular9, TimeFormat
                .regular9);
        guiderListBean.setAppointStartDate(begin);
        guiderListBean.setAppointEndDate(end);
        guiderListBean.setTravelAgentName(freeGuider.getTravelAgentName());

        return guiderListBean;
    }

    private void setAdapterView() {
        notDataView = ViewHelper.getEmptyView(this, rvGuiderList);
        notDataView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        errorView = ViewHelper.getErrorView(this, rvGuiderList);
        errorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRefresh();
            }
        });

        loaddingView = ViewHelper.getLoadingView(this, rvGuiderList);

        mAdapter.setEmptyView(loaddingView);
    }

    private void initContent() {
        rvGuiderList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GuiderListAdapter(R.layout.item_guider_list, null);
        mAdapter.setEnableLoadMore(false);
        rvGuiderList.setAdapter(mAdapter);
        RecyclerDecoration decoration = new RecyclerDecoration(this, LinearLayoutManager.VERTICAL,
                R.drawable.divider_10dp_gray);
        decoration.shouldDrawLastDividerOnLast(false);
        rvGuiderList.addItemDecoration(decoration);
        rvGuiderList.setHasFixedSize(true);
        setAdapterView();
        initListener();
    }

    private void initListener() {
        mAdapter.setOnRecordListener(new GuiderListAdapter.OnCommitListener() {
            @Override
            public void getGuiderInfo(GuiderListBean guider) {
                needRequestRecord.put(guider.getGuideId(), guider);
            }
        });
        rvGuiderList.addOnItemTouchListener(
                new OnItemChildClickListener() {
                    @Override
                    public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        oprationTarget = (GuiderListBean) adapter.getData().get(position);
                        String tag = (String) view.getTag();
                        if (TextUtils.isEmpty(tag)) {
                            ToastUtils.showToast("无效操作");
                            Logger.e("无效操作,一般不会走到这里");
                            return;
                        }

                        switch (view.getId()) {
                            case R.id.btnDelegate:
                                switch (tag) {
                                    case GuiderListAdapter.EVENT_REQUEST:
                                        showAlert(GuiderListOperation.OPER_DELEGATE);//申请委派
                                        break;
                                    case GuiderListAdapter.INTERNET_ITEM: //取消委派(网络),至少保留一条
                                        if (mAdapter.getData().size() - addedGuiders.size() <= 1) {
                                            ToastUtils.showToast(App.getResourceString(R.string
                                                    .hint_atleast_one_guider));
                                            return;
                                        }
                                        showAlert(GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET);
                                        break;
                                    case GuiderListAdapter.LOCAL_ITEM:
                                        showAlert(GuiderListOperation.OPER_CANCEL_DELEGATE_LOCAL);//取消委派(本地)
                                        break;
                                }
                                break;
                            case R.id.btnDelete:
                                if (tag.equals(GuiderListAdapter.INTERNET_ITEM)) {//删除(网络),至少保留一条
                                    if (mAdapter.getData().size() - addedGuiders.size() <= 1) {
                                        ToastUtils.showToast(App.getResourceString(R.string.hint_atleast_one_guider));
                                        return;
                                    }
                                    showAlert(GuiderListOperation.OPER_DELETE_INTERNET);
                                } else if (tag.equals(GuiderListAdapter.LOCAL_ITEM)) {
                                    showAlert(GuiderListOperation.OPER_DELETE_LOCAL); //删除(本地)
                                }
                                break;
                        }
                    }
                });
        btnAddGuider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddGuiderActivity.callActivityForResult(GuideListActivity.this,getTeamInfoId(), REQUEST_CODE_ADD);
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });
        rvGuiderList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                EditGuiderActivity.callActivity((GuiderListBean) adapter.getData().get(position), bean,
                        GuideListActivity.this, REQUEST_CODE_EDIT);
                overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
            }
        });
    }

    private void initHeader() {
        tvToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarRight.setText(App.getResourceString(R.string.commit));
        tvToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needRequestRecord != null && needRequestRecord.size() > 0) {
                    ToastUtils.showToast(App.getResourceString(R.string.hint_invalid_guider));
                } else {
                    if (mAdapter.getData().size() <= 0) {
                        ToastUtils.showToast("无效操作");
                        return;
                    }
                    if (editedGuiders != null && editedGuiders.size() > 0) {
                        showAlert(GuiderListOperation.OPER_COMMIT);
                    } else {
                        showAlert(GuiderListOperation.OPER_COMMIT_UNCHANGE);
                    }
                }
            }
        });

        tvToolbarCenter.setVisibility(View.VISIBLE);
        tvToolbarCenter.setText(App.getResourceString(R.string.guider_change));
        srlChangeGurder.setEnabled(false);
        //        srlChangeGurder.setColorSchemeColors(App.getResource().getColor(R.color.colorPrimary));
        //        srlChangeGurder.setOnRefreshListener(this);
    }

    private void updateData(int result, List<GuiderListBean> data) {
        if (result == ResultCode.SUCCESS || result == ResultCode.SUCCESS_NO_MORE_DATA || result ==
                SUCCESS_EMPTY) {
            resetRecord(data);
            if (addedGuiders != null && addedGuiders.size() > 0) {
                for (Map.Entry<String, GuiderListBean> entry : addedGuiders.entrySet()) {
                    data.add(0, entry.getValue());
                }
            }
            mAdapter.setNewData(data);
        }
    }

    @SuppressLint("SwitchIntDef")
    private void setAdapterStatus(@ResultCode int result) {
        switch (result) {
            case ResultCode.SUCCESS:
                mAdapter.loadMoreComplete();
                break;
            case ResultCode.SUCCESS_NO_MORE_DATA:
            case ResultCode.SEARCH_NO_DATA:
            case SUCCESS_EMPTY:
                mAdapter.loadMoreEnd();
                mAdapter.setEmptyView(notDataView);
                break;
            default:
                mAdapter.loadMoreFail();
                mAdapter.setEmptyView(errorView);
                break;
        }
    }

    /**
     * 弹框
     *
     * @param title 弹框标题
     */
    @SuppressLint("SwitchIntDef")
    private void initAlertDialog(String title) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(title, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton(App.getResourceString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (operType) {
                    case GuiderListOperation.OPER_COMMIT://0 提交操作
                    case GuiderListOperation.OPER_COMMIT_UNCHANGE://0 提交操作,用户未做
                        mPresenter.modifyGuider();
                        break;
                    case GuiderListOperation.OPER_CANCEL://1,取消操作
                        GuideListActivity.super.finish();
                        break;
                    case GuiderListOperation.OPER_DELETE_INTERNET://删除(网络)
                    case GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET://网络取消委派
                        mPresenter.whetherCanDeleteGuider();
                        break;
                    case GuiderListOperation.OPER_DELETE_LOCAL://删除(本地)
                        deleteRecord(oprationTarget);
                        break;
                    case GuiderListOperation.OPER_CANCEL_DELEGATE_LOCAL://本地取消委派
                        deleteRecord(oprationTarget);
                        break;
                    case GuiderListOperation.OPER_DELEGATE://委派
                        requestDelegate(oprationTarget);
                        break;
                }
                dialog.dismiss();
            }
        });


        builder.setNegativeButton(App.getResourceString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                resetDialogFlag();
            }
        });
    }

    /**
     * 如果是网络数据,会在{@link #canDelete()}中清除标记
     */
    private void resetDialogFlag() {
        if (operType != GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET && operType != GuiderListOperation
                .OPER_DELETE_INTERNET) {
            operType = GuiderListOperation.OPER_INVALID;
            oprationTarget = null;
        }
    }

    /**
     * @param type 不同操作
     */
    @SuppressLint("SwitchIntDef")
    private void showAlert(@GuiderListOperation int type) {
        operType = type;
        TextView tvMessage = null;
        if (alertDialog != null) {
            tvMessage = (TextView) alertDialog.findViewById(R.id.tvMessage);
        }
        switch (operType) {
            case GuiderListOperation.OPER_COMMIT://提交
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_commit_change_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_commit_change_guider));
                }
                break;
            case GuiderListOperation.OPER_COMMIT_UNCHANGE:
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_commit_unchange_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_commit_unchange_guider));
                }
                break;
            case GuiderListOperation.OPER_CANCEL://取消
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_cancel_change_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_cancel_change_guider));
                }
                break;
            case GuiderListOperation.OPER_DELETE_LOCAL://删除(本地)
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_delete_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_delete_guider));
                }
                break;
            case GuiderListOperation.OPER_DELETE_INTERNET://删除(网络)
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_delete_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_delete_guider));
                }
                break;
            case GuiderListOperation.OPER_CANCEL_DELEGATE_INTERNET:// 取消委派(网络)
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_cancel_delegate_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_cancel_delegate_guider));
                }
                break;
            case GuiderListOperation.OPER_CANCEL_DELEGATE_LOCAL:// 取消委派(本地)
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_cancel_delegate_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_cancel_delegate_guider));
                }
                break;
            case GuiderListOperation.OPER_DELEGATE://委派
                if (alertDialog != null && tvMessage != null) {
                    tvMessage.setText(App.getResourceString(R.string.hint_delegate_guider));
                    alertDialog.show();
                } else {
                    initAlertDialog(App.getResourceString(R.string.hint_delegate_guider));
                }
                break;
        }
    }

    private void updateItem(GuiderListBean guiderListBean) {
        int position = getPositionByItem(mAdapter.getData(), guiderListBean);
        if (position != -1) {
            mAdapter.setData(position, guiderListBean);
        }
    }

    //申请委派
    private void requestDelegate(GuiderListBean bean) {
        DelegateGuiderActivity.callActivity(bean, GuideListActivity.this, REQUEST_CODE_DELEGATE);
        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
    }

    //取消委派
    private void requestCancel(GuiderListBean bean) {
        bean.setOpType("1");
        bean.setOpStatus("0");
        editorRecord(bean);
    }

    //----------------record结尾的方法,都是对4大数据源的操作-----------------------------

    private void clearRecord() {
        addedGuiders.clear();
        editedGuiders.clear();
        deletedGuiders.clear();
        needRequestRecord.clear();
        //        mDeleteSizeOfValideGuider = 0;
    }

    /**
     * 下拉刷新,还原网络导游
     * 1.去除本地数据源中,网络导游的记录
     * 2.去除编辑记录中,网络导游的记录
     * 3.取出删除记录中,网络导游的记录
     * 4.取出待委派记录中,网络导游的记录
     */
    private void resetRecord(List<GuiderListBean> data) {
        for (GuiderListBean bean : data) {
            addedGuiders.remove(bean.getGuideId());
            editedGuiders.remove(bean.getGuideId());
            deletedGuiders.remove(bean.getGuideId());
            needRequestRecord.remove(bean.getGuideId());
        }
        //        mDeleteSizeOfValideGuider = 0;
    }

    /**
     * 修改操作
     * 1.修改数据源
     * 2.更新删除记录
     * 3.更新本地记录
     * 4.添加编辑记录
     */
    private void editorRecord(GuiderListBean bean) {
        bean.setIsModified("1");
        updateItem(bean);
        operationDeletedRecord(bean, 2);
        operationAddedRecord(bean, 2);
        editedGuiders.put(bean.getGuideId(), bean);
    }

    /**
     * 删除导游
     * 1.从数据源中移除
     * 2.从本地记录中删除
     * 3.删除待委派记录
     * 4.如果是网络的导游
     * -----------添加到删除记录中
     * -----------添加编辑记录中
     * --如果是本地的导游
     * ----------从删除记录中移除
     * ----------从编辑记录中移除
     */
    private void deleteRecord(GuiderListBean bean) {
        bean.setDelFlag("1");
        bean.setOpType("0");
        mAdapter.remove(getPositionByItem(mAdapter.getData(), bean));
        operationAddedRecord(bean, 0);
        needRequestRecord.remove(bean.getGuideId());
        if (!bean.getIsNativeGuider().equals("1")) {//删除网络
            operationDeletedRecord(bean, 1);
            editedGuiders.put(bean.getGuideId(), bean);
        } else {//删除本地
            operationDeletedRecord(bean, 0);
            if (editedGuiders.containsKey(bean.getGuideId())) {
                editedGuiders.remove(bean.getGuideId());
            }
        }
    }

    /**
     * 增加本地记录
     * 判断记录是否已存在  和数据源比较
     * 如果已存在,toast,不做其他操作
     * 如果不存在
     * 1.数据源中添加
     * 2.本地记录中添加
     * 3.删除记录中移除
     * 4.编辑记录中添加或更新
     */
    private void addRecord(GuiderListBean bean) {
        if (getPositionByItem(mAdapter.getData(), bean) == -1) {
            mAdapter.addData(0, bean);
            operationAddedRecord(bean, 1);
            operationDeletedRecord(bean, 0);
            editedGuiders.put(bean.getGuideId(), bean);
            rvGuiderList.scrollToPosition(0);
        } else {
            ToastUtils.showToast(App.getResourceString(R.string.hint_guider_exist));
        }
    }

    //-----------------operation开头的方法是对单个数据源的操作----------------------------------
    //从本地的添加记录中移除,0移除,1添加,2更新
    private void operationAddedRecord(GuiderListBean bean, int opType) {
        if (opType == 0 || opType == 2) {
            if (addedGuiders.containsKey(bean.getGuideId())) {
                switch (opType) {
                    case 0://移除
                        addedGuiders.remove(bean.getGuideId());
                        break;
                    case 2://更新
                        addedGuiders.put(bean.getGuideId(), bean);
                        break;
                }
            }
        } else if (opType == 1) {//添加
            addedGuiders.put(bean.getGuideId(), bean);
        }
    }

    //从删除记录中移除,0移除,1添加,2更新
    private void operationDeletedRecord(GuiderListBean bean, int opType) {
        if (opType == 0 || opType == 2) {
            if (deletedGuiders.containsKey(bean.getGuideId())) {
                switch (opType) {
                    case 0://移除
                        deletedGuiders.remove(bean.getGuideId());
                        break;
                    case 2://更新
                        deletedGuiders.put(bean.getGuideId(), bean);
                        break;
                }
            }
        } else if (opType == 1) {//添加
            deletedGuiders.put(bean.getGuideId(), bean);
        }
    }
}
