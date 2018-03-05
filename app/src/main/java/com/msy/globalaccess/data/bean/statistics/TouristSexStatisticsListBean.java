package com.msy.globalaccess.data.bean.statistics;


import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;

/**
 * Created by pepys on 2017/7/10
 * description: 游客性别统计
 */
public class TouristSexStatisticsListBean {

    private List<TouristSexStatisticsBean> customSexCountList;

    private PieChartData sexStatisticsPie;


    public List<TouristSexStatisticsBean> getCustomSexCountList() {
        return customSexCountList;
    }

    public void setCustomSexCountList(List<TouristSexStatisticsBean> customSexCountList) {
        this.customSexCountList = customSexCountList;
    }

    public PieChartData getSexStatisticsPie() {
        return sexStatisticsPie;
    }

    public void setSexStatisticsPie(PieChartData sexStatisticsPie) {
        this.sexStatisticsPie = sexStatisticsPie;
    }
}
