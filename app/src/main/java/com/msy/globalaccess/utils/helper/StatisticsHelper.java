package com.msy.globalaccess.utils.helper;

import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import cn.msy.zc.commonutils.StringUtils;

/**
 * Created by shawn on 2017/7/25 0025.
 * <p>
 * description :
 */

public class StatisticsHelper {
    public static void hideView(List<? extends View> data) {
        if (data != null && data.size() > 0) {
            for (View v : data) {
                v.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 重绘label的大小,目的:让radio(百分比)完全显示,label动态根据宽度显示
     *
     * @param tvRadio     显示百分比的textview
     * @param tvLabel     显示label的textview
     * @param layoutWidth 计算得出的可用宽度
     */
    public static void resetTextViewWidth(TextView tvRadio, TextView tvLabel, int layoutWidth) {
        int contentWidth = layoutWidth - (int) StringUtils.getStringWidth(tvRadio);//得出label可用的最大宽度
        int oughtWidth = (int) StringUtils.getStringWidth(tvLabel);
        if (oughtWidth > contentWidth) {
            tvLabel.setWidth(contentWidth);
        } else {
            tvLabel.setWidth(oughtWidth);
        }
    }

    /**
     * 计算当前item的比率,如果当前的总比率等于0或者当前不是最后一个item,
     * 那么  ratio=(x/sum)*100
     * 否则,当前为最后一个并且当前的总比率不等于0
     * 那么 ratio=100-sum
     *
     * @param currentNum   当前的值
     * @param totalNum     总值
     * @param totalPercent 累积的比率
     * @param isLast       是否最后一个
     * @param scale        bigDecimal的scale
     * @param roundingMode bigDecimal的模式
     * @return 计算后的比率
     */
    public static double calculateRatio(double currentNum, double totalNum, double totalPercent, boolean isLast,
                                        int scale, int roundingMode) {
        BigDecimal ratio;
        if (!isLast || totalPercent == 0) {
            ratio = BigDecimal.valueOf(currentNum)
                    .multiply(BigDecimal.valueOf(100.0))
                    .divide(BigDecimal.valueOf(totalNum), scale, roundingMode);
        } else {

            ratio = BigDecimal.valueOf(100.0).subtract(BigDecimal.valueOf(totalPercent));
        }
        return ratio.setScale(scale,roundingMode).doubleValue();
    }
}
