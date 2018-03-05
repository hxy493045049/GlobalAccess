package com.msy.globalaccess.business.travelAgency.search;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseContract;
import com.msy.globalaccess.base.BaseFragment;
import com.msy.globalaccess.business.adapter.AdapterPopuList;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.popupwindow.MyListPopuWindow;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.DisplayUtil;

/**
 * 查询条件fragment
 * Created by chensh on 2017/1/24 0024.
 * 已经无用
 */

@Deprecated
public class SearchExaFragment extends BaseFragment {

    //模拟数据
    private ArrayList<String> spinnerList = new ArrayList<>();

    @BindView(R.id.fragment_search)
    LinearLayout fragment_search;

    /*添加测试数据*/
    private void initData() {
        spinnerList.add("团队编号");
        spinnerList.add("处理类型");
        spinnerList.add("业务状态");
        spinnerList.add("创建日期");
        spinnerList.add("待处理业务");
    }

    private int popHeight;

    @Inject
    public SearchExaFragment() {

    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void init(View view) {
        popHeight = DisplayUtil.getScreenHeight() / 3;
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_exa_search;
    }

    @Override
    public BaseContract.Presenter getBasePresenter() {
        return null;
    }


    /**
     * 添加查询条件
     */
    public void addPrerequisiteView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_settlement_prerequisite, null);
        fragment_search.addView(view);
        initPrerequisiteView(view);
    }

    /**
     * 清除所有条件
     */
    public void removAllView() {
        fragment_search.removeAllViews();
    }

    /**
     * 初始化条件View
     *
     * @param view 条件View
     */
    private void initPrerequisiteView(final View view) {
        //初始化pop
        final MyListPopuWindow mListPop = new MyListPopuWindow(getActivity());
        final TextView test = (TextView) view.findViewById(R.id.settlement_text1);
        if (spinnerList != null && spinnerList.size() > 0) {
            test.setText(spinnerList.get(0));
        }
        TextView delete = (TextView) view.findViewById(R.id.settement_delete);
        EditText editText = (EditText) view.findViewById(R.id.editText);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment_search.getChildCount() > 0) {
                    fragment_search.removeView(view);
                }
            }
        });
        mListPop.setAdapter(new AdapterPopuList(getActivity(), spinnerList));
        mListPop.initView(LinearLayout.LayoutParams.WRAP_CONTENT, popHeight, test, true);
        //显示Pop
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListPop.show(v);
            }
        });
        //pop的点击事件
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showToast(spinnerList.get(position));
                test.setText(spinnerList.get(position));
                mListPop.dismiss();
            }
        });
    }
}
