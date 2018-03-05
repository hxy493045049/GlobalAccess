package com.msy.globalaccess.business.travelAgency.search.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.travelAgency.search.contract.SearchContract;
import com.msy.globalaccess.business.travelAgency.search.contract.impl.SearchPresenterImpl;
import com.msy.globalaccess.data.bean.base.CurrencyBean;
import com.msy.globalaccess.data.bean.search.PerequisiteBean;
import com.msy.globalaccess.data.bean.search.SearchTeamTypeBean;
import com.msy.globalaccess.data.bean.search.SearchValueListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.bean.TravelDepBean;
import com.msy.globalaccess.data.holder.SearchPerequisiteHolder;
import com.msy.globalaccess.data.holder.TravelHelper;
import com.msy.globalaccess.utils.MapUtils;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.DateTimePickDialog;
import com.msy.globalaccess.widget.dialog.MultilevelWheelDialog;
import com.msy.globalaccess.widget.dialog.SmallDialog;
import com.msy.globalaccess.widget.dialog.WheelViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.msy.zc.commonutils.RegularUtils;
import cn.msy.zc.commonutils.StringUtils;

/**
 * 结算查询
 * Created by chensh on 2017/1/20 0020.
 */

public class SearchActivity extends BaseActivity implements SearchContract.View, WheelViewDialog
        .onWheelViewPickedListener, MultilevelWheelDialog.MultilevelWheelListener {
    /**
     * 查询类型: 1 团队查询、 2  结算查询、3 游客查询
     */
    @SearchResultActivity.SEARCH_TYPE
    public static int SEARCH_TYPE = 1;
    @Inject
    public TravelDepBean travelDepBean;//部门数据
    /**
     * 标题
     */
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView AppCompatTextView;
    /**
     * 返回按钮
     */
    @BindView(R.id.imgBtnBack)
    AppCompatImageButton back;
    /**
     * 添加条件
     */
    @BindView(R.id.settlement_lin_add)
    LinearLayout addView;//添加查询条件
    /**
     * 重置按钮
     */
    @BindView(R.id.settlement_reset)
    TextView resetView;
    /**
     * 提交按钮
     */
    @BindView(R.id.settlement_submit)
    TextView submit;
    /**
     * 查询条件布局
     */
    @BindView(R.id.search)
    LinearLayout search;
    @Inject
    SearchPresenterImpl presenter;
    @Inject
    SearchTeamTypeBean searchTeamTypeBean;//团队类型
    /**
     * 默认显示文字
     */
    private Object defaultBean;
    /**
     * 帮助类
     */
    private SearchPerequisiteHolder searchHolder;
    /**
     * 筛选条件Map
     */
    private Map<String, PerequisiteBean> perequisiteMap;
    /**
     * 参数集合
     */
    private HashMap<String, View> paramValue;
    /**
     * 旅行社dialog
     */
    private WheelViewDialog travelwheelViewDialog;
    private SmallDialog smallDialog;
    private ArrayList<SearchValueListBean> searchvaluelist = new ArrayList<>();//支出/收入数据集合
    /**
     * 团队id
     */
    private String teamId;
    /**
     * 是否为旅游局角色
     */
    private boolean isTravelAgent = false;
    /**
     * 旅行社搜索内柔
     */
    private String searchEdtText = "";
    private int searchPosition;

    public static void callActivity(Context context, int searchTpey) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("type", searchTpey);
        context.startActivity(intent);
    }


    public static void callActivity(Context context, int searchTpey, String teamId) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("type", searchTpey);
        intent.putExtra("teamId", teamId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = presenter;
    }

    @Override
    protected void init() {
        paramValue = new HashMap<>();
        AppCompatTextView.setVisibility(View.VISIBLE);
        //得到筛选条件数组
        searchHolder = SearchPerequisiteHolder.getInstance();
        smallDialog = new SmallDialog(this, getString(R.string.small_dialog));
        initIntent();
        initData();
    }

    private void initData() {
        //判断类型 请求不同的数据
        perequisiteMap = searchHolder.getPerequisiteBeen(SEARCH_TYPE);
        if (SEARCH_TYPE == SearchResultActivity.TEAM_SEARCH) {
            if ("2".equals(App.userHelper.getUser().getUserRoleType())) {
                isTravelAgent = true;
                presenter.loadTravelAgentSearch();
            } else if ("0".equals(App.userHelper.getUser().getUserRoleType()) && StringUtils.isEmpty(App.userHelper
                    .getUser().getUserDepName())) {
                presenter.loadTravelDep("");
            }
            AppCompatTextView.setText(R.string.settlement_query_team);
            presenter.loadTeamType();
            defaultBean = perequisiteMap.get(searchHolder.TeamCode);
        } else if (SEARCH_TYPE == SearchResultActivity.SETTLEMENT_SEARCH) {
            AppCompatTextView.setText(R.string.settlement_detail_query);
            loadUnit();
            defaultBean = perequisiteMap.get(searchHolder.TeamCode);
        } else if (SEARCH_TYPE == SearchResultActivity.TOURIST_SEARCH) {
            AppCompatTextView.setText(R.string.search_ciceroniInfo);
            defaultBean = perequisiteMap.get(searchHolder.cardType);
        }
        if (perequisiteMap == null) {
            return;
        }
        addPrerequisiteView(defaultBean);
    }

    /**
     * 获取传参
     */
    @SuppressWarnings("WrongConstant")
    private void initIntent() {
        SEARCH_TYPE = getIntent().getIntExtra("type", SearchResultActivity.TEAM_SEARCH);
        teamId = getIntent().getStringExtra("teamId");
    }

    @OnClick(R.id.imgBtnBack)
    public void back() {
        SearchActivity.this.finish();
    }

    //重置查询条件
    @OnClick(R.id.settlement_reset)
    public void reset() {
        if (perequisiteMap == null) {
            return;
        }
        searchEdtText = "";
        searchPosition = 0;
        search.removeAllViews();
        paramValue.clear();
        addPrerequisiteView(defaultBean);
    }

    /**
     * 添加查询条件
     */
    @OnClick(R.id.settlement_lin_add)
    public void addView() {
        if (perequisiteMap == null) {
            return;
        }
        if (perequisiteMap.size() == paramValue.size()) {
            ToastUtils.showToast("已添加所有条件");
            return;
        }
        String title = "";
        Set<String> set1 = perequisiteMap.keySet();
        Iterator<String> it = set1.iterator();
        while (it.hasNext()) {
            String obj = it.next();
            if (!paramValue.containsKey(obj)) {
                title = obj;
            }
        }
        addPrerequisiteView(perequisiteMap.get(title));
    }


    /**
     * 添加查询条件
     */
    public void addPrerequisiteView(Object bean) {
        View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_settlement_prerequisite, null);
        search.addView(view);
        initPrerequisiteView(view, bean);
    }

    /**
     * 初始化条件View
     *
     * @param mainview 条件View
     */
    private void initPrerequisiteView(final View mainview, final Object bean) {
        final TextView test = (TextView) mainview.findViewById(R.id.settlement_text1);
        AppCompatImageView delete = (AppCompatImageView) mainview.findViewById(R.id.settement_delete);
        final EditText editText = (EditText) mainview.findViewById(R.id.editText);
        final TextView textView2 = (TextView) mainview.findViewById(R.id.settlement_text2);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search.getChildCount() > 0) {
                    if (search.getChildCount() == 1) {
                        ToastUtils.showToast("默认添加一条筛选条件");
                    } else {
                        if (test.getTag() != null) {
                            if (test.getTag() instanceof PerequisiteBean) {
                                paramValue.remove(((PerequisiteBean) test.getTag()).getParam());
                            }
                        }
                        search.removeView(mainview);
                    }
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test.getTag() != null && test.getTag() instanceof PerequisiteBean) {
                    if (searchHolder.DATE.equals((((PerequisiteBean) test.getTag())).getKey())) {
                        String data = textView2.getText().toString();
                        DateTimePickDialog dateTimePickDialog;
                        if (!StringUtils.isEmpty(data)) {
                            dateTimePickDialog = new DateTimePickDialog(SearchActivity.this, data, DateTimePickDialog
                                    .TYPE_SIMPLIFY);
                        } else {
                            dateTimePickDialog = new DateTimePickDialog(SearchActivity.this, DateTimePickDialog
                                    .TYPE_SIMPLIFY);
                        }
                        dateTimePickDialog.buildDialog(textView2);
                        dateTimePickDialog.show();
                    } else {
                        showDialog(textView2, (PerequisiteBean) test.getTag());
                    }
                }
            }
        });
        if (bean instanceof PerequisiteBean) {
            ViewVisib((PerequisiteBean) bean, editText, textView2);
            paramValue.put(((PerequisiteBean) bean).getParam(), mainview);
            test.setText(((PerequisiteBean) bean).getName());
            test.setTag(bean);
        }
        test.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(final View v) {
                ArrayList<PerequisiteBean> list = MapUtils.convertList(perequisiteMap);
                //初始化pop
                if (list != null && list.size() > 0) {
                    try {
                        new WheelViewDialog(SearchActivity.this, list, test, new WheelViewDialog
                                .onWheelViewPickedListener() {
                            @Override
                            public void onPicked(Object pickedItem, View view) {
                                if (paramValue.size() == 1) {
                                    paramValue.clear();
                                }
                                if (pickedItem instanceof PerequisiteBean) {
                                    PerequisiteBean perequisiteBean = ((PerequisiteBean) pickedItem);
                                    if (paramValue.containsKey(perequisiteBean.getParam())) {
                                        ToastUtils.showToast("已添加此条件");
                                        return;
                                    }
                                    if (test.getTag() != null) {
                                        PerequisiteBean tag = (PerequisiteBean) test.getTag();
                                        paramValue.remove(tag.getParam());
                                    }
                                    test.setText(perequisiteBean.getName());
                                    test.setTag(pickedItem);
                                    ViewVisib(perequisiteBean, editText, textView2);
                                    paramValue.put(perequisiteBean.getParam(), mainview);
                                    Log.i("", "" + searchHolder.paramMap.size());
                                }
                            }
                        }).show();
                    } catch (Exception e) {
                        Log.i("LoginPre", "   e    " + e.toString());
                    }
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void showDialog(final TextView textView2, PerequisiteBean object) {
        if (searchHolder.SearchType.equals(object.getParam())) {
            new WheelViewDialog(SearchActivity.this, searchHolder.departmentList, textView2, this).show();
        } else if (searchHolder.TeamStatus.equals(object.getParam())) {
            new WheelViewDialog(SearchActivity.this, searchHolder.StatusList, textView2, this).show();
        } else if (searchHolder.TeamTypeId.equals(object.getParam())) {
            ArrayList<SearchTeamTypeBean.TeamTypeListBean> typeList = searchTeamTypeBean.getTeamTypeList();
            if (typeList == null) {
                presenter.loadTeamType();
                return;
            }
            new WheelViewDialog(SearchActivity.this, typeList, textView2, this).show();
        } else if (searchHolder.TravelDepId.equals(object.getParam())) {
            ArrayList<TravelDepBean.TravelDepListBean> travelList = travelDepBean.getTravelDepList();
            if (travelList == null) {
                presenter.loadTravelDep("");
                return;
            }
            new WheelViewDialog(SearchActivity.this, travelList, textView2, this).show();
        } else if (searchHolder.inComeUnitCoding.equals(object.getParam()) || searchHolder.outComeUnitCoding.equals
                (object.getParam())) {
            if (searchvaluelist == null) {
                loadUnit();
                return;
            }
            new MultilevelWheelDialog(SearchActivity.this, searchvaluelist, null, null, this, textView2).show();
        } else if (searchHolder.cardType.equals(object.getParam())) {
            new WheelViewDialog(SearchActivity.this, searchHolder.CertificateList, textView2, this).show();
        } else if (searchHolder.sex.equals(object.getParam())) {
            new WheelViewDialog(SearchActivity.this, searchHolder.SexList, textView2, this).show();
        } else if (isTravelAgent && searchHolder.TravelAgentId.equals(object.getParam())) {
            travelwheelViewDialog = new WheelViewDialog(SearchActivity.this, TravelHelper.getInstance().getTravelAll
                    (), textView2, this);
            travelwheelViewDialog.setSearchShow(true, searchEdtText, searchPosition);
            travelwheelViewDialog.show();
        }
    }

    //请求支出/收入单位数据
    private void loadUnit() {
        searchvaluelist.clear();
        smallDialog.shows();
        presenter.loadTravelAgentSearch();//旅行社数据
        presenter.loadScenicSearch();//景区
        presenter.loadHotelSearch();//酒店
        presenter.loadVehicleSearch();//车队
        presenter.loadInsuranceSearch();//保险公司
    }


    /**
     * 根据不同的类型显示不同的控件
     *
     * @param perequisiteBean 标记
     * @param editText        输入框
     * @param textView        文本
     */
    private void ViewVisib(PerequisiteBean perequisiteBean, EditText editText, TextView textView) {
        String key = perequisiteBean.getKey();
        if (searchHolder.EDIT.equals(key)) {
            editText.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            textView.setText("");
            editText.setText("");
            if (searchHolder.cardNum.equals(perequisiteBean.getParam())) {
                editText.setHint("请输入证件号码");
                changInputSta(editText);
            } else if (searchHolder.phoneNum.equals(perequisiteBean.getParam())) {
                editText.setHint("请输入手机号码");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (searchHolder.name.equals(perequisiteBean.getParam())) {
                editText.setHint("请输入姓名");
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (searchHolder.payCode.equals(perequisiteBean.getParam())) {
                editText.setHint("请输入支付编号");
                changInputSta(editText);
            } else {
                editText.setHint(getString(R.string.settlement_edit_hit));
                changInputSta(editText);
            }
        } else if (searchHolder.TEXT.equals(key) || searchHolder.DATE.equals(key)) {
            editText.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText("");
            editText.setText("");
        }
    }

    /**
     * 设置输入类型
     *
     * @param editText 输入框
     */
    private void changInputSta(EditText editText) {
        editText.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_TEXT_VARIATION_PASSWORD;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] data = getString(R.string.digits).toCharArray();
                return data;
            }
        });
    }

    /**
     * 获取到
     *
     * @param searchTeamTypeBean 团队类型
     */
    @Override
    public void getTeamType(SearchTeamTypeBean searchTeamTypeBean) {
        if (searchTeamTypeBean != null && searchTeamTypeBean.getTeamTypeList() != null) {
            this.searchTeamTypeBean = searchTeamTypeBean;
        }
    }

    @Override
    public void getTravelDep(TravelDepBean data) {
        //获取到部门数据
        if (data != null && data.getTravelDepList().size() > 0) {
            travelDepBean = data;
        }
    }

    @Override
    public void getScenicSearch(SearchValueListBean searchValueListBean) {
        searchvaluelist.add(searchValueListBean);
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
     * 提交查询
     */
    @OnClick(R.id.settlement_submit)
    public void submit() {
        Set<Map.Entry<String, View>> items = paramValue.entrySet();
        //转换为迭代器
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            LinearLayout val = (LinearLayout) entry.getValue();
            final EditText editText = (EditText) val.findViewById(R.id.editText);
            final TextView textView2 = (TextView) val.findViewById(R.id.settlement_text2);
            String key = entry.getKey().toString();
            if (editText.getVisibility() == View.GONE) {
                //根据key值来获取不同控件中保存的值
                if (!StringUtils.isEmpty(textView2.getText().toString())) {
                    if (searchHolder.paramMap.containsKey(key) && judgeKey(key)) {
                        //获取tag的值
                        searchHolder.paramMap.put(key, textView2.getTag().toString());
                    } else {
                        searchHolder.paramMap.put(key, textView2.getText().toString());
                    }
                } else {
                    ToastUtils.showToast("条件填写不完整");
                    return;
                }
            } else if (textView2.getVisibility() == View.GONE) {
                String value = editText.getText().toString();
                if (!StringUtils.isEmpty(value)) {
                    //判断手机格式
                    if (searchHolder.phoneNum.equals(key) && !RegularUtils.checkMobile(value)) {
                        ToastUtils.showToast("格式不正确");
                        return;
                    } else if (searchHolder.cardNum.equals(key) && "0".equals(searchHolder.paramMap.get(searchHolder
                            .cardType)) && !RegularUtils.checkIdCard(value)) {
                        //判断身份证格式
                        ToastUtils.showToast("身份证格式不正确");
                        return;
                    } else {
                        searchHolder.paramMap.put(key, value);
                    }
                } else {
                    ToastUtils.showToast("条件填写不完整");
                    return;
                }
            }
        }
        if (SEARCH_TYPE == SearchResultActivity.TOURIST_SEARCH) {
            searchHolder.paramMap.put(searchHolder.teamId, teamId);
        }
        SearchResultActivity.callActivity(SearchActivity.this, SEARCH_TYPE, searchHolder.paramMap);
        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
        cleanValue();
    }

    /**
     * 清除参数数组对应值
     */
    private void cleanValue() {
        for (Map.Entry<String, Object> mm : searchHolder.paramMap.entrySet()) {
            mm.setValue("");
        }
    }

    /**
     * 判断key值
     */
    private boolean judgeKey(String key) {
        boolean ismatching = false;
        if (searchHolder.cardType.equals(key) || searchHolder.SearchType.equals(key) ||
                searchHolder.TeamStatus.equals(key) || searchHolder.outComeUnitCoding.equals(key) ||
                searchHolder.inComeUnitCoding.equals(key) || searchHolder.sex.equals(key)
                || searchHolder.TravelDepId.equals(key) || searchHolder.TeamTypeId.equals(key)) {
            ismatching = true;
        } else if (isTravelAgent && searchHolder.TravelAgentId.equals(key)) {
            //旅游局角色
            ismatching = true;
        }
        return ismatching;
    }

    @Override
    public void onPicked(Object pickedItem, View view) {
        if (view instanceof TextView) {
            if (pickedItem instanceof PerequisiteBean) {
                //给筛条件复制
                ((TextView) view).setText(((PerequisiteBean) pickedItem).getName());
                view.setTag(((PerequisiteBean) pickedItem).getKey());
            } else if (pickedItem instanceof SearchTeamTypeBean.TeamTypeListBean) {
                //团队类型  取值状态
                ((TextView) view).setText(((SearchTeamTypeBean.TeamTypeListBean) pickedItem).getTeamTypeName());
                view.setTag(((SearchTeamTypeBean.TeamTypeListBean) pickedItem).getTeamTypeId());
            } else if (pickedItem instanceof TravelDepBean.TravelDepListBean) {
                //旅行社部门
                ((TextView) view).setText(((TravelDepBean.TravelDepListBean) pickedItem).getTravelDepName());
                view.setTag(((TravelDepBean.TravelDepListBean) pickedItem).getTravelDepId());
            } else if (pickedItem instanceof CurrencyBean) {
                //旅行社部门
                ((TextView) view).setText(((CurrencyBean) pickedItem).getName());
                view.setTag(((CurrencyBean) pickedItem).getId());
            } else if (pickedItem instanceof TravelAgentListBean) {
                //旅行社搜索
                TravelAgentListBean travelAgentListBean = ((TravelAgentListBean) pickedItem);
                searchEdtText = travelAgentListBean.getSearchText();
                searchPosition = travelAgentListBean.getSearchPosition();
                ((TextView) view).setText(travelAgentListBean.getTravelAgentName());
                view.setTag(travelAgentListBean.getTravelAgentId());
            }
        }
    }

    @Override
    public void onPicked(CurrencyBean searchValue, View view) {
        if (searchValue != null) {
            //收入/支出单位   取值/状态
            ((TextView) view).setText(searchValue.getName());
            view.setTag(searchValue.getId());
        }
    }
}
