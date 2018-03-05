package com.msy.globalaccess.utils;

import android.accounts.NetworkErrorException;
import android.database.DatabaseErrorHandler;
import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.App;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.orhanobut.logger.Logger;

import java.net.UnknownServiceException;

/**
 * Created by hxy on 2017/2/14.
 * class description:
 */

public class ErrorUtils {
    public static String getErrorMessage(NoDataBean baseBean, String defaultMsg) {
        switch (baseBean.getStatus()) {
            case ResultCode.ARGUMENT_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,参数错误");
                break;
            case ResultCode.DATABASE_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,数据库异常");
                break;
            case ResultCode.INTERFACE_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,接口异常");
                break;
            case ResultCode.NET_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,网络异常");
                break;
            case ResultCode.SERVICE_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,服务器异常");
                break;
            case ResultCode.SIGN_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,签名失败");
                break;
            case ResultCode.SUCCESS_NO_MORE_DATA:
                Logger.i("请求成功,但是已经到达最后一页");
                break;
            case ResultCode.SUCCESS:
                Logger.i("操作成功");
                break;
            case ResultCode.USER_DELETED:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,用户已删除");
                break;
            case ResultCode.USER_INEXISTENCE:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,用户不存在");
                break;
            case ResultCode.USER_INVALID:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,用户状态无效，不允许登录");
                break;
            case ResultCode.USER_LOCKED:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,用户状态已锁定，不允许登录");
                break;
            case ResultCode.USER_PASSWORD_ERROR:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息,登录密码错误");
                break;
            default:
                Logger.e("错误码:" + baseBean.getStatus() + ", 错误信息:" + baseBean.getMessage());
                break;
        }
        return TextUtils.isEmpty(baseBean.getMessage()) ? defaultMsg : baseBean.getMessage();
    }

    public static String getErrorMessage(NoDataBean baseBean, int resId) {
        return getErrorMessage(baseBean, App.getResourceString(resId));
    }

    public static String getExceptionMessage(Throwable e, String defaultMsg) {
        String message = "";
        if (e instanceof NetworkErrorException) {
            message = App.getResourceString(R.string.internet_error);
        } else if (e instanceof DatabaseErrorHandler) {
            message = App.getResourceString(R.string.db_error);
        } else if (e instanceof IllegalArgumentException) {
            message = App.getResourceString(R.string.request_error);
        } else if (e instanceof JsonSyntaxException) {
            message = App.getResourceString(R.string.resolve_error);
        }

        // TODO: 2017/3/28 0028 自行补充完善
        return TextUtils.isEmpty(message) ? defaultMsg : message;
    }

    @ResultCode
    public static int getExceptionCode(Throwable e) {
        int resultCode = ResultCode.NET_ERROR;
        if (e instanceof NetworkErrorException) {
            //默认
        } else if (e instanceof DatabaseErrorHandler) {
            resultCode = ResultCode.DATABASE_ERROR;
        } else if (e instanceof UnknownServiceException) {//服务器异常有多种
            resultCode = ResultCode.SERVICE_ERROR;
        }
        // TODO: 2017/3/28 0028 自行补充完善

        return resultCode;
    }
}
