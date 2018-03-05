package com.msy.globalaccess.data.bean.search;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 团队类型
 * Created by chensh on 2017/2/10 0010.
 */

public class SearchTeamTypeBean {
    /**
     * totalNum : 5
     * teamTypeList : [{"teamTypeId":"0","teamTypeName":"地接 国内游"},{"teamTypeId":"1","teamTypeName":"地接 入境游"},
     * {"teamTypeId":"2","teamTypeName":"自组 国内游"},{"teamTypeId":"3","teamTypeName":"自组 赴台游"},{"teamTypeId":"4",
     * "teamTypeName":"自组 出境游"}]
     */

    private int totalNum;
    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":5,"teamTypeList":[{"teamTypeId":"0","teamTypeName":"地接 国内游"},{"teamTypeId":"1",
     * "teamTypeName":"地接 入境游"},{"teamTypeId":"2","teamTypeName":"自组 国内游"},{"teamTypeId":"3","teamTypeName":"自组
     * 赴台游"},{"teamTypeId":"4","teamTypeName":"自组 出境游"}]}
     */
    private ArrayList<TeamTypeListBean> teamTypeList;

    @Inject
    public SearchTeamTypeBean() {

    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<TeamTypeListBean> getTeamTypeList() {
        return teamTypeList;
    }

    public void setTeamTypeList(ArrayList<TeamTypeListBean> teamTypeList) {
        this.teamTypeList = teamTypeList;
    }

    public static class TeamTypeListBean {
        /**
         * teamTypeId : 0
         * teamTypeName : 地接 国内游
         */

        private String teamTypeId;
        private String teamTypeName;

        public TeamTypeListBean() {

        }

        public TeamTypeListBean(String name, String id) {
            this.teamTypeName = name;
            this.teamTypeId = id;
        }

        public String getTeamTypeId() {
            return teamTypeId;
        }

        public void setTeamTypeId(String teamTypeId) {
            this.teamTypeId = teamTypeId;
        }

        public String getTeamTypeName() {
            return teamTypeName;
        }

        public void setTeamTypeName(String teamTypeName) {
            this.teamTypeName = teamTypeName;
        }
    }
}
