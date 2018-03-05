package com.msy.globalaccess.data.bean.statistics;

import java.util.List;

/**
 * Created by shawn on 2017/7/11 0011.
 * <p>
 * description :景点认证时间的适配类
 */

public class ScenicCheckTimeCountListWrapper {
    private List<ScenicCheckTimeCountList> scenicCheckTimeCountList;

    public List<ScenicCheckTimeCountList> getScenicCheckTimeCountList() {
        return scenicCheckTimeCountList;
    }

    public void setScenicCheckTimeCountList(List<ScenicCheckTimeCountList> scenicCheckTimeCountList) {
        this.scenicCheckTimeCountList = scenicCheckTimeCountList;
    }
    /**
     * 景点认证时间
     */
    public static class ScenicCheckTimeCountList {
        private String name;
        private String countNum;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountNum() {
            return countNum;
        }

        public void setCountNum(String countNum) {
            this.countNum = countNum;
        }
    }
}
