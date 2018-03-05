package com.msy.globalaccess.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shawn on 2017/5/16 0016.
 * <p>
 * description : 适用于gridlayoutmanager的decoration
 */

public class GridDecoration extends RecyclerView.ItemDecoration {
    private int offsetSpaces;
    private RecyclerView.LayoutManager manager;
    private boolean shouldDrawFirstRowTopDecoration;//是否绘制第一行的顶部部的decoration
    private boolean shouldDrawLastRowBottomDecoration;//是否绘制最后一行的底部的decoration
    private boolean shouldDrawFirstColumnLeftDecoration;//是否绘制第一列的左边的decoration
    private boolean shouldDrawLastColumnRightDecoration;//是否绘制最后一列的右边的decoration

    public GridDecoration(int offsetSpaces) {
        this.offsetSpaces = offsetSpaces;
    }

    public GridDecoration(int offsetSpaces,
                          boolean shouldDrawFirstRowTopDecoration,
                          boolean shouldDrawLastRowBottomDecoration,
                          boolean shouldDrawFirstColumnLeftDecoration,
                          boolean shouldDrawLastColumnRightDecoration) {
        this.offsetSpaces = offsetSpaces;
        this.shouldDrawFirstRowTopDecoration = shouldDrawFirstRowTopDecoration;
        this.shouldDrawLastRowBottomDecoration = shouldDrawLastRowBottomDecoration;
        this.shouldDrawFirstColumnLeftDecoration = shouldDrawFirstColumnLeftDecoration;
        this.shouldDrawLastColumnRightDecoration = shouldDrawLastColumnRightDecoration;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (manager == null) {
            manager = parent.getLayoutManager();
        }
        if (!(manager instanceof GridLayoutManager)) {
            throw new RuntimeException("该decoration只用于gridLayoutmanager");
        }

        int spanCount = ((GridLayoutManager) manager).getSpanCount();//设定的span
        int position = parent.getChildAdapterPosition(view);//view的坐标
        int childCount = parent.getAdapter().getItemCount();//child总数

        int direction = manager.getLayoutDirection();//排列方向

        //暂时只考虑水平方向
        if (direction == OrientationHelper.HORIZONTAL) {
            setHorizentalOffSet(outRect, position, childCount, spanCount);
        } else {
            // TODO: 2017/5/16 0016
        }
    }

    private void setHorizentalOffSet(Rect outRect, int position, int childCount, int spanCount) {
        int columnIndex = position % spanCount;//列数下标
        int rowIndex = (int) Math.floor(position * 1.0 / spanCount);//行数下标
        int totalRowSize = (int) Math.floor(childCount * 1.0 / spanCount);//总行数
        int left = offsetSpaces / 2, top = offsetSpaces / 2, right = offsetSpaces / 2, bottom = offsetSpaces / 2;

        //如果是第一行
        if (rowIndex == 0) {
            if (shouldDrawFirstRowTopDecoration) {
                top = offsetSpaces;
            } else {
                top = 0;
            }
        }
        //如果是最后一行
        if (rowIndex == totalRowSize) {
            if (shouldDrawLastRowBottomDecoration) {
                bottom = offsetSpaces;
            } else {
                bottom = 0;
            }
        }

        //如果是第一列
        if (columnIndex == 0) {
            if (shouldDrawFirstColumnLeftDecoration) {
                left = offsetSpaces;
            } else {
                left = 0;
            }
        }

        //如果是最后一列
        if (columnIndex == spanCount - 1) {
            if (shouldDrawLastColumnRightDecoration) {
                right = offsetSpaces;
            } else {
                right = 0;
            }
        }
        outRect.set(left, top, right, bottom);
    }
}
