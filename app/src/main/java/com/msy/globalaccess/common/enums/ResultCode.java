package com.msy.globalaccess.common.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_CANT_CHANGE;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_CANT_DISCARD;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_CREATE_FAILED;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_DISCARD_FAILED;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_FAILED;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_PROCESSED;
import static com.msy.globalaccess.common.enums.ResultCode.APPROVAL_USER_NOT_EXIST;
import static com.msy.globalaccess.common.enums.ResultCode.ARGUMENT_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.DATABASE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.ERROR_GUIDER_ARGUMENT;
import static com.msy.globalaccess.common.enums.ResultCode.GUIDER_ALREADY_BEEN_DELEGATE;
import static com.msy.globalaccess.common.enums.ResultCode.INTERFACE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.NET_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_NO_DATA;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_SEARCH_TIME_EMPTY;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_SETTLEMENT_ID_EMPTY;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_TEAM_ID_EMPTY;
import static com.msy.globalaccess.common.enums.ResultCode.SEARCH_USER_NOT_EXIST;
import static com.msy.globalaccess.common.enums.ResultCode.SERVICE_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SETTLEMENT_INSUFFICIENT_BALANCE;
import static com.msy.globalaccess.common.enums.ResultCode.SETTLEMENT_PAY_FAILED;
import static com.msy.globalaccess.common.enums.ResultCode.SETTLEMENT_PWD_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SETTLEMENT_USER_NOT_EXIST;
import static com.msy.globalaccess.common.enums.ResultCode.SIGN_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.SUCCESS;
import static com.msy.globalaccess.common.enums.ResultCode.SUCCESS_EMPTY;
import static com.msy.globalaccess.common.enums.ResultCode.SUCCESS_NO_MORE_DATA;
import static com.msy.globalaccess.common.enums.ResultCode.USER_DELETED;
import static com.msy.globalaccess.common.enums.ResultCode.USER_INEXISTENCE;
import static com.msy.globalaccess.common.enums.ResultCode.USER_INVALID;
import static com.msy.globalaccess.common.enums.ResultCode.USER_LOCKED;
import static com.msy.globalaccess.common.enums.ResultCode.USER_PASSWORD_ERROR;
import static com.msy.globalaccess.common.enums.ResultCode.USER_UNLOCKED;

/**
 * Created by hxy on 2017/2/8.
 * class description: 用于定义状态码
 */
@IntDef({NET_ERROR, SERVICE_ERROR, DATABASE_ERROR, SUCCESS, SUCCESS_NO_MORE_DATA, ARGUMENT_ERROR,
        SIGN_ERROR, INTERFACE_ERROR, USER_INEXISTENCE, USER_PASSWORD_ERROR, USER_INVALID, USER_DELETED, USER_LOCKED,
        USER_UNLOCKED, SEARCH_NO_DATA, SEARCH_USER_NOT_EXIST, SEARCH_TEAM_ID_EMPTY, SEARCH_SETTLEMENT_ID_EMPTY,
        SEARCH_SEARCH_TIME_EMPTY, APPROVAL_USER_NOT_EXIST, APPROVAL_DISCARD_FAILED, APPROVAL_FAILED,
        APPROVAL_CANT_DISCARD, APPROVAL_PROCESSED, APPROVAL_CANT_CHANGE, APPROVAL_CREATE_FAILED,
        SETTLEMENT_USER_NOT_EXIST, SETTLEMENT_PAY_FAILED, SETTLEMENT_PWD_ERROR, SETTLEMENT_INSUFFICIENT_BALANCE,
        SUCCESS_EMPTY, ERROR_GUIDER_ARGUMENT, GUIDER_ALREADY_BEEN_DELEGATE})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultCode {
    //************通用接口状态说明************
    //成功
    int SUCCESS = 0;//'操作成功'
    int SUCCESS_NO_MORE_DATA = 0x111110;//请求成功,但是已经到达最后一页  0<data.size<maxSize
    int SUCCESS_EMPTY = 0x111100;//请求成功,但是data中的数据为空

    //失败类型
    int NET_ERROR = -1; //网络异常
    int SERVICE_ERROR = -2;//服务器异常
    int DATABASE_ERROR = -3;//数据库异常

    int ARGUMENT_ERROR = 1;//'参数错误'
    int SIGN_ERROR = 2;//'签名失败'
    int INTERFACE_ERROR = 3;//'接口异常'


    //************登录失败接口状态说明****
    int USER_INEXISTENCE = 1000;//用户不存在
    int USER_PASSWORD_ERROR = 1001;//登录密码错误
    int USER_INVALID = 1002;//用户状态无效，不允许登录
    int USER_DELETED = 1003;//用户已删除
    int USER_LOCKED = 1004;//用户状态已锁定，不允许登录
    int USER_UNLOCKED = 1005;//帐号已解除锁定,请重新登录

    //旅行社、部门、团队类型、团队、酒店、景区、车队、保险公司、团队详情、结算单详情查询、团队游客查询，统计查询接口状态说明

    int SEARCH_NO_DATA = 2003;//查询无数据
    int SEARCH_USER_NOT_EXIST = 2000;//用户不存在
    int SEARCH_TEAM_ID_EMPTY = 2001;//团队ID不能为空
    int SEARCH_SETTLEMENT_ID_EMPTY = 2002;//结算单ID不能为空
    int SEARCH_SEARCH_TIME_EMPTY = 2004;//查询时间不能为空


    //团队单审批流程接口状态说明
    int APPROVAL_USER_NOT_EXIST = 3000;//用户不存在  // FIXME: 2017/2/21 0021 和code==2000的区别是什么,去问后台
    int APPROVAL_DISCARD_FAILED = 3001;//作废申请失败
    int APPROVAL_FAILED = 3002;//审批失败
    int APPROVAL_CANT_DISCARD = 3003;//无法作废
    int APPROVAL_PROCESSED = 3004;//该团队已处理过审批申请
    int APPROVAL_CANT_CHANGE = 3005;//该团队存在已预约的景点，无法变更。请先取消预约后再试
    int APPROVAL_CREATE_FAILED = 3006;//出团失败


    //5、结算单结算流程接口状态说明
    int SETTLEMENT_USER_NOT_EXIST = 4000;//用户不存在
    int SETTLEMENT_PAY_FAILED = 4001;//支付失败
    int SETTLEMENT_PWD_ERROR = 4002;//支付密码错误
    int SETTLEMENT_INSUFFICIENT_BALANCE = 4003;//部门余额不足

    //导游编辑
    int ERROR_GUIDER_ARGUMENT = 6;//没有有效导游
    int GUIDER_ALREADY_BEEN_DELEGATE = 5;//导游已经被委派

}
