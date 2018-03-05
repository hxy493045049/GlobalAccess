package com.msy.globalaccess.listener;

import com.msy.globalaccess.data.bean.team.TeamMemberBean;
import com.msy.globalaccess.data.bean.team.TeamMemberSourceBean;
import com.msy.globalaccess.data.bean.tourism.TouristsInfoBean;

import java.util.ArrayList;

/**
 * Created by pepys on 2017/2/11.
 * description:设置客源地信息
 */
public interface SetTeamDetailSourceListener {
    /**
     * 设置客源地信息
     * @param teamMemberSourceList 客源地信息
     * @param teamMemberList    游客信息
     */
    void setTeamDetailSourceListener(ArrayList<TeamMemberSourceBean> teamMemberSourceList, ArrayList<TeamMemberBean> teamMemberList,TouristsInfoBean touristsInfo,String teamId);
}
