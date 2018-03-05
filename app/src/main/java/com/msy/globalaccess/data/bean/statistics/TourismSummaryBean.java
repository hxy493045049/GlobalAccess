package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

/**
 * Created by shawn on 2017/5/23 0023.
 * <p>
 * description : 旅游局数据概览的实体类
 */

public class TourismSummaryBean {


    /**
     * scenicCheckTeamCountList : [{"name":"武陵源核心景区（吴家峪门票站）","countNum":"256"},{"name":"天门山旅游区","countNum":"216"},
     * {"name":"张家界大峡谷旅游区B线（大峡谷+玻璃桥）","countNum":"140"},{"name":"武陵源核心景区（森林公园门票站）","countNum":"47"},
     * {"name":"张家界大峡谷旅游区C线(玻璃桥)","countNum":"33"},{"name":"张家界大峡谷A线（大峡谷）","countNum":"13"}]
     * customSourceCountList : [{"name":"湖南","countNum":"548"},{"name":"山东","countNum":"35"},{"name":"广东",
     * "countNum":"31"},{"name":"亚洲","countNum":"21"},{"name":"广西壮族自治区","countNum":"20"},{"name":"其他","countNum":"54"}]
     * scenicCheckCustomCount : 12872
     * scenicCheckTeamCount : 705
     * domesticCustomCount : 709
     * teamCountList : [{"date":"11月份","countNum":"0"},{"date":"12月份","countNum":"181"},{"date":"1月份",
     * "countNum":"2932"},{"date":"2月份","countNum":"5291"},{"date":"3月份","countNum":"11947"},{"date":"4月份",
     * "countNum":"17945"}]
     * scenicOrderCustomCountList : [{"scenicOrderCustomCountBillList":[{"date":"2017-04-01","countNum":"1455"},
     * {"date":"2017-04-02","countNum":"1889"},{"date":"2017-04-03","countNum":"2251"},{"date":"2017-04-04",
     * "countNum":"1045"},{"date":"2017-04-05","countNum":"941"}],"name":"武陵源核心景区（森林公园门票站）"},
     * {"scenicOrderCustomCountBillList":[{"date":"2017-04-01","countNum":"5345"},{"date":"2017-04-02",
     * "countNum":"7238"},{"date":"2017-04-03","countNum":"6632"},{"date":"2017-04-04","countNum":"3997"},
     * {"date":"2017-04-05","countNum":"3358"}],"name":"武陵源核心景区（吴家峪门票站）"},
     * {"scenicOrderCustomCountBillList":[{"date":"2017-04-01","countNum":"3123"},{"date":"2017-04-02",
     * "countNum":"3330"},{"date":"2017-04-03","countNum":"3710"},{"date":"2017-04-04","countNum":"2671"},
     * {"date":"2017-04-05","countNum":"1907"}],"name":"天门山旅游区"},
     * {"scenicOrderCustomCountBillList":[{"date":"2017-04-03","countNum":"3390"},{"date":"2017-04-04",
     * "countNum":"3314"},{"date":"2017-04-01","countNum":"3272"},{"date":"2017-04-02","countNum":"3266"},
     * {"date":"2017-04-05","countNum":"1680"}],"name":"张家界大峡谷旅游区"}]
     * createTeamCount : 724
     * domesticTeamCount : 60
     */

    private String scenicCheckCustomCount;
    private String scenicCheckTeamCount;
    private String domesticCustomCount;
    private String createTeamCount;
    private String domesticTeamCount;
    private List<ScenicCheckTeamCountListBean> scenicCheckTeamCountList;
    private List<GuestSourceBeanWrapper.GuestSourceBean> customSourceCountList;
    private List<TendencyBean> teamCountList;
    private List<ScenicOrderCustomCountListBean> scenicOrderCustomCountList;
    private List<TouristSexStatisticsBean> customSexCountList;
    private List<TouristAbroadStatisticsBean> customFromCountList;  //境内外游客对比
    private List<GuestSourceBeanWrapper.GuestSourceBean> teamStayDaysCountList; //停留天数


    public String getScenicCheckCustomCount() {
        return scenicCheckCustomCount;
    }

    public void setScenicCheckCustomCount(String scenicCheckCustomCount) {
        this.scenicCheckCustomCount = scenicCheckCustomCount;
    }

    public String getScenicCheckTeamCount() {
        return scenicCheckTeamCount;
    }

    public void setScenicCheckTeamCount(String scenicCheckTeamCount) {
        this.scenicCheckTeamCount = scenicCheckTeamCount;
    }

    public String getDomesticCustomCount() {
        return domesticCustomCount;
    }

    public void setDomesticCustomCount(String domesticCustomCount) {
        this.domesticCustomCount = domesticCustomCount;
    }

    public String getCreateTeamCount() {
        return createTeamCount;
    }

    public void setCreateTeamCount(String createTeamCount) {
        this.createTeamCount = createTeamCount;
    }

    public String getDomesticTeamCount() {
        return domesticTeamCount;
    }

    public void setDomesticTeamCount(String domesticTeamCount) {
        this.domesticTeamCount = domesticTeamCount;
    }

    public List<ScenicCheckTeamCountListBean> getScenicCheckTeamCountList() {
        return scenicCheckTeamCountList;
    }

    public void setScenicCheckTeamCountList(List<ScenicCheckTeamCountListBean> scenicCheckTeamCountList) {
        this.scenicCheckTeamCountList = scenicCheckTeamCountList;
    }

    public List<GuestSourceBeanWrapper.GuestSourceBean> getCustomSourceCountList() {
        return customSourceCountList;
    }

    public void setCustomSourceCountList(List<GuestSourceBeanWrapper.GuestSourceBean> customSourceCountList) {
        this.customSourceCountList = customSourceCountList;
    }

    public List<TendencyBean> getTeamCountList() {
        return teamCountList;
    }

    public void setTeamCountList(List<TendencyBean> teamCountList) {
        this.teamCountList = teamCountList;
    }

    public List<ScenicOrderCustomCountListBean> getScenicOrderCustomCountList() {
        return scenicOrderCustomCountList;
    }

    public void setScenicOrderCustomCountList(List<ScenicOrderCustomCountListBean>
                                                      scenicOrderCustomCountList) {
        this.scenicOrderCustomCountList = scenicOrderCustomCountList;
    }

    public List<TouristSexStatisticsBean> getCustomSexCountList() {
        return customSexCountList;
    }

    public void setCustomSexCountList(List<TouristSexStatisticsBean> customSexCountList) {
        this.customSexCountList = customSexCountList;
    }

    public List<TouristAbroadStatisticsBean> getCustomFromCountList() {
        return customFromCountList;
    }

    public void setCustomFromCountList(List<TouristAbroadStatisticsBean> customFromCountList) {
        this.customFromCountList = customFromCountList;
    }

    public List<GuestSourceBeanWrapper.GuestSourceBean> getTeamStayDaysCountList() {
        return teamStayDaysCountList;
    }

    public void setTeamStayDaysCountList(List<GuestSourceBeanWrapper.GuestSourceBean> teamStayDaysCountList) {
        this.teamStayDaysCountList = teamStayDaysCountList;
    }
}
