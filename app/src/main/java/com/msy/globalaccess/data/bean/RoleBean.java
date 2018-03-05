package com.msy.globalaccess.data.bean;

/**
 * Created by pepys on 2017/2/14.
 * description:
 */
public class RoleBean {
    /**
     * 审批出团申请
     */
    public static String CHECKOUTTEAM = "CHECKOUTTEAM";
    /**
     * 出团
     */
    public static String OUTTEAM = "OUTTEAM";
    /**
     * 审批作废申请
     */
    public static String CHECKDISTEAM = "CHECKDISTEAM";
    /**
     * 作废团队
     */
    public static String DISTEAM = "DISTEAM";
    /**
     * 变更团队
     */
    public static String CHANGETEAM = "CHANGETEAM";
    /**
     * 审批团队变更
     */
    public static String CHECKCHANGETEAM = "CHECKCHANGETEAM";
    /**
     * 结算团队结算单
     */
    public static String AUDITTEAMAUDIT = "AUDITTEAMAUDIT";

    /**
     * 处理导游委派
     */
    public static String HANDLEGUIDEAPP = "HANDLEGUIDEAPP";

    /**
     * 权限列表
     */
    public static String[] RoleList = new String[]{CHECKOUTTEAM,OUTTEAM,CHECKDISTEAM,DISTEAM,CHANGETEAM,CHECKCHANGETEAM,AUDITTEAMAUDIT,HANDLEGUIDEAPP};
    /**
     * roleDesc : 查看团队模板
     * isOwn : 1
     * roleTag : LOOKTEAMMODULE
     */
    private String roleDesc;
    private String isOwn;
    private String roleTag;


    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public String getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(String isOwn) {
        this.isOwn = isOwn;
    }

    public String getRoleTag() {
        return roleTag;
    }

    public void setRoleTag(String roleTag) {
        this.roleTag = roleTag;
    }

}
