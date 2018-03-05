package com.msy.globalaccess.widget.chartview;

import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;

/**
 * Created by shawn on 2017/7/18 0018.
 * <p>
 * description :
 */

public class CustomAxis extends Axis {
    private boolean limitLength;

    public CustomAxis(List<AxisValue> axisValues) {
        super(axisValues);
    }

    public boolean isLimitLength() {
        return limitLength;
    }

    public void setLimitLength(boolean limitLength) {
        this.limitLength = limitLength;
    }
}
