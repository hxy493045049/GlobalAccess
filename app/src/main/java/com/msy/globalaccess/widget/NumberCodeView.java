package com.msy.globalaccess.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.msy.globalaccess.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * Created by shawn on 2016/5/31.
 */
public class NumberCodeView extends RelativeLayout implements AdapterView.OnItemClickListener {

    private final static int NUMBER_BUTTON_DELETE = 11;
    private final static int NUMBER_BUTTON_ZERO = 10;//0号按键
    private final static int NUMBER_BUTTON_CLEAR = 9;//清除按键
    private final static int NUMBER_COUNT = 6;

    private final static String PASSWORD_NUMBER_SYMBOL = "●";

    private Context mContext;
    private ExpandGridView mNumbersGridView;

    private boolean mIsPassword;
    private boolean mIsDialog;

    private TextView mNumber1TextView;
    private TextView mNumber2TextView;
    private TextView mNumber3TextView;
    private TextView mNumber4TextView;
    private TextView mNumber5TextView;
    private TextView mNumber6TextView;
    private TextView mResultTextView;

    private TextView tv_payment_hint;
    private TextView tv_payment_forget;

    private ImageView iv_close;

    private RelativeLayout rl_input;
    private RelativeLayout rl_layout;
    private RelativeLayout ll_input_title;
    private LinearLayout ll_top_content;

    private Stack<Integer> mNumberStack;
    private List<TextView> mNumberViewList;

    private OnInputNumberCodeCallback mCallback;


    public NumberCodeView(Context context) {
        this(context, null);
    }

    public NumberCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mNumberStack = new Stack<>();
        mNumberViewList = new ArrayList<>();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NumberCodeView);
        mIsPassword = array.getBoolean(R.styleable.NumberCodeView_isPassword, false);
        mIsDialog = array.getBoolean(R.styleable.NumberCodeView_isDialog, false);
        initView();
        initListener();
    }

    private void initView() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_input_group_code, null);
        rl_input = (RelativeLayout) view.findViewById(R.id.rl_input);
        rl_layout = (RelativeLayout) view.findViewById(R.id.rl_layout);
        ll_input_title = (RelativeLayout) view.findViewById(R.id.ll_input_title);
        ll_top_content = (LinearLayout) view.findViewById(R.id.ll_top_content);
        mNumbersGridView = (ExpandGridView) view.findViewById(R.id.numbers_gridView);
        if (mIsDialog) {
            ll_input_title.setVisibility(View.VISIBLE);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rl_layout.setLayoutParams(layoutParams);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .MATCH_PARENT);
            params.addRule(RelativeLayout.BELOW, R.id.ll_top_content);
            params.setMargins(0, DisplayUtil.dip2px(10), 0, 0);
            mNumbersGridView.setLayoutParams(params);
            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .WRAP_CONTENT));
        } else {
            ll_input_title.setVisibility(View.GONE);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            ll_top_content.setLayoutParams(params);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mNumbersGridView.setLayoutParams(layoutParams);
            addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                    .MATCH_PARENT));
        }
        mNumbersGridView.setAdapter(new NumbersAdapter());
        mNumbersGridView.setOnItemClickListener(this);
        mResultTextView = (TextView) view.findViewById(R.id.input_code_result_textView);
        mNumber1TextView = (TextView) view.findViewById(R.id.number_1_textView);
        mNumber2TextView = (TextView) view.findViewById(R.id.number_2_textView);
        mNumber3TextView = (TextView) view.findViewById(R.id.number_3_textView);
        mNumber4TextView = (TextView) view.findViewById(R.id.number_4_textView);
        mNumber5TextView = (TextView) view.findViewById(R.id.number_5_textView);
        mNumber6TextView = (TextView) view.findViewById(R.id.number_6_textView);
        tv_payment_hint = (TextView) view.findViewById(R.id.tv_payment_hint);
        tv_payment_forget = (TextView) view.findViewById(R.id.tv_payment_forget);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        mNumberViewList.add(mNumber1TextView);
        mNumberViewList.add(mNumber2TextView);
        mNumberViewList.add(mNumber3TextView);
        mNumberViewList.add(mNumber4TextView);
        mNumberViewList.add(mNumber5TextView);
        mNumberViewList.add(mNumber6TextView);
    }

    public void initListener() {
        tv_payment_forget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onForget();
            }
        });
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onClose();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mResultTextView.setVisibility(GONE);
        if (position == NUMBER_BUTTON_CLEAR) {
            restoreViews();
            return;
        }
        if (position == NUMBER_BUTTON_DELETE) {
            if (mNumberStack.empty() || mNumberStack.size() > NUMBER_COUNT) {
                return;
            }
            //            mNumberStack.pop();
            mNumberStack.remove(mNumberStack.size() - 1);
        } else {
            if (mNumberStack.size() < NUMBER_COUNT) {
                if (position == NUMBER_BUTTON_ZERO) {
                    mNumberStack.push(0);
                } else {
                    mNumberStack.push(++position);
                }
            } else {
                return;
            }
        }
        refreshNumberViews();
        //input 6 numbers complete
        if (mNumberStack.size() == NUMBER_COUNT) {
            StringBuilder codeBuilder = new StringBuilder();
            for (int number : mNumberStack) {
                codeBuilder.append(number);
            }
            if (mCallback != null)
                mCallback.onResult(codeBuilder.toString());
        }
    }

    public void restoreViews() {
        mNumberStack.clear();
        refreshNumberViews();
        mResultTextView.setVisibility(GONE);
    }

    private void refreshNumberViews() {
        for (int i = 0, size = mNumberViewList.size(); i < size; i++) {
            int numSize = mNumberStack.size();
            if (i < numSize) {
                if (mIsPassword) {
                    mNumberViewList.get(i).setText(PASSWORD_NUMBER_SYMBOL);
                } else {
                    mNumberViewList.get(i).setText(String.valueOf(mNumberStack.get(i)));
                }

            } else {
                mNumberViewList.get(i).setText(" ");
            }
        }
    }

    public void setNumberCodeCallback(OnInputNumberCodeCallback callback) {
        this.mCallback = callback;
    }

    public void setHintText(String hint) {
        tv_payment_hint.setText(hint);
    }

    public void setHintColor(int color) {
        tv_payment_hint.setTextColor(color);
    }

    public void setHintVisible(int visible) {
        tv_payment_hint.setVisibility(visible);
    }

    public void setForgetText(String text) {
        tv_payment_forget.setText(text);
    }

    public void setForgetColor(int color) {
        tv_payment_forget.setTextColor(color);
    }

    public void setForgetVisible(int visible) {
        tv_payment_forget.setVisibility(visible);
    }

    public interface OnInputNumberCodeCallback {
        void onResult(String code);

        void onForget();

        void onClose();
    }

    private static class ItemHolder {
        RelativeLayout mRootView;
        TextView mNumberTextView;
        ImageView mDeleteImageView;
    }

    private class NumbersAdapter extends BaseAdapter {

        private String mNumbers = "123456789C0#";

        @Override
        public int getCount() {
            return mNumbers.length();
        }

        @Override
        public String getItem(int position) {
            return String.valueOf(mNumbers.charAt(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder itemHolder;
            if (convertView == null) {
                itemHolder = new ItemHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_item_view_input_group_code,
                        null);
                itemHolder.mRootView = (RelativeLayout) convertView.findViewById(R.id.number_root_view);
                itemHolder.mNumberTextView = (TextView) convertView.findViewById(R.id.number_textView);
                itemHolder.mDeleteImageView = (ImageView) convertView.findViewById(R.id.number_delete_imageView);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ItemHolder) convertView.getTag();
            }
            String curNumber = getItem(position);
            if ("C".equals(curNumber)) {
                itemHolder.mDeleteImageView.setVisibility(GONE);
                itemHolder.mNumberTextView.setVisibility(VISIBLE);
                itemHolder.mNumberTextView.setText(curNumber);
                //                itemHolder.mRootView.setBackgroundColor(getResources().getColor(R.color.back_gray));
                itemHolder.mRootView.setBackgroundResource(R.drawable.selector_payment_delete);
            } else if ("#".equals(curNumber)) {
                //                itemHolder.mRootView.setBackgroundColor(getResources().getColor(R.color.back_gray));
                itemHolder.mRootView.setBackgroundResource(R.drawable.selector_payment_delete);
                itemHolder.mNumberTextView.setVisibility(GONE);
                itemHolder.mDeleteImageView.setVisibility(VISIBLE);
            } else {
                itemHolder.mRootView.setBackgroundResource(R.drawable.selector_list);
                itemHolder.mDeleteImageView.setVisibility(GONE);
                itemHolder.mNumberTextView.setVisibility(VISIBLE);
                itemHolder.mNumberTextView.setText(curNumber);
            }
            return convertView;
        }
    }
}
