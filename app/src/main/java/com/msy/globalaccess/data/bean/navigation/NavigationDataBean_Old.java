package com.msy.globalaccess.data.bean.navigation;

import java.util.List;

/**
 * Created by WuDebin on 2017/2/21.
 */

public class NavigationDataBean_Old {

    /**
     * 图片连接地址（http://xxxx.xx.xx/xxx/xxx），图片名称：1.jpg，2.jpg,3.jpg,4.jpg
     */
    private String picUrl;
    /**
     * 图片张数（注：从1开始）
     */
    private int picNum;
    /**
     * 在线团队数
     */
    private String onlineTeamCount;
    /**
     *认证团队数
     */
    private String certificationTeamCount;
    /**
     *游客数量
     */
    private String customCount;
    /**
     * 旅行社接团团队数统计
     */
    private List<GuideSubBean> travelAgentTeamCountList;
    /**
     * 导游统计
     */
    private List<GuideSubBean> teamGuideCountList;
    /**
     * 客源地统计
     */
    private List<GuideSubBean> customSourceCountList;
    /**
     * 核心景区预约数
     */
    private List<GuideSubBean> scenicPreCountList;

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public String getOnlineTeamCount() {
        return onlineTeamCount;
    }

    public void setOnlineTeamCount(String onlineTeamCount) {
        this.onlineTeamCount = onlineTeamCount;
    }

    public String getCertificationTeamCount() {
        return certificationTeamCount;
    }

    public void setCertificationTeamCount(String certificationTeamCount) {
        this.certificationTeamCount = certificationTeamCount;
    }

    public String getCustomCount() {
        return customCount;
    }

    public void setCustomCount(String customCount) {
        this.customCount = customCount;
    }

    public List<GuideSubBean> getTravelAgentTeamCountList() {
        return travelAgentTeamCountList;
    }

    public void setTravelAgentTeamCountList(List<GuideSubBean> travelAgentTeamCountList) {
        this.travelAgentTeamCountList = travelAgentTeamCountList;
    }

    public List<GuideSubBean> getTeamGuideCountList() {
        return teamGuideCountList;
    }

    public void setTeamGuideCountList(List<GuideSubBean> teamGuideCountList) {
        this.teamGuideCountList = teamGuideCountList;
    }

    public List<GuideSubBean> getCustomSourceCountList() {
        return customSourceCountList;
    }

    public void setCustomSourceCountList(List<GuideSubBean> customSourceCountList) {
        this.customSourceCountList = customSourceCountList;
    }

    public List<GuideSubBean> getScenicPreCountList() {
        return scenicPreCountList;
    }

    public void setScenicPreCountList(List<GuideSubBean> scenicPreCountList) {
        this.scenicPreCountList = scenicPreCountList;
    }

    public class GuideSubBean{
        /**
         * 描述  如：团队类型  客源地名称 景区名称
         */
        private String name;
        /**
         * 人数
         */
        private int countNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCountNum() {
            return countNum;
        }

        public void setCountNum(int countNum) {
            this.countNum = countNum;
        }
    }
}
