package com.msy.globalaccess.widget.popupwindow;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.msy.globalaccess.R;
import com.msy.globalaccess.business.adapter.SearchTravelAdapter;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.holder.TravelHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuDebin on 2017/5/15.
 * <p>
 * example:SearchTravelPopWindow searchTeamTravelPopWindow =new SearchTravelPopWindow(TourismMainActivity.this, new SearchTravelPopWindow.SelectedTouristCallBack() {
 *
 * @Override public void confirmSelected(TravelAgentListBean travelAgentListBean) {
 * }
 * });
 * searchTeamTravelPopWindow.show(View);
 */

public class SearchTravelPopWindow {

    private CustomPopWindow popWindow;

    private AppCompatEditText etSearchTravel;

    private RecyclerView rvTravel;

    private AppCompatTextView tvConfirm;

    private SearchTravelAdapter mAdapter;

    private SelectedTravelCallBack callBack;

    /**
     * 当前被选中的
     */
    private TravelAgentListBean currentTravel = TravelHelper.getInstance().getTravelAll().get(0);

    private TravelAgentListBean currentTravelCache = currentTravel;

    private List<TravelAgentListBean> currentList;


    public SearchTravelPopWindow(Context context, final SelectedTravelCallBack callBack) {
        this.callBack = callBack;
        popWindow = new CustomPopWindow
                .PopupWindowBuilder(context)
                .setView(R.layout.popup_team_travel)
                .enableBackgroundDark(true)
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        currentList = setTravelUnCheckedConfim(TravelHelper.getInstance().getTravelAll());
                        callBack.dismissTravel();
                    }
                }).create();
        initPopup();
    }

    public void initPopup() {
        View contentView = popWindow.getContentView();
        if (contentView != null) {
            etSearchTravel = (AppCompatEditText) contentView.findViewById(R.id.et_search_travel);
            rvTravel = (RecyclerView) contentView.findViewById(R.id.rv_popup_travel);
            tvConfirm = (AppCompatTextView) contentView.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentTravel = currentTravelCache;
                    callBack.confirmSelectedTravel(currentTravel);
                    currentList = getDifferentAddressList(mAdapter.getData());
                    popWindow.dissmiss();
                }
            });
            rvTravel.setHasFixedSize(true);
            final LinearLayoutManager manager = new LinearLayoutManager(rvTravel.getContext(), LinearLayoutManager.VERTICAL, false);
            rvTravel.setLayoutManager(manager);
            mAdapter = new SearchTravelAdapter(R.layout.item_popup_travel, setTravelUnCheckedConfim(TravelHelper.getInstance().getTravelAll()));
            rvTravel.setAdapter(mAdapter);
            rvTravel.addOnItemTouchListener(new OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    currentTravelCache = (TravelAgentListBean) adapter.getData().get(position);
                    setTravelUnChecked(adapter.getData());
                    mAdapter.notifyDataSetChanged();
                }
            });
            etSearchTravel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mAdapter.setNewData(setTravelUnChecked(TravelHelper.getInstance().getKeyList(s.toString())));
                }
            });
        }
    }

    public List<TravelAgentListBean> setTravelUnCheckedConfim(List<TravelAgentListBean> data) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (currentTravel.getTravelAgentName().equals(data.get(i).getTravelAgentName())) {
                    data.get(i).setChecked(true);
                } else {
                    data.get(i).setChecked(false);
                }
            }
        }
        return data;
    }
    public List<TravelAgentListBean> setTravelUnChecked(List<TravelAgentListBean> data) {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (currentTravelCache.getTravelAgentName().equals(data.get(i).getTravelAgentName())) {
                    data.get(i).setChecked(true);
                } else {
                    data.get(i).setChecked(false);
                }
            }
        }
        return data;
    }

    /**
     * 清除当前选中数据
     */
    public void clearCurrentList() {
        if (currentList != null) {
            currentList = null;
        }
        currentTravel = TravelHelper.getInstance().getTravelAll().get(0);
        currentTravelCache = currentTravel;
    }

    public void showAsDropDown(View view) {
        if (currentList != null) {
            mAdapter.setNewData(getDifferentAddressList(currentList));
        } else {
            mAdapter.setNewData(setTravelUnCheckedConfim(TravelHelper.getInstance().getTravelAll()));
        }
        popWindow.showAsDropDown(view);
    }

    public ArrayList<TravelAgentListBean> getDifferentAddressList(List<TravelAgentListBean> list) {
        ArrayList<TravelAgentListBean> travelAgentListBeen = null;
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        ObjectOutputStream stream;
        try {
            stream = new ObjectOutputStream(byteout);
            stream.writeObject(list);
            ByteArrayInputStream bytein = new ByteArrayInputStream(byteout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bytein);
            travelAgentListBeen = (ArrayList<TravelAgentListBean>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return travelAgentListBeen;
    }

    public interface SelectedTravelCallBack {
        void confirmSelectedTravel(TravelAgentListBean travelAgentListBean);

        void dismissTravel();
    }
}
