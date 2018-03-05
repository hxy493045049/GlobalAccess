package com.msy.globalaccess.business.travelAgency.search.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.adapter.SearchAdapter;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchDelegateGuiderContract;
import com.msy.globalaccess.business.travelAgency.search.contract.impl.SearchDelegateGuiderPresenterImpl;
import com.msy.globalaccess.data.bean.search.SearchBean;
import com.msy.globalaccess.utils.helper.TimeHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.DateTimePickDialog;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;
import com.orhanobut.logger.Logger;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.TimeFormat;

/**
 * Created by shawn on 2017/7/3 0003.
 * <p>
 * description :
 */

public class SearchDelegateGuiderActivity extends BaseActivity implements SearchDelegateGuiderContract.View {

    @BindView(R.id.settlement_lin_add)
    LinearLayout llAddBtn;

    @BindView(R.id.rvSearch)
    RecyclerView rvSearch;

    @Inject
    SearchDelegateGuiderPresenterImpl mPresenter;

    @BindView(R.id.settlementReset)
    TextView settlementReset;

    @BindView(R.id.settlementSubmit)
    TextView settlementSubmit;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView tvToolbarTitle;
    private LinkedHashMap<String, SearchBean> currentCondition = new LinkedHashMap<>();
    private ArrayList<SearchBean> conditions = new ArrayList<>();
    private SearchAdapter mAdapter;
    private WheelViewDialog<SearchBean> conditionPicker;
    //正在操作的查询条件的下标
    private int operationPosition = -1;
    private DateTimePickDialog timePickDialog;
    private String[] str = App.getResource().getStringArray(R.array.touristAdminQueryCondition);

    public static void callActivity(Context context) {
        Intent intent = new Intent(context, SearchDelegateGuiderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search2;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        mPresenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = mPresenter;
    }

    @Override
    protected void init() {
        initRecyclerView();
        initListener();
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarTitle.setText(App.getResourceString(R.string.search_guider_title));
        timePickDialog = new DateTimePickDialog(this, DateTimePickDialog.TYPE_SIMPLIFY);
        timePickDialog.buildDialog();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @Override
    public void onQueryConditionCallback(List<SearchBean> data) {
        conditions = new ArrayList<>(data);
        reset();
        initConditionPicker();
    }

    @Override
    public List<SearchBean> getCurrentCondition() {
        return mAdapter.getData();
    }

    @Override
    public void onParamCallBack(HashMap<String, Object> param) {
        SearchResultActivity.callActivity(this, SearchResultActivity.DELEGATE_GUIDER_SEARCH, param);
    }

    //************************private************************************
    private void initConditionPicker() {
        if (conditions == null || conditions.size() == 0) {
            return;
        }
        conditionPicker = new WheelViewDialog<>(this, conditions, new WheelViewDialog
                .onWheelViewPickedListener1<SearchBean>() {
            @Override
            public void onPicked(SearchBean pickedItem, int position) {
                updateCondition(pickedItem);
            }
        });
        conditionPicker.setCanceledOnTouchOutside(true);
    }

    private void updateCondition(SearchBean pickedItem) {
        if (currentCondition.containsKey(pickedItem.getKey())) {
            ToastUtils.showToast(R.string.search_condition_existed);
        } else if (operationPosition >= 0) {
            //首先找到数据源中对应下标的bean
            SearchBean bean = mAdapter.getData().get(operationPosition);
            //将当前记录中的bean移除并加入心的bean,注意这里的位置,由于采用了linkedhashmap,暂时没有找到替换指定下标的key和value的方法,
            //所以这里采用先移除在添加,会导致新的数据在链表的最后,而不是在相同位置做替换
            currentCondition.remove(bean.getKey());
            currentCondition.put(pickedItem.getKey(), pickedItem);
            //替换数据源中指定下标的bean,并更新
            mAdapter.setData(operationPosition, pickedItem);
            redraw();
        } else {
            Logger.e("内部异常");
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(settlementReset.getWindowToken(), 0);
    }

    //重置
    private void reset() {
        if (conditions == null || conditions.size() == 0) {
            return;
        }
        //恢复默认状态
        for (SearchBean bean : conditions) {
            bean.setSelectedValue("");
        }
        currentCondition.clear();
        if (conditions.size() > 0) {
            currentCondition.put(conditions.get(0).getKey(), conditions.get(0));
        }
        mAdapter.setNewData(new ArrayList<>(currentCondition.values()));
        mAdapter.notifyDataSetChanged();
        hideKeyboard();
    }

    private void initRecyclerView() {
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setHasFixedSize(true);
        rvSearch.setItemAnimator(null);
        rvSearch.setAdapter(mAdapter = new SearchAdapter(R.layout.item_search, null));
    }

    private void initListener() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                SearchBean currentBean = (SearchBean) baseQuickAdapter.getData().get(i);
                operationPosition = i;
                switch (view.getId()) {
                    case R.id.fl_settlement_text1:
                        showQueryConditionChooser();
                        break;
                    case R.id.fl_settlement_text2:
                        handlerBehavior(currentBean, true);
                        break;
                    case R.id.fl_settlement_text3:
                        handlerBehavior(currentBean, false);
                        break;
                    case R.id.settement_delete:
                        currentBean.setSelectedValue("");
                        if(currentBean.getValueType()== SearchBean.TYPE_TEXT){
                            mAdapter.clearEditText();
                        }
                        if (currentCondition.size() == 1) {
                            ToastUtils.showToast(R.string.search_atleast_one);
                            mAdapter.notifyItemChanged(i);
                            return;
                        }
                        currentCondition.remove(currentBean.getKey());
                        mAdapter.remove(i);
                        redraw();
                        break;
                }
            }
        });

        llAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                //添加条件
                for (int i = 0; i < conditions.size(); i++) {
                    if (addContion(conditions.get(i))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    ToastUtils.showToast(App.getResourceString(R.string.search_all_condition_added));
                }
            }
        });

        settlementReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        settlementSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要将团队编号的内容记录下来
                mAdapter.saveDate();
                for (SearchBean bean : currentCondition.values()) {
                    if (bean.getKey().equals(str[3])) {
                        //创建日期:需要判断结束时间大于开始时间
                        String[] times = bean.getTimes();
                        if (times != null && times.length == 2) {//如果开始时间和结束时间同时添加了
                            String startTime = times[0];
                            String endTime = times[1];
                            if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)
                                    && TimeHelper.compareTime(startTime, TimeFormat.regular7, endTime, TimeFormat
                                    .regular7) < 0) {
                                ToastUtils.showToast(R.string.search_error_time);
                                return;
                            }
                        }
                    }

                    if (TextUtils.isEmpty(bean.getSelectedValue())) {
                        ToastUtils.showToast(R.string.search_error_condition);
                        return;
                    }
                }
                mPresenter.getQueryParam();
            }
        });
    }

    private void redraw() {
        rvSearch.requestLayout();
    }

    /**
     * 添加新的查询条件,如果该条件不在currentCondition中,那么加入,并且加入到adapter的数据源中,
     * 由于searchBean是同一个地址,所以在添加之前要清空该conditon的selectedvalue
     *
     * @param target 添加的目标
     * @return 添加成功, true, 失败false
     */
    private boolean addContion(SearchBean target) {
        if (!currentCondition.containsKey(target.getKey())) {
            target.setSelectedValue("");
            currentCondition.put(target.getKey(), target);
            mAdapter.addData(target);
            redraw();
            return true;
        }
        return false;
    }

    //处理查询行为,是显示时间dialog还是显示item dialog
    private void handlerBehavior(SearchBean searchBean, boolean startTime) {
        switch (searchBean.getValueType()) {
            //这里判断是响应文本输入还是响应选择输入
            case SearchBean.TYPE_MULTI_TIME:
                showTimeDialog(searchBean, startTime);
                break;
            case SearchBean.TYPE_SCROLL:
                showItemChooser(searchBean);
                break;
            case SearchBean.TYPE_TIME:
                break;
            case SearchBean.TYPE_TEXT:
                break;
        }
    }

    //显示查询条件选择器
    private void showQueryConditionChooser() {
        if (conditionPicker != null) {
            conditionPicker.show();
        }
    }

    //显示查询内容选择器
    private void showItemChooser(final SearchBean searchBean) {
        if (searchBean.getValues() == null || searchBean.getValues().size() == 0) {
            return;
        }
        WheelViewDialog<String> dialog = new WheelViewDialog<>(this, searchBean.getValues(),
                new WheelViewDialog.onWheelViewPickedListener1<String>() {
                    @Override
                    public void onPicked(String pickedItem, int position) {
                        searchBean.setSelectedValue(pickedItem);
                        mAdapter.notifyItemChanged(operationPosition);
                    }
                });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    //显示查询时间选择器
    private void showTimeDialog(final SearchBean searchBean, final boolean startTime) {
        timePickDialog.setCallBack(new DateTimePickDialog.CallBack() {
            @Override
            public void doSomething(String dateTime) {
                if (startTime) {
                    searchBean.setStartTime(dateTime);
                } else {
                    searchBean.setEndTime(dateTime);
                }
                mAdapter.notifyItemChanged(operationPosition);
            }
        });
        timePickDialog.show();
    }


}
