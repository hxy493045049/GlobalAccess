package com.msy.globalaccess.widget.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by hxy on 2016/12/27 0027.
 * <p>
 * description :
 */

public class FABBehavior extends FloatingActionButton.Behavior {
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    public boolean upAnimFlag = false, downAnimFlag = false;
    private ShowState show = new ShowState();
    private HideState hide = new HideState();

    public FABBehavior() {
        super();
    }

    public FABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View
            directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child,
                                          View dependency) {
        super.onDependentViewChanged(parent, child, dependency);
        if (dependency instanceof NestedScrollView) {
            NestedScrollView view = (NestedScrollView) dependency;
            if (view.getScrollY() == 0 && !upAnimFlag && child.getVisibility() == View.VISIBLE) {
                animateOuts(child);
            }
        }
        return false;
    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int
            dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        if (dyConsumed > 0 && !upAnimFlag && child.getVisibility() == View.VISIBLE) {
            animateOuts(child);//手指上滑
        } else if (dyConsumed <= 0 && !downAnimFlag && child.getVisibility() == View.INVISIBLE) {
            animateIn(child);//手指下滑
        }


        //        if (dyConsumed > 0 && !upAnimFlag && child.getVisibility() == View.VISIBLE) {
        //            animateOuts(child);
        //        } else if (dyConsumed <= 0 && !downAnimFlag && child.getVisibility() == View.GONE) {
        //            animateIn(child);
        //        }
    }

    //    @Override
    //    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target,
    //                                  int dx, int dy, int[] consumed) {
    //        if (!upAnimFlag && child.getVisibility() == View.VISIBLE) {
    //            animateOuts(child);
    //        }
    //        if (dy > 0 && mDySinceDirectionChange < 0
    //                || dy < 0 && mDySinceDirectionChange > 0) {
    //            child.animate().cancel();
    //            mDySinceDirectionChange = 0;
    //        }
    //
    //        mDySinceDirectionChange += dy;
    //
    //        if (mDySinceDirectionChange > child.getHeight()
    //                && child.getVisibility() == View.VISIBLE) {
    //            animateOuts(child);
    //        } else if (mDySinceDirectionChange < 0 && child.getVisibility() == View.GONE) {
    //            animateIn(child);
    //        }

    //        if (dy > 0 && !upAnimFlag && child.getVisibility() == View.VISIBLE) {
    //            animateOuts(child);
    //        } else if (dy <= 0 && !downAnimFlag && child.getVisibility() == View.GONE) {
    //            animateIn(child);
    //        }
    //    }

    public void animateIn(FloatingActionButton child) {
        ViewCompat.animate(child).rotation(0).scaleX(1.0F).scaleY(1.0F).alpha(1.0F).setInterpolator(INTERPOLATOR)
                .withLayer().setDuration(300).setListener(show).start();
    }

    public void animateOuts(FloatingActionButton child) {
        ViewCompat.animate(child).rotation(360).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR)
                .withLayer().setDuration(300).setListener(hide).start();
    }

    public class ShowState implements ViewPropertyAnimatorListener {
        @Override
        public void onAnimationStart(View view) {
            view.setVisibility(View.VISIBLE);
            downAnimFlag = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            downAnimFlag = false;
        }

        @Override
        public void onAnimationCancel(View view) {
            downAnimFlag = false;
        }
    }

    public class HideState implements ViewPropertyAnimatorListener {
        @Override
        public void onAnimationStart(View view) {
            upAnimFlag = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            upAnimFlag = false;
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationCancel(View view) {
            upAnimFlag = false;
        }
    }
}
