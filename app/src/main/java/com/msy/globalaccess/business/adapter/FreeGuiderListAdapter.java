package com.msy.globalaccess.business.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.msy.globalaccess.R;
import com.msy.globalaccess.data.bean.guider.FreeGuiderListBean;

import java.util.List;

/**
 * Created by shawn on 2017/3/23 0023.
 * <p>
 * description :
 */
public class FreeGuiderListAdapter extends BaseQuickAdapter<FreeGuiderListBean, BaseViewHolder> {
    private int currentPos = -1;
    private int lastPos = -1;
    private String selectedGuiderId = "";
    private OnCheckCallBack mCheckCallBack;

    public FreeGuiderListAdapter(int layoutResId, List<FreeGuiderListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FreeGuiderListBean item) {
        helper.setText(R.id.tvGuiderName, item.getName())
                .setText(R.id.tvGuiderGender, item.getSex().equals("1") ? "男" : "女")
                .setText(R.id.tvGuiderIdentify, item.getGuideCode())
                .setText(R.id.tvAffiliateUnit, item.getTravelAgentName());

        final AppCompatCheckBox btnDelegate = helper.getView(R.id.btnDelegate);
        if (!selectedGuiderId.equals(item.getGuideId())) {
            btnDelegate.setChecked(false);
        } else {
            btnDelegate.setChecked(true);
        }
        btnDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(helper.getLayoutPosition(), item, btnDelegate);
            }
        });
    }

    public void setChecked(int position, FreeGuiderListBean item, AppCompatCheckBox btnDelegate) {
        selectedGuiderId = item.getGuideId();
        lastPos = currentPos;
        currentPos = position;
        btnDelegate.setChecked(true);
        if (lastPos != -1 && lastPos != currentPos) {
            notifyItemChanged(lastPos);
        }
        if (mCheckCallBack != null) {
            mCheckCallBack.onChecked(item);
        }
    }

    public void setOnCheckedCallBack(OnCheckCallBack callBack) {
        mCheckCallBack = callBack;
    }

    public interface OnCheckCallBack {
        void onChecked(FreeGuiderListBean selectedGuider);
    }
}
