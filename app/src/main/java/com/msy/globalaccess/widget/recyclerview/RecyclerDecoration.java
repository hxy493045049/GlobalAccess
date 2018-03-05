package com.msy.globalaccess.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxy on 2016/7/28 0028.
 * description : recycler的分隔符,适用于线性布局
 */
public class RecyclerDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    //通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATRRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int mOrientation;
    private boolean lastDividerOnVertial = true;
    private List<Integer> itemType;
    private Drawable itemTypeSplitDrawable;


    public RecyclerDecoration(Context context, int orientation) {
        final TypedArray ta = context.obtainStyledAttributes(ATRRS);
        this.mDivider = ta.getDrawable(0);
        ta.recycle();
        setOrientation(orientation);
    }


    public RecyclerDecoration(Context context, int orientation, int splitId) {
        mDivider = context.getResources().getDrawable(splitId);
        setOrientation(orientation);
    }

    public RecyclerDecoration(Context context, int orientation, int splitId, int splitAlpha) {
        try {
            mDivider = context.getResources().getDrawable(splitId);
        } catch (Exception e) {
            TypedArray ta = context.obtainStyledAttributes(ATRRS);
            this.mDivider = ta.getDrawable(0);
            ta.recycle();
        }
        setOrientation(orientation);
        mDivider.setAlpha(splitAlpha);
    }

    //设置屏幕的方向
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == HORIZONTAL_LIST) {//水平排列画竖线
            drawVerticalLine(c, parent, state);
        } else {
            drawHorizontalLine(c, parent, state);//竖直排列画横线
        }
    }

    //设置itemtype所对应的分割线
    public void setItemTypeSplit(List<Integer> itemTypes, Drawable itemTypeSplitDrawable) {
        if (itemType == null) {
            itemType = new ArrayList<>();
        }
        itemType.clear();
        itemType.addAll(itemTypes);
        this.itemTypeSplitDrawable = itemTypeSplitDrawable;
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        RecyclerView.Adapter adapter = parent.getAdapter();

        for (int i = 0; i < childCount; i++) {
            View v = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(v);
            if (i == childCount - 1 && !lastDividerOnVertial) {
                continue;
            }
            if (itemType != null && itemType.contains(adapter.getItemViewType(position))) {
                if (itemTypeSplitDrawable != null) {
                    drawHorizontSplit(c, itemTypeSplitDrawable, parent, i, left, right);
                }
            } else {
                drawHorizontSplit(c, mDivider, parent, i, left, right);
            }
        }
    }


    private void drawHorizontSplit(Canvas c, Drawable drawable, RecyclerView rv, int position, int left, int right) {
        final View child = rv.getChildAt(position);
        //获得child的布局信息
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + drawable.getIntrinsicHeight();
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(c);
    }

    /**
     * 画水平分割线时是否给最后一个item绘制分割线
     *
     * @param val true:绘制,false不绘制
     */
    public void shouldDrawLastDividerOnLast(boolean val) {
        lastDividerOnVertial = val;
    }

    //画竖线
    public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {//画横线，就是往下偏移一个分割线的高度
            int position = parent.getChildLayoutPosition(view);
            if (itemType != null && itemType.contains(parent.getAdapter().getItemViewType(position))) {
                outRect.set(0, 0, 0, itemTypeSplitDrawable != null ? itemTypeSplitDrawable.getIntrinsicHeight() : 0);
            } else {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }
            if (!lastDividerOnVertial && position == parent.getAdapter().getItemCount() - 1) {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
