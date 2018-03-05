package com.msy.globalaccess.data.holder;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.business.travelAgency.search.ui.SearchResultActivity;
import com.msy.globalaccess.data.bean.search.PerequisiteBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.msy.zc.commonutils.StringUtils;

/**
 * 团队筛选条件
 * Created by chensh on 2017/2/11 0011.
 */

public class SearchPerequisiteHolder {
    //输入框
    public final String EDIT = "Edit";
    //日期
    public final String DATE = "date";
    //显示滚轮
    public final String TEXT = "text";

    public final String SearchType = "searchType";//查询类型
    public final String TeamCode = "teamCode";//团队编号
    public final String TeamStatus = "teamStatus";//团队状态:0:编辑中;1:已提交;2:生效;3:作废;
    public final String TravelAgentId = "travelAgentId";//旅行社Id,旅行社查询接口中获取
    public final String TravelDepId = "travelDepId";//部门Id,旅行社部门查询接口中获取
    public final String TeamStartDate = "teamStartDate";//行程开始日期
    public final String TeamEndDate = "teamEndDate";//行程结束日期
    public final String TeamTypeId = "teamTypeId";//团队类型Id,团队类型查询接口中获取

    //结算查询
    public final String payCode = "payCode";//支付编号
    public final String outComeUnitCoding = "outComeUnitCoding";//支出单位编号
    public final String inComeUnitCoding = "inComeUnitCoding";//收入单位编号
    public final String createTimeStart = "createTimeStart";//创建开始时间
    public final String createTimeEnd = "createTimeEnd";//创建结束时间
    public final String submitPayTimeStart = "submitPayTimeStart";//支付开始时间
    public final String submitPayTimeEnd = "submitPayTimeEnd";//支付结束时间

    //游客查询
    public final String cardType = "cardType";
    public final String cardNum = "cardNum";
    public final String name = "name";
    public final String sex = "sex";
    public final String phoneNum = "phoneNum";
    public final String teamId = "teamId";

    //查询筛选列表
    public ArrayList<PerequisiteBean> departmentList = null;
    public HashMap<String, Object> paramMap = null;
    public ArrayList<PerequisiteBean> StatusList = null;
    public ArrayList<PerequisiteBean> CertificateList = null;
    public ArrayList<PerequisiteBean> SexList = null;

    private static class Holder {
        private static SearchPerequisiteHolder instance = new SearchPerequisiteHolder();
    }

    public static SearchPerequisiteHolder getInstance() {
        return Holder.instance;
    }

    /**
     * 条件
     */
    private static Map<String, PerequisiteBean> perequisiteBeen = null;

    public Map<String, PerequisiteBean> getPerequisiteBeen(int type) {
        paramMap(type);
        switch (type) {
            case SearchResultActivity.TEAM_SEARCH:
                setPerequisiteBeen();
                StatusList();
                adddEpartmentList(type);
                break;
            case SearchResultActivity.SETTLEMENT_SEARCH:
                setSettlementBeen();
                adddEpartmentList(type);
                break;
            case SearchResultActivity.TOURIST_SEARCH:
                setTouristBeen();
                CertificateList();
                SexList();
                break;
        }
        return perequisiteBeen;
    }

    /**
     * 团队筛选条件
     */
    private void setPerequisiteBeen() {
        perequisiteBeen = new LinkedHashMap<>();
        if ("2".equals(App.userHelper.getUser().getUserRoleType())) {
            perequisiteBeen.put(TravelAgentId, new PerequisiteBean("旅行社", TEXT, TravelAgentId));
        }else{
            perequisiteBeen.put(SearchType, new PerequisiteBean("处理类型", TEXT, SearchType));
        }
        perequisiteBeen.put(TeamTypeId, new PerequisiteBean("团队类型", TEXT, TeamTypeId));
        perequisiteBeen.put(TeamEndDate, new PerequisiteBean("行程结束日期", DATE, TeamEndDate));
        perequisiteBeen.put(TeamStartDate, new PerequisiteBean("行程开始日期", DATE, TeamStartDate));
        if ("0".equals(App.userHelper.getUser().getUserRoleType()) && StringUtils.isEmpty(App.userHelper.getUser().getUserDepName())) {
            perequisiteBeen.put(TravelDepId, new PerequisiteBean("部门", TEXT, TravelDepId));
        }
        perequisiteBeen.put(TeamStatus, new PerequisiteBean("状态", TEXT, TeamStatus));
        perequisiteBeen.put(TeamCode, new PerequisiteBean("团队编号", EDIT, TeamCode));
    }

    /**
     * 结算筛选条件
     */
    private void setSettlementBeen() {
        perequisiteBeen = new LinkedHashMap<>();
        perequisiteBeen.put(submitPayTimeEnd, new PerequisiteBean("支付结束时间", DATE, submitPayTimeEnd));
        perequisiteBeen.put(submitPayTimeStart, new PerequisiteBean("支付开始时间", DATE, submitPayTimeStart));
        perequisiteBeen.put(createTimeEnd, new PerequisiteBean("创建结束时间", DATE, createTimeEnd));
        perequisiteBeen.put(createTimeStart, new PerequisiteBean("创建开始时间", DATE, createTimeStart));
        perequisiteBeen.put(inComeUnitCoding, new PerequisiteBean("收入单位", TEXT, inComeUnitCoding));
        perequisiteBeen.put(outComeUnitCoding, new PerequisiteBean("支出单位", TEXT, outComeUnitCoding));
        perequisiteBeen.put(SearchType, new PerequisiteBean("结算类型", TEXT, SearchType));
        perequisiteBeen.put(payCode, new PerequisiteBean("支付编号", EDIT, payCode));
        perequisiteBeen.put(TeamCode, new PerequisiteBean("团队编号", EDIT, TeamCode));
    }

    /**
     * 游客查询表筛选条件
     */
    private void setTouristBeen() {
        perequisiteBeen = new LinkedHashMap<>();
        perequisiteBeen.put(cardType, new PerequisiteBean("证件类型", TEXT, cardType));
        perequisiteBeen.put(cardNum, new PerequisiteBean("证件号码", EDIT, cardNum));
        perequisiteBeen.put(name, new PerequisiteBean("姓名", EDIT, name));
        perequisiteBeen.put(sex, new PerequisiteBean("性别", TEXT, sex));
        perequisiteBeen.put(phoneNum, new PerequisiteBean("手机号", EDIT, phoneNum));
    }

    /**
     * 查询类型
     *
     * @param type 类型
     */
    private void adddEpartmentList(int type) {
        departmentList = new ArrayList<>();
        switch (type) {
            case SearchResultActivity.TEAM_SEARCH:
                departmentList.add(new PerequisiteBean("出团待审批", "0", SearchType));
                departmentList.add(new PerequisiteBean("变更待审批", "1", SearchType));
                departmentList.add(new PerequisiteBean("作废待审批", "2", SearchType));
                break;
            case SearchResultActivity.SETTLEMENT_SEARCH:
                departmentList.add(new PerequisiteBean("预支付待结算", "0", SearchType));
                departmentList.add(new PerequisiteBean("追加支付待结算", "1", SearchType));
                departmentList.add(new PerequisiteBean("退款待结算", "2", SearchType));
                break;
        }
    }

    /**
     * 默认参数集合
     *
     * @param type 查询类型
     */
    private void paramMap(int type) {
        paramMap = new HashMap<>();
        switch (type) {
            case SearchResultActivity.TEAM_SEARCH:
                paramMap.put(SearchType, "");
                paramMap.put(TeamCode, "");
                paramMap.put(TeamStatus, "");
                paramMap.put(TravelAgentId, "");
                paramMap.put(TravelDepId, "");
                paramMap.put(TeamStartDate, "");
                paramMap.put(TeamEndDate, "");
                paramMap.put(TeamTypeId, "");
                break;
            case SearchResultActivity.SETTLEMENT_SEARCH:
                paramMap.put(SearchType, "");
                paramMap.put(TeamCode, "");
                paramMap.put(payCode, "");
                paramMap.put(outComeUnitCoding, "");
                paramMap.put(inComeUnitCoding, "");
                paramMap.put(createTimeStart, "");
                paramMap.put(createTimeEnd, "");
                paramMap.put(submitPayTimeStart, "");
                paramMap.put(submitPayTimeEnd, "");
                paramMap.put(payCode, "");
                break;
            case SearchResultActivity.TOURIST_SEARCH:
                paramMap.put(cardType, "");
                paramMap.put(teamId, "");
                paramMap.put(cardNum, "");
                paramMap.put(name, "");
                paramMap.put(sex, "");
                paramMap.put(phoneNum, "");
                break;
            default://默认团队查询
                setPerequisiteBeen();
        }
    }

    /**
     * 团队查询  状态对应的值
     */
    private void StatusList() {
        StatusList = new ArrayList<>();
        StatusList.add(new PerequisiteBean("编辑中", "0", TeamStatus));
        StatusList.add(new PerequisiteBean("已提交", "1", TeamStatus));
        StatusList.add(new PerequisiteBean("生效", "2", TeamStatus));
        StatusList.add(new PerequisiteBean("作废", "3", TeamStatus));
    }

    /**
     * 游客的证件类型
     */
    private void CertificateList() {
        CertificateList = new ArrayList<>();
        CertificateList.add(new PerequisiteBean("身份证", "0", cardType));
        CertificateList.add(new PerequisiteBean("护照", "1", cardType));
        CertificateList.add(new PerequisiteBean("军人证", "2", cardType));
        CertificateList.add(new PerequisiteBean("其他", "3", cardType));
        CertificateList.add(new PerequisiteBean("户口簿", "5", cardType));
        CertificateList.add(new PerequisiteBean("港澳居民身份证", "6", cardType));
        CertificateList.add(new PerequisiteBean("港澳居民来往内地通行证", "7", cardType));
        CertificateList.add(new PerequisiteBean("台湾居民来往内地通行证", "8", cardType));
    }

    private void SexList() {
        SexList = new ArrayList<>();
        SexList.add(new PerequisiteBean("女", "0", sex));
        SexList.add(new PerequisiteBean("男", "1", sex));
    }

}
