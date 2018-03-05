package com.msy.globalaccess.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by hxy on 2016/7/28 0028.
 * description : 画一条水平的分割线于item顶部
 */
public class TopHorizontalDecoration extends RecyclerView.ItemDecoration {

    //通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATRRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;


    public TopHorizontalDecoration(Context context) {
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
    }


    public TopHorizontalDecoration(Context context, int splitId) {
        this(context, splitId, 255);
    }

    public TopHorizontalDecoration(Context context, int splitId, int splitAlpha) {
        try {
            mDivider = context.getResources().getDrawable(splitId);
        } catch (Exception e) {
            TypedArray ta = context.obtainStyledAttributes(ATRRS);
            this.mDivider = ta.getDrawable(0);
            ta.recycle();
        }
        mDivider.setAlpha(splitAlpha);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawTopDecoration(c, parent);
    }

    //不覆盖padding
    private void drawTopDecoration(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount(); i++) {
            drawDivider(c, mDivider, parent, i, left, right);
        }
    }

    private void drawDivider(Canvas c, Drawable drawable, RecyclerView rv, int position, int left, int right) {
        final View child = rv.getChildAt(position);
        //获得child的布局信息
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int bottom = child.getTop() + params.topMargin;
        final int top = bottom - drawable.getIntrinsicHeight();
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(c);
    }

    //由于Divider也有长宽高，每一个Item需要偏移一定大小
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, mDivider.getIntrinsicHeight(), 0, 0);
    }
}
