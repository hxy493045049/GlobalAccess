package com.msy.globalaccess.business.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.guider.GuiderListBean;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by hxy on 2017/3/15 0015.
 * <p>
 * description : 修改导游
 */
public class GuiderListAdapter extends BaseQuickAdapter<GuiderListBean, BaseViewHolder> {
    public static final String INTERNET_ITEM = "internetItem";//该数据是网络数据
    public static final String LOCAL_ITEM = "localItem";//该数据是本地数据
    public static final String EVENT_REQUEST = "request";//请求委派
    private OnCommitListener mCommitListener;

    public GuiderListAdapter(int resId, List<GuiderListBean> data) {
        super(resId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GuiderListBean item) {
        helper.setText(R.id.tvGuiderName, item.getName());
        helper.setText(R.id.tvGuiderPhone, item.getPhoneNum());
        helper.setText(R.id.tvGuiderIdentify, item.getGuideCode());
        helper.setText(R.id.tvTimeRange, item.getAppointStartDate() + " 至 " + item.getAppointEndDate());
        helper.setText(R.id.tvAffiliateUnit, item.getTravelAgentName());
        helper.addOnClickListener(R.id.btnDelegate);
        helper.addOnClickListener(R.id.btnDelete);
        AppCompatImageView iv_flag = helper.getView(R.id.iv_flag);
        if (item.getIsNativeGuider().equals("1")) {
            iv_flag.setVisibility(View.VISIBLE);
            iv_flag.setImageResource(R.mipmap.icon_added);
        } else if (item.getIsModified().equals("1")) {
            iv_flag.setVisibility(View.VISIBLE);
            iv_flag.setImageResource(R.mipmap.icon_modified);
        } else {
            iv_flag.setVisibility(View.GONE);
        }

        onHidenAllButton(helper);
        if (!checkValid(item)) {
            return;
        }
        dispatchGuiderStatus(helper, item);
    }

    private boolean checkValid(GuiderListBean item) {
        if (TextUtils.isEmpty(item.getIsOur())) {
            Logger.e("isOur为空");
            return false;
        }
        if (TextUtils.isEmpty(item.getMustAppoint())) {
            Logger.e("mustAppoint为空");
            return false;
        }
        if (TextUtils.isEmpty(item.getOpType())) {
            Logger.e("opType为空");
            return false;
        }
        if (TextUtils.isEmpty(item.getOpStatus())) {
            Logger.e("opStatus为空");
            return false;
        }
        return true;
    }

    /**
     * 1.如果是本社导游,显示删除按钮
     * <p>
     * 2.如果是外社导游
     * ``````a.需要委派
     * ``````````1.optype=0(委派申请)
     * ````````````````opstatus=0(处理中)   委派处理中,显示取消委派
     * ````````````````opstatus=1(未通过)   委派失败-------------------------------------这条数据不会返回到app
     * ````````````````opstatus=2(成功)    委派成功,显示取消委派按钮
     * ``````````2.optype=1(取消委派)
     * ````````````````opstatus=0(待处理)   不显示任何按钮,正在处理中
     * ````````````````opstatus=1(未通过)   不显示任何按钮,强制用户接受该导游
     * ````````````````opstatus=2(成功)    取消成功------------------------------------这条数据不会返回到app
     * ``````````3.optype=-1(初始状态)
     * * ````````````````显示委派按钮
     * ``````b.不需要委派  显示删除按钮
     */
    private void dispatchGuiderStatus(BaseViewHolder helper, GuiderListBean item) {
        switch (item.getIsOur()) {
            case "0"://外社导游
                handlerExteralGuiderStatus(helper, item);
                break;
            case "1"://本社导游
                onDelete(helper, item);
                break;
        }
    }

    /**
     * 处理外社导游的分支
     * 如果是外社导游  --> 不需要委派  --> 显示删除按钮
     */
    private void handlerExteralGuiderStatus(BaseViewHolder helper, GuiderListBean item) {
        switch (item.getMustAppoint()) {
            case "1"://需要委派
                handlerExteralDelegateStatus(helper, item);
                break;
            case "0"://不需要委派
                onDelete(helper, item);
                break;
        }
    }

    /**
     * 如果是外社导游 -->需要委派
     * ``````````1.optype=0(委派申请)
     * ````````````````opstatus=0(处理中)   委派处理中,显示取消委派
     * ````````````````opstatus=1(未通过)   委派失败,这条数据不会返回到app
     * ````````````````opstatus=2(成功)    委派成功,显示取消委派按钮
     * ``````````2.optype=1(取消委派)
     * ````````````````opstatus=0(待处理)   不显示任何按钮,正在处理中
     * ````````````````opstatus=1(未通过)   不显示任何按钮,强制用户接受该导游
     * ````````````````opstatus=2(成功)    取消成功,这条数据不会返回到app
     * ``````````3.optype=-1(初始状态)
     * * ````````````````显示委派按钮
     */
    //处理外社导游需要委派的情况
    private void handlerExteralDelegateStatus(BaseViewHolder helper, GuiderListBean item) {
        switch (item.getOpType()) {
            case "0": //请求委派
                switch (item.getOpStatus()) {
                    case "0":
                        onCancel(helper, item);
                        break;
                    case "1":
                        onHidenAllButton(helper);
                        break;
                    case "2":
                        onCancel(helper, item);
                        break;
                }
                break;
            case "1": //取消委派
                switch (item.getOpStatus()) {
                    case "0":
                        onHidenAllButton(helper);
                        break;
                    case "1":
                        onHidenAllButton(helper);
                        break;
                    case "2":
                        onHidenAllButton(helper);
                        break;
                }
                break;
            case "-1":  //-1 初始状态
                onRequest(helper, item);
                onDelete(helper, item);
                break;
        }
    }

    //删除
    private void onDelete(BaseViewHolder helper, GuiderListBean item) {

        AppCompatTextView btnDelete = helper.getView(R.id.btnDelete);
        btnDelete.setVisibility(View.VISIBLE);

        if (item.getIsNativeGuider().equals("1")) {
            btnDelete.setTag(LOCAL_ITEM);
        } else {
            btnDelete.setTag(INTERNET_ITEM);
        }
    }

    //请求委派
    private void onRequest(BaseViewHolder helper, GuiderListBean item) {
        AppCompatTextView btnDelegate = helper.getView(R.id.btnDelegate);
        btnDelegate.setVisibility(View.VISIBLE);
        btnDelegate.setText(App.getResourceString(R.string.guider_delegate));
        btnDelegate.setTag(EVENT_REQUEST);
        if (mCommitListener != null) {
            mCommitListener.getGuiderInfo(item);
        }
    }

    //取消委派
    private void onCancel(BaseViewHolder helper, GuiderListBean item) {
        AppCompatTextView btnDelegate = helper.getView(R.id.btnDelegate);
        btnDelegate.setVisibility(View.VISIBLE);
        btnDelegate.setText(App.getResourceString(R.string.guider_delegate_cancel));
        if (item.getIsNativeGuider().equals("1")) {
            btnDelegate.setTag(LOCAL_ITEM);
        } else {
            btnDelegate.setTag(INTERNET_ITEM);
        }
    }

    private void onHidenAllButton(BaseViewHolder helper) {
        helper.setVisible(R.id.btnDelete, false).setVisible(R.id.btnDelegate, false);
    }

    public void setOnRecordListener(OnCommitListener listener) {
        this.mCommitListener = listener;
    }

    /**
     * 用于通知activity,哪些导游是需要点击委派后,才能提交的,返回的string是导游证号
     */
    public interface OnCommitListener {
        void getGuiderInfo(GuiderListBean guiderCode);
    }
}
