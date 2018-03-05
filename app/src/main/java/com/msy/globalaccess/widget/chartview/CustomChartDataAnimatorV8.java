package com.msy.globalaccess.widget.chartview;

/**
 * Created by WuDebin on 2017/5/18.
 */

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.animation.ChartDataAnimator;
import lecho.lib.hellocharts.animation.DummyChartAnimationListener;


public class CustomChartDataAnimatorV8 implements ChartDataAnimator {

    final CustomChart chart;
    final Handler handler;
    final Interpolator interpolator = new AccelerateDecelerateInterpolator();
    long start;
    boolean isAnimationStarted = false;
    long duration;
    private final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - start;
            if (elapsed > duration) {
                isAnimationStarted = false;
                handler.removeCallbacks(runnable);
                chart.animationDataFinished();
                return;
            }
            float scale = Math.min(interpolator.getInterpolation((float) elapsed / duration), 1);
            chart.animationDataUpdate(scale);
            handler.postDelayed(this, 16);

        }
    };
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();

    public CustomChartDataAnimatorV8(CustomChart chart) {
        this.chart = chart;
        this.handler = new Handler();
    }

    @Override
    public void startAnimation(long duration) {
        if (duration >= 0) {
            this.duration = duration;
        } else {
            this.duration = DEFAULT_ANIMATION_DURATION;
        }

        isAnimationStarted = true;
        animationListener.onAnimationStarted();
        start = SystemClock.uptimeMillis();
        handler.post(runnable);
    }

    @Override
    public void cancelAnimation() {
        isAnimationStarted = false;
        handler.removeCallbacks(runnable);
        chart.animationDataFinished();
        animationListener.onAnimationFinished();
    }

    @Override
    public boolean isAnimationStarted() {
        return isAnimationStarted;
    }

    @Override
    public void setChartAnimationListener(ChartAnimationListener animationListener) {
        if (null == animationListener) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener;
        }
    }
}
