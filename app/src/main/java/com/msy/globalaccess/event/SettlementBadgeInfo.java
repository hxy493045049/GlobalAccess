package com.msy.globalaccess.event;

/**
 * Created by hxy on 2017/2/22 0022.
 * <p>
 * description : 接受团队列表的小圆点信息
 */

public class SettlementBadgeInfo {

    private String backNum;//退款数量
    private String totalNum;//当前查询类型的总数量
    private String prePayNum;//预支付数量
    private String addPrePayNum;//追加预支付数量

    public String getAddPrePayNum() {
        return addPrePayNum;
    }

    public void setAddPrePayNum(String addPrePayNum) {
        this.addPrePayNum = addPrePayNum;
    }

    public String getBackNum() {
        return backNum;
    }

    public void setBackNum(String backNum) {
        this.backNum = backNum;
    }

    public String getPrePayNum() {
        return prePayNum;
    }

    public void setPrePayNum(String prePayNum) {
        this.prePayNum = prePayNum;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }
}
