package com.msy.globalaccess.widget.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.msy.globalaccess.R;


/**
 * Created by hxy on 2016/12/28 0028.
 * <p>
 * description : 这个behavior解决recycleview和appbar公用导致的fling事件冲突
 */

public class SmoothAppbarBehavior extends AppBarLayout.Behavior {

    private static final int TOP_CHILD_FLING_THRESHOLD = 3;
    private boolean isPositive;

    public SmoothAppbarBehavior() {
    }

    public SmoothAppbarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View
            target, int nestedScrollAxes) {
        boolean parentResult = super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
        if (directTargetChild.getId() == R.id.scrollImmedia) {
            return true;
        } else {
            return parentResult;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float
            velocityX, float velocityY, boolean consumed) {
        if (target instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) target;
            consumed = velocityY > 0 || recyclerView.computeVerticalScrollOffset() > 0;
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

}